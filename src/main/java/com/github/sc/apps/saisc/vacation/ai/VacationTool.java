package com.github.sc.apps.saisc.vacation.ai;


import com.github.sc.apps.saisc.common.mcp.ToolMarkerInterface;
import com.github.sc.apps.saisc.vacation.persistence.VacationRepository;
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
public class VacationTool implements ToolMarkerInterface {

    private final VacationRepository repository;

    @Autowired
    public VacationTool(VacationRepository repository) {
        this.repository = repository;
    }

    @McpTool(name = "is_person_on_vacation", description = "checks if a person is on vacation for the given date, true if person is on vacation - false if person is not on vacation")
    @Tool(name = "is_person_on_vacation", description = "checks if a person is on vacation for the given date, true if person is on vacation - false if person is not on vacation")
    public boolean isOnVacation(@McpToolParam(description = "The id of the person") @ToolParam(description = "The id of the person") Integer personId, @McpToolParam(description = "The date to test for vacation in the format yyyy-MM-dd eg. 2026-06-20") @ToolParam(description = "The date to test for vacation in the format yyyy-MM-dd eg. 2026-06-20") LocalDate date) {
        log.debug("is person on vacation for person {} on date {}", personId, date);
        var result = repository.findVacationByPersonAndDate(personId, date) != null;
        log.debug("person {} is on vacation: {}", personId, result);
        return result;
    }
    @McpTool(name = "list_available_persons", description = "returns a list of person ids that are available on the given date")
    @Tool(name = "list_available_persons", description = "returns a list of person ids that are available on the given date")
    public List<Integer> listAvailablePersons(@McpToolParam(description = "The date to test for vacation in the format yyyy-MM-dd eg. 2026-06-20") @ToolParam(description = "The date to test for vacation in the format yyyy-MM-dd eg. 2026-06-20") LocalDate date) {
        log.debug("list available persons for date {}", date);
        var result = repository.findAvailablePersons(date);
        log.debug("found {}", result);
        return result;
    }

}
