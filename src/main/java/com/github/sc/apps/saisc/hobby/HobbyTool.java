package com.github.sc.apps.saisc.hobby;

import com.github.sc.apps.saisc.common.mcp.ToolMarkerInterface;
import com.github.sc.apps.saisc.person.PersonHobbyET;
import lombok.extern.slf4j.Slf4j;
import org.springaicommunity.mcp.annotation.McpTool;
import org.springaicommunity.mcp.annotation.McpToolParam;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class HobbyTool implements ToolMarkerInterface {

    private final HobbyRepository repository;

    @Autowired
    public HobbyTool(HobbyRepository repository) {
        this.repository = repository;
    }

    @McpTool(name = "get_hobby_by_id", description = "get a specific hobby by id")
    @Tool(name = "get_hobby_by_id", description = "get a specific hobby by id")
    public HobbyToolResponse getHobbyById(
            @McpToolParam(description = "The id of the hobby")
            @ToolParam(description = "The id of the hobby") Integer id) {
        log.debug("get hobby by id {}", id);
        var result = repository.findById(id).map(h -> new HobbyToolResponse(h.getId(), h.getName())).orElse(null);
        log.debug("found: {}", result);
        return result;
    }

    @McpTool(name = "get_hobby_by_name", description = "get a specific hobby by name")
    @Tool(name = "get_hobby_by_name", description = "get a specific hobby by name")
    public HobbyToolResponse getHobbyByName(
            @McpToolParam(description = "The name of the hobby")
            @ToolParam(description = "The name of the hobby") String name) {
        log.debug("get hobby by name {}", name);
        var result = repository.findByNameIgnoreCase(name).map(h -> new HobbyToolResponse(h.getId(), h.getName())).orElse(null);
        log.debug("found: {}", result);
        return result;
    }

    @McpTool(name = "compare_skill_level", description = "compares two skill levels")
    @Tool(name = "compare_skill_level", description = "compares two skill levels")
    public String compareSkillLevel(
            @McpToolParam(description = "the first skill level")
            @ToolParam(description = "the first skill level") PersonHobbyET.SkillLevel skillLevel1,
            @McpToolParam(description = "the second skill level")
            @ToolParam(description = "the second skill level") PersonHobbyET.SkillLevel skillLevel2
    ) {
        log.debug("compare skill level {} with {}", skillLevel1, skillLevel2);
        var result = "equal";
        if (skillLevel1.ordinal() > skillLevel2.ordinal()) {
            result = skillLevel1.name() + " is higher than " + skillLevel2.name();
        } else if (skillLevel1.ordinal() < skillLevel2.ordinal()) {
            result = skillLevel2.name() + " is higher than " + skillLevel1.name();
        }
        log.debug("result: {}", result);
        return result;
    }

    public record HobbyToolResponse(
            int hobbyId,
            String name
    ) {
    }
}
