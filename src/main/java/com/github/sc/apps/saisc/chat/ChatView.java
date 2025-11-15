package com.github.sc.apps.saisc.chat;

import com.github.sc.apps.saisc.common.frontend.BaseLayout;
import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.accordion.AccordionPanel;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.messages.MessageInput;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.Scroller;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.UUID;

@Slf4j
@PageTitle("Chat")
@Route(value = "chat", layout = BaseLayout.class)
public class ChatView extends VerticalLayout {

    private final ChatService chatService;
    private final VerticalLayout div;
    private final StarterSuggestionsComponent starterSuggestions = new StarterSuggestionsComponent(this::handleUserPrompt);
    private final ChatResponseParser chatResponseParser;
    private String chatId = UUID.randomUUID().toString();

    @Autowired
    public ChatView(ChatService chatService, ChatResponseParser chatResponseParser) {
        this.chatService = chatService;
        this.chatResponseParser = chatResponseParser;

        div = new VerticalLayout();
        div.setWidth(100.0f, Unit.PERCENTAGE);
        div.setSizeFull();
        div.setSpacing(12, Unit.PIXELS);

        var scroller = new Scroller(div);
        scroller.setSizeFull();
        this.addAndExpand(scroller);

        this.add(starterSuggestions);

        MessageInput messageInput = new MessageInput();
        messageInput.addSubmitListener(this::onSubmit);
        messageInput.setWidthFull();
        this.add(messageInput);
    }

    private void onSubmit(MessageInput.SubmitEvent submitEvent) {
        starterSuggestions.setHasSentMessage();
        handleUserPrompt(submitEvent.getValue());
    }

    private void handleUserPrompt(String userPrompt) {
        var userCard = new ChatBubbleCard("User", userPrompt);
        userCard.addClassName("chat-bubble-right");
        div.add(userCard);
        div.setAlignSelf(FlexComponent.Alignment.END, userCard);

        var botCard = new ChatBubbleCard("Bot");
        var additionalOutput = botCard.getAdditionalOutput();
        var paragraph = botCard.getParagraph();
        div.add(botCard);
        div.setAlignSelf(FlexComponent.Alignment.START, botCard);
        this.getUI().ifPresent(ui -> ui.access(botCard::startProgress));

        var messageBuffer = new StringBuilder();

        this.getUI().ifPresent(ui ->
                chatService.chatStreamDetailed(userPrompt, chatId)
                        .doOnComplete(() -> ui.access(() -> {
                            var response = chatResponseParser.parse(messageBuffer.toString());
                            response.thoughts().forEach(t -> {
                                var layout = new VerticalLayout();
                                layout.add(new Paragraph(t));
                                var accordionPanel = new AccordionPanel("Thought some time");
                                accordionPanel.add(layout);
                                additionalOutput.add(accordionPanel);
                            });
                            paragraph.setText(response.message());
                            botCard.stopProgress();
                        }))
                        .subscribe(ccr -> {
                            if (ccr.chatResponse() != null) {
                                var token = ccr.chatResponse().getResult().getOutput().getText();
                                if (token != null) {
                                    messageBuffer.append(token);
                                    ui.access(() -> paragraph.setText(messageBuffer.toString()));
                                }
                            }
                        }))
        ;
    }


}