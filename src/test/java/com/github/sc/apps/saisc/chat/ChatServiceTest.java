package com.github.sc.apps.saisc.chat;

import com.github.sc.apps.saisc.TestcontainersConfiguration;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

import java.util.ArrayList;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Import(TestcontainersConfiguration.class)
@Slf4j
class ChatServiceTest {

    @Autowired
    private ChatService chatService;

    @Test
    void chatStream() {
        var messages = new ArrayList<ChatMessage>();
        var isThinking = new AtomicBoolean(false);
        var currentMessage = new AtomicReference<ChatMessage>(null);
        var message = chatService.chatStreamDetailed("What is the current time?", "test").mapNotNull(ccr -> {
            if (ccr.chatResponse() != null) {
                var text = ccr.chatResponse().getResult().getOutput().getText();
                log.info("{}", ccr);
                if (text == null) {
                    return null;
                }

                if (text.equalsIgnoreCase("<think>")) {
                    isThinking.set(true);
                } else if (text.equalsIgnoreCase("</think>")) {
                    isThinking.set(false);
                } else {
                    if (isThinking.get()) {
                        if (currentMessage.get() == null) {
                            currentMessage.set(new ChatMessage(text, ChatMessageType.THINKING));
                        } else {
                            if (currentMessage.get().type() != ChatMessageType.THINKING) {
                                messages.add(currentMessage.get());
                                currentMessage.set(new ChatMessage(text, ChatMessageType.THINKING));
                            } else {
                                currentMessage.set(currentMessage.get().append(text));
                            }
                        }
                        return new ChatMessage(text, ChatMessageType.THINKING);
                    } else {
                        if (currentMessage.get() == null) {
                            currentMessage.set(new ChatMessage(text, ChatMessageType.THINKING));
                        } else {
                            if (currentMessage.get().type() != ChatMessageType.RESPONSE) {
                                messages.add(currentMessage.get());
                                currentMessage.set(new ChatMessage(text, ChatMessageType.RESPONSE));
                            } else {
                                currentMessage.set(currentMessage.get().append(text));
                            }
                        }
                        return new ChatMessage(text, ChatMessageType.RESPONSE);
                    }
                }
            }
            return null;
        }).collectList().block();
        messages.add(currentMessage.get());
        log.info("{}", message);

    }

    private record Token(String text) {

    }

    private class ChatMessage {
        private String text;
        private ChatMessageType type;

        public ChatMessage(String text, ChatMessageType type) {
            this.text = text;
            this.type = type;
        }

        public ChatMessageType type() {
            return type;
        }

        public String text() {
            return text;
        }

        public ChatMessage append(String text) {
            this.text = this.text + text;
            return this;
        }
    }

    private enum ChatMessageType {
        THINKING,
        RESPONSE
    }
}