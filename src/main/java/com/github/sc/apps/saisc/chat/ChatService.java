package com.github.sc.apps.saisc.chat;

import com.github.sc.apps.saisc.common.mcp.ToolMarkerInterface;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.ChatClientResponse;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.util.List;

@Service
public class ChatService {

    private final ChatClient chatClient;
    private final Object[] tools;

    @Autowired
    public ChatService(ChatClient.Builder chatClientBuilder,
                       ChatMemory chatMemory, List<ToolMarkerInterface> tools) {
        this.tools = tools.toArray(new ToolMarkerInterface[0]);
        var chatMemoryAdvisor = MessageChatMemoryAdvisor
                .builder(chatMemory)
                .build();
        chatClient = chatClientBuilder
                .defaultSystem("Your goal is to help finding persons. Always try to find the best fitting person by availability and hobby skill level. Resolve the person at the last possible moment from id to the real deal.")
                .defaultAdvisors(chatMemoryAdvisor)
                .build();
    }

    public Flux<String> chatStream(String userInput, String chatId) {
        return chatClient.prompt()
                .advisors(advisorSpec ->
                        advisorSpec.param(ChatMemory.CONVERSATION_ID, chatId)
                )
                .user(userInput)
                .tools(tools)
                .stream()
                .content();
    }

    public Flux<ChatClientResponse> chatStreamDetailed(String userInput, String chatId) {
        return chatClient.prompt()
                .advisors(advisorSpec ->
                        advisorSpec.param(ChatMemory.CONVERSATION_ID, chatId)
                )
                .user(userInput)
                .tools(tools)
                .stream()
                .chatClientResponse();
    }
}
