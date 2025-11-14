package com.github.sc.apps.saisc.chat;


import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

@Component
@Slf4j
public class ChatResponseParser {

    private static final Pattern THINK_PATTERN = Pattern.compile("<think>(.*?)</think>", Pattern.DOTALL);

    public ChatResponse parse(String response) {
        var thoughts = new ArrayList<String>();
        var matcher = THINK_PATTERN.matcher(response);

        while (matcher.find()) {
            String thinkContent = matcher.group(1).trim();
            thoughts.add(thinkContent);
        }
        String message = THINK_PATTERN.matcher(response).replaceAll("").trim();

        log.info("Found {} thoughts", thoughts.size());
        return new ChatResponse(thoughts, message);
    }

    public record ChatResponse(
            List<String> thoughts,
            String message
    ) {

    }



}
