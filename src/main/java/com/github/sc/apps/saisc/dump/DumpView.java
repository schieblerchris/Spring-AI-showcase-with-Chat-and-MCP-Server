package com.github.sc.apps.saisc.dump;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.sc.apps.saisc.common.frontend.BaseLayout;
import com.helger.commons.io.stream.StringInputStream;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.streams.DownloadHandler;
import com.vaadin.flow.server.streams.DownloadResponse;
import org.springframework.beans.factory.annotation.Autowired;

import java.nio.charset.Charset;

@PageTitle("Data dump")
@Route(value = "dump", layout = BaseLayout.class)
public class DumpView extends VerticalLayout {

    private final DumpService dumpService;
    private final ObjectMapper objectMapper;

    @Autowired
    public DumpView(DumpService dumpService, ObjectMapper objectMapper) {
        this.dumpService = dumpService;
        this.objectMapper = objectMapper;
        createContent();
    }

    private void createContent() {
        setSizeFull();
        setDefaultHorizontalComponentAlignment(Alignment.CENTER);
        setJustifyContentMode(JustifyContentMode.CENTER);
        var downloadAttachment = new Anchor(
                DownloadHandler.fromInputStream((event) -> {
                    try {
                        var attachment = dumpService.dump();
                        var data = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(attachment);
                        return new DownloadResponse(new StringInputStream(data, Charset.defaultCharset()),
                                "dump.json", "application/json", data.length());
                    } catch (Exception e) {
                        return DownloadResponse.error(500);
                    }
                }), "Download attachment");
        add(downloadAttachment);
    }
}
