package com.github.sc.apps.saisc.event.ai;

import com.github.sc.apps.saisc.event.model.EventET;
import com.github.sc.apps.saisc.event.persistence.EventRepository;
import com.github.sc.apps.saisc.person.model.SkillLevel;
import com.github.sc.apps.saisc.shared.mcp.ToolMarkerInterface;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springaicommunity.mcp.annotation.McpTool;
import org.springaicommunity.mcp.annotation.McpToolParam;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;
import java.util.function.Function;

@Component
@Slf4j
public class EventTool implements ToolMarkerInterface {

    private final EventRepository repository;

    @Autowired
    public EventTool(EventRepository repository) {
        this.repository = repository;
    }

    @McpTool(name = "get_events_by_date", description = "Get all events surrounding the date")
    @Tool(name = "get_events_by_date", description = "Get all events surrounding the date")
    private List<EventToolResponse> getEventsByDate(
            @McpToolParam(description = "The date to test for events")
            @ToolParam(description = "The date to test for events") LocalDate date
    ) {
        log.debug("get events by date {}", date);
        var result = repository.findAllByDate(date).stream().map(mapEntity()).toList();
        log.debug("found: {}", result);
        return result;
    }

    @McpTool(name = "get_events_by_title", description = "Get all events by title")
    @Tool(name = "get_events_by_title", description = "Get all events by title")
    private List<EventToolResponse> getEventsByTitle(
            @McpToolParam(description = "The title of the event")
            @ToolParam(description = "The title of the event") String title
    ) {
        log.debug("get events by title {}", title);
        var result = repository.findByTitleOrderByStartDateAscTitleAsc(title).stream().map(mapEntity()).toList();
        log.debug("found: {}", result);
        return result;
    }

    private static @NotNull Function<EventET, EventToolResponse> mapEntity() {
        return e -> new EventToolResponse(
                e.getId(),
                e.getTitle(),
                e.getStartDate(),
                e.getEndDate(),
                e.getHobbyId(),
                e.getSkillLevel()

        );
    }

    @Override
    public String name() {
        return "Event";
    }

    public record EventToolResponse(int eventId, String title, LocalDate startDate, LocalDate endDate, Integer hobbyId,
                                    SkillLevel skillLevel) {
    }

}
