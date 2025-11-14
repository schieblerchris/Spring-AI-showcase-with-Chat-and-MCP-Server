package com.github.sc.apps.saisc.dump;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Exposes a download endpoint for the current data dump as JSON.
 */
@RestController
@RequestMapping("/api")
public class DumpDownloadController {

    private final DumpService dumpService;
    private final ObjectMapper objectMapper;

    @Autowired
    public DumpDownloadController(DumpService dumpService, ObjectMapper objectMapper) {
        this.dumpService = dumpService;
        this.objectMapper = objectMapper;
    }

    @GetMapping(value = "/dump.json", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<byte[]> downloadDump() {
        try {
            var model = dumpService.dump();
            byte[] bytes = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsBytes(model);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.set(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"dump.json\"");
            headers.setCacheControl("no-store, no-cache, must-revalidate, max-age=0");

            return new ResponseEntity<>(bytes, headers, HttpStatus.OK);
        } catch (Exception e) {
            // Fallback to an empty JSON object on serialization error
            byte[] bytes = "{}".getBytes();
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.set(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"dump.json\"");
            headers.setCacheControl("no-store, no-cache, must-revalidate, max-age=0");
            return new ResponseEntity<>(bytes, headers, HttpStatus.OK);
        }
    }
}
