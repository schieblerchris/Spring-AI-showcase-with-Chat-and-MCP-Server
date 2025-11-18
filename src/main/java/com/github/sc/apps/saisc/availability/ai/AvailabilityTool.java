package com.github.sc.apps.saisc.availability.ai;


import com.github.sc.apps.saisc.availability.service.AvailabilityService;
import com.github.sc.apps.saisc.shared.mcp.ToolMarkerInterface;
import lombok.extern.slf4j.Slf4j;
import org.springaicommunity.mcp.annotation.McpTool;
import org.springaicommunity.mcp.annotation.McpToolParam;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.Set;

@Component
@Slf4j
public class AvailabilityTool implements ToolMarkerInterface {

    private final AvailabilityService service;

    @Autowired
    public AvailabilityTool(AvailabilityService service) {
        this.service = service;
    }

    @McpTool(name = "is_person_available", description = "checks if a person is available for the given date, true if person is available - false if person is unavailable")
    @Tool(name = "is_person_available", description = "checks if a person is available for the given date, true if person is available - false if person is unavailable")
    public boolean isAvailable(
            @McpToolParam(description = "The id of the person")
            @ToolParam(description = "The id of the person") Integer personId,
            @McpToolParam(description = "The date to test for availability")
            @ToolParam(description = "The date to test for availability") LocalDate date) {
        log.debug("is person available for person {} on date {}", personId, date);
        var result = service.isPersonAvailable(personId, date);
        log.debug("person {} is available: {}", personId, result);
        return result;
    }

    @McpTool(name = "list_available_persons", description = "returns a list of person ids that are available on the given date")
    @Tool(name = "list_available_persons", description = "returns a list of person ids that are available on the given date")
    public Set<Integer> listAvailablePersons(@McpToolParam(description = "The date to test for availability") @ToolParam(description = "The date to test for availability") LocalDate date) {
        log.debug("list available persons for date {}", date);
        var result = service.findAvailablePersons(date);
        log.debug("found {}", result);
        return result;
    }

}
