package com.github.sc.apps.saisc.event.ai;

import com.github.sc.apps.saisc.common.mcp.ToolMarkerInterface;
import com.github.sc.apps.saisc.event.persistence.EventRepository;
import com.github.sc.apps.saisc.person.persistence.SkillLevel;
import lombok.extern.slf4j.Slf4j;
import org.springaicommunity.mcp.annotation.McpTool;
import org.springaicommunity.mcp.annotation.McpToolParam;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
@Slf4j
public class EventTool implements ToolMarkerInterface {

    private final EventRepository repository;

    @Autowired
    public EventTool(EventRepository repository) {
        this.repository = repository;
    }

    @McpTool(name = "get_events_by_date", description = "Get all events surrunding the date")
    @Tool(name = "get_events_by_date", description = "Get all events surrunding the date")
    private List<EventToolResponse> getEventsByDate(
            @McpToolParam(description = "The date to test for events")
            @ToolParam(description = "The date to test for events") LocalDate date
    ) {
        log.debug("get events by date {}", date);
        var result = repository.findAllByDate(date).stream().map(e -> new EventToolResponse(
                e.getId(),
                e.getTitle(),
                e.getStartDate(),
                e.getEndDate(),
                e.getHobbyId(),
                e.getSkillLevel()

        )).toList();
        log.debug("found: {}", result);
        return result;
    }

    public record EventToolResponse(int eventId, String title, LocalDate startDate, LocalDate endDate, Integer hobbyId,
                                    SkillLevel skillLevel) {
    }

}
