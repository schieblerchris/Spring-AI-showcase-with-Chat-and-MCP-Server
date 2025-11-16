package com.github.sc.apps.saisc.chat.view;

import com.github.sc.apps.saisc.common.mcp.ToolMarkerInterface;
import com.github.sc.apps.saisc.common.view.BaseLayout;
import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.markdown.Markdown;
import com.vaadin.flow.component.messages.MessageInput;
import com.vaadin.flow.component.orderedlayout.Scroller;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.ChatClientResponse;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.memory.ChatMemoryRepository;
import org.springframework.ai.chat.messages.MessageType;
import org.springframework.beans.factory.annotation.Autowired;
import reactor.core.publisher.Flux;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Slf4j
@PageTitle("Chat")
@Route(value = "chat/:chatId", layout = BaseLayout.class)
public class ChatView extends VerticalLayout implements BeforeEnterObserver {

    private static final String SYSTEM_PROMPT = """
            You are a personal assistant to help finding the best suited person for a given activity.
            Always try to find the best fitting person by availability and hobby skill level.
            To reduce biases resolve the person data, like first name, last name, age, gender, etc. at the last possible moment.
            If you reference any person add a http link to their profile http://localhost:58080/persons/{personId}.
            Always include a table containing your tool calls with the input parameters in the final response.
            """;
    private final VerticalLayout chatLayout = new VerticalLayout();
    private final StarterSuggestionsComponent starterSuggestions = new StarterSuggestionsComponent(this::handleUserPrompt);
    private final ChatMemoryRepository chatMemoryRepository;
    private final ChatClient chatClient;
    private final Object[] tools;
    private ChatId chatId;

    @Autowired
    public ChatView(ChatClient.Builder chatClientBuilder,
                    ChatMemory chatMemory, List<ToolMarkerInterface> tools, ChatMemoryRepository chatMemoryRepository) {
        this.tools = tools.toArray(new ToolMarkerInterface[0]);
        var chatMemoryAdvisor = MessageChatMemoryAdvisor
                .builder(chatMemory)
                .build();
        this.chatClient = chatClientBuilder
                .defaultSystem(SYSTEM_PROMPT)
                .defaultAdvisors(chatMemoryAdvisor)
                .build();
        this.chatMemoryRepository = chatMemoryRepository;

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
        return chatClient.prompt()
                .advisors(advisorSpec ->
                        advisorSpec.param(ChatMemory.CONVERSATION_ID, chatId.id())
                )
                .user(userInput)
                .tools(tools)
                .stream()
                .chatClientResponse();
    }

    private record ChatId(String id) {
    }

}