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

    private static final String SYSTEM_PROMPT = """
            You are a personal assistant to help finding the best suited person for a given activity.
            Always try to find the best fitting person by availability and hobby skill level.
            To reduce biases resolve the person data, like first name, last name, age, gender, etc. at the last possible moment.
            If you reference any person add a http link to their profile http://localhost:58080/persons/{personId}.
            Always include a table containing your tool calls with the input parameters in the final response.
            """;

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
                .defaultSystem(SYSTEM_PROMPT)
                .defaultAdvisors(chatMemoryAdvisor)
                .build();
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
