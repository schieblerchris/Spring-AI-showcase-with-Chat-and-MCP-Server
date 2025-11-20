package com.github.sc.apps.saisc.chat.view;

import com.github.sc.apps.saisc.chat.view.components.ChatBubbleCard;
import com.github.sc.apps.saisc.chat.view.components.StarterSuggestionsComponent;
import com.github.sc.apps.saisc.chatmodel.api.OpenAIAdapter;
import com.github.sc.apps.saisc.shared.mcp.ToolMarkerInterface;
import com.github.sc.apps.saisc.shared.web.BaseLayout;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.markdown.Markdown;
import com.vaadin.flow.component.messages.MessageInput;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.Scroller;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.router.*;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.jetbrains.annotations.NotNull;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.ChatClientResponse;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.memory.ChatMemoryRepository;
import org.springframework.ai.chat.messages.MessageType;
import org.springframework.ai.chat.prompt.ChatOptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import reactor.core.publisher.Flux;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@PageTitle("Chat")
@Route(value = "chat/:chatId", layout = BaseLayout.class)
public class ChatView extends VerticalLayout implements BeforeEnterObserver {

    private static final String SYSTEM_PROMPT = """
            *Persona*:
            You are a helpful and honest assistant who will help the users to find what they need.
            You are always focussed on unbiased responses.
            *Task*:
            Your goal is to provide the user with the information they need and help them find efficient solutions.
            If the question of the user cannot be full filled, provide the user with an alternative solution.
            If you rely on tool calls, always include your decision making and verify the data from the tool call.
            *Context*:
            All needed context besides the user prompt can be accessed by the tools provided.
            *Format*:
            If you reference any of the following data add a http link to the corresponding detail page:
            * Persons `http://localhost:58080/persons/{personId}`
            * Hobbies `http://localhost:58080/hobbies/{hobbyId}`
            * Events `http://localhost:58080/events/{eventId}`
            Use Markdown notation to abbreviate the links, either on the ID or the name/title.
            Always include a table containing your tool calls with the input parameters in the final response.
            """;
    private final VerticalLayout chatLayout = new VerticalLayout();
    private final StarterSuggestionsComponent starterSuggestions = new StarterSuggestionsComponent(this::handleUserPrompt);
    private final ChatMemoryRepository chatMemoryRepository;
    private final ChatClient.Builder chatClientBuilder;
    private final MessageChatMemoryAdvisor chatMemoryAdvisor;
    private final Map<String, Tool> toolsMap;
    private final List<String> models;
    private ChatClient chatClient;
    private ChatId chatId;
    private String model;
    private double temperature = 0.3;

    @Autowired
    public ChatView(ChatClient.Builder chatClientBuilder, ChatMemory chatMemory, List<ToolMarkerInterface> tools, ChatMemoryRepository chatMemoryRepository, OpenAIAdapter openAIAdapter, @Value("${application.preferred.ai.model}") List<String> preferredModel) {
        this.chatClientBuilder = chatClientBuilder;
        this.toolsMap = new TreeMap<>(tools.stream().collect(Collectors.toMap(ToolMarkerInterface::name, toolMarkerInterface -> new Tool(toolMarkerInterface.name(), toolMarkerInterface, toolMarkerInterface.functions()))));
        this.chatMemoryAdvisor = MessageChatMemoryAdvisor.builder(chatMemory).build();

        this.chatMemoryRepository = chatMemoryRepository;
        this.models = openAIAdapter.getModels().stream().map(OpenAIAdapter.ModelData::id).toList();
        this.model = models.stream().filter(preferredModel::contains).findFirst().orElse(models.getFirst());

        // Button zum Starten eines komplett neuen Chats (oben rechts als globale Aktion)
        var newChatButton = new Button("Neuer Chat", VaadinIcon.PLUS.create());
        newChatButton.addClickListener(e -> this.getUI().ifPresent(ui ->
                ui.navigate(ChatView.class, new RouteParameters(Map.of("chatId", UUID.randomUUID().toString())))
        ));

        var headerLayout = new HorizontalLayout(newChatButton);
        headerLayout.setWidthFull();
        headerLayout.setJustifyContentMode(FlexComponent.JustifyContentMode.END);
        this.add(headerLayout);

        var modelSelect = getModelSelect();
        HorizontalLayout modelOptionsLayout = new HorizontalLayout();
        modelOptionsLayout.add(modelSelect);

        var temperatureField = getTemperatureField();
        modelOptionsLayout.add(temperatureField);

        var toolBoyLayout = new VerticalLayout();
        toolBoyLayout.add(new Text("Tools:"));
        var toolLayout = new HorizontalLayout();
        toolLayout.setDefaultVerticalComponentAlignment(Alignment.BASELINE);
        toolsMap.forEach((k, v) -> {
            var checkBox = new Checkbox(k);
            checkBox.setTooltipText(Strings.join(v.getFunctions(), '\n'));
            checkBox.setValue(v.isEnabled());
            checkBox.addValueChangeListener(e -> v.setEnabled(e.getValue()));
            toolLayout.add(checkBox);
        });
        toolBoyLayout.add(toolLayout);
        modelOptionsLayout.add(toolBoyLayout);

        this.add(modelOptionsLayout);

        this.chatLayout.setWidth(100.0f, Unit.PERCENTAGE);
        this.chatLayout.setSizeFull();
        this.chatLayout.setSpacing(12, Unit.PIXELS);
        var scroller = new Scroller(chatLayout);
        scroller.setSizeFull();
        this.addAndExpand(scroller);

        this.add(starterSuggestions);

        MessageInput messageInput = new MessageInput();
        messageInput.addSubmitListener(this::onSubmit);
        messageInput.setWidthFull();
        this.add(messageInput);
        initChatClient(model);
    }

    private @NotNull Select<String> getModelSelect() {
        var modelSelect = new Select<String>();
        modelSelect.setLabel("Model");
        modelSelect.setItems(models);
        modelSelect.setValue(model);
        modelSelect.addValueChangeListener(e -> {
            log.info("Model changed to {}", e.getValue());
            this.model = e.getValue();
            this.initChatClient(e.getValue());
        });
        return modelSelect;
    }

    private @NotNull NumberField getTemperatureField() {
        var temperatureField = new NumberField();
        temperatureField.setLabel("Temperature");
        temperatureField.setValue(temperature);
        temperatureField.setStepButtonsVisible(true);
        temperatureField.setMin(0);
        temperatureField.setStep(0.1);
        temperatureField.setMax(1);
        temperatureField.addValueChangeListener(e -> {
            log.info("Temperature changed to {}", e.getValue());
            this.temperature = e.getValue();
            this.initChatClient(model);
        });
        return temperatureField;
    }

    public void initChatClient(String modelName) {
        this.chatClient = chatClientBuilder
                .defaultOptions(
                        ChatOptions.builder()
                                .model(modelName)
                                .temperature(temperature)
                                .build()
                )
                .defaultSystem(SYSTEM_PROMPT)
                .defaultAdvisors(chatMemoryAdvisor)
                .build();
    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        var params = event.getRouteParameters();
        var maybeChatId = params.get("chatId");

        if (maybeChatId.isEmpty()) {
            var newId = UUID.randomUUID().toString();
            event.rerouteTo(ChatView.class, new RouteParameters(Map.of("chatId", newId)));
            return;
        }
        var requestedId = maybeChatId.get();
        var messages = chatMemoryRepository.findByConversationId(requestedId);
        if (!messages.isEmpty()) {
            messages.forEach(chatMessage -> {
                if (chatMessage.getMessageType() == MessageType.USER) {
                    addUserCard(chatMessage.getText());
                } else {
                    addBotCard(new Markdown(chatMessage.getText()));
                }
            });
            starterSuggestions.setHasSentMessage();
        }

        this.chatId = new ChatId(requestedId);
    }

    private void onSubmit(MessageInput.SubmitEvent submitEvent) {
        starterSuggestions.setHasSentMessage();
        handleUserPrompt(submitEvent.getValue());
    }

    private void handleUserPrompt(String userPrompt) {
        addUserCard(userPrompt);

        var botResponse = new Markdown();
        var botCard = addBotCard(botResponse);
        this.getUI().ifPresent(ui -> ui.access(botCard::startProgress));

        this.getUI().ifPresent(ui -> chatStreamDetailed(userPrompt).doOnComplete(() -> ui.access(botCard::stopProgress)).subscribe(ccr -> {
            if (ccr.chatResponse() != null) {
                var token = ccr.chatResponse().getResult().getOutput().getText();
                if (token != null) {
                    ui.access(() -> botResponse.appendContent(token));
                }
            }
        }));
    }

    private void addUserCard(String userPrompt) {
        var userCard = new ChatBubbleCard("User", userPrompt);
        userCard.addClassName("chat-bubble-right");
        chatLayout.add(userCard);
        chatLayout.setAlignSelf(Alignment.END, userCard);
    }

    private ChatBubbleCard addBotCard(Markdown chatMessage) {
        var botCard = new ChatBubbleCard("Bot");
        botCard.addBody(chatMessage);
        chatLayout.add(botCard);
        chatLayout.setAlignSelf(Alignment.START, botCard);
        return botCard;
    }

    private Flux<ChatClientResponse> chatStreamDetailed(String userInput) {
        return chatClient
                .prompt()
                .advisors(advisorSpec -> advisorSpec.param(ChatMemory.CONVERSATION_ID, chatId.id()))
                .user(userInput)
                .tools(toolsMap.values().stream().filter(Tool::isEnabled).map(Tool::getToolImplementation).toArray())
                .stream()
                .chatClientResponse();
    }

    private record ChatId(String id) {
    }

    @Data
    private static class Tool {
        private final String name;
        private final Object toolImplementation;
        private final List<String> functions;
        private boolean enabled = true;
    }

}