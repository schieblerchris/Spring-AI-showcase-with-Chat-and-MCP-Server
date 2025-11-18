package com.github.sc.apps.saisc.shared.mcp;

import org.springframework.ai.tool.annotation.Tool;

import java.util.Arrays;
import java.util.List;

public interface ToolMarkerInterface {

    String name();

    default List<String> functions() {
        return Arrays.stream(this.getClass().getMethods())
                .filter(m -> m.isAnnotationPresent(Tool.class))
                .map(m -> m.getAnnotation(Tool.class).name())
                .sorted()
                .toList();
    }
}
