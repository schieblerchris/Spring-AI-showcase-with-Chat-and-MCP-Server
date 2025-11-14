package com.github.sc.apps.saisc.datetime;

import com.github.sc.apps.saisc.common.mcp.ToolMarkerInterface;
import lombok.extern.slf4j.Slf4j;
import org.springaicommunity.mcp.annotation.McpTool;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

@Component
@Slf4j
public class DateTimeTool implements ToolMarkerInterface {

    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm:ss");

    @McpTool(name = "get_current_date", description = "returns the current date in format yyyy-MM-dd")
    @Tool(name = "get_current_date", description = "returns the current date in format yyyy-MM-dd")
    public LocalDate getCurrentDate() {
        log.debug("get current date");
        var result = LocalDate.now();
        log.debug("found date: {}", result);
        return result;
    }

    @McpTool(name = "get_current_year", description = "returns the current year")
    @Tool(name = "get_current_year", description = "returns the current year")
    public int getCurrentYear() {
        log.debug("get current year");
        var result = LocalDate.now().getYear();
        log.debug("found year: {}", result);
        return result;
    }

    @McpTool(name = "get_current_time", description = "returns the current time")
    @Tool(name = "get_current_time", description = "returns the current time")
    public String getCurrentTime() {
        log.debug("get current time");
        var result = LocalTime.now().format(TIME_FORMATTER);
        log.debug("found time: {}", result);
        return result;
    }

    @McpTool(name = "get_current_weekday", description = "returns the current weekday")
    @Tool(name = "get_current_weekday", description = "returns the current weekday")
    public String getCurrentWeekday() {
        log.debug("get current weekday");
        var result = LocalDate.now().getDayOfWeek().name();
        log.debug("found weekday: {}", result);
        return result;
    }

}