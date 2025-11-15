package com.github.sc.apps.saisc.chat;


import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.avatar.Avatar;
import com.vaadin.flow.component.card.Card;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.progressbar.ProgressBar;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ChatBubbleCard extends Card {

    public static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm:ss");

    private final ProgressBar progressBar = new ProgressBar();

    private final String title;

    public ChatBubbleCard(String title, String paragraphText) {
        this.title = title;
        initComponents();
        this.addBody(new Paragraph(paragraphText));
    }

    public ChatBubbleCard(String title) {
        this.title = title;
        initComponents();
    }

    private void initComponents() {
        progressBar.setVisible(false);
        this.setHeaderPrefix(new Avatar(title));
        this.setTitle(title);
        var time = LocalDateTime.now();
        var subtitle = time.format(DateTimeFormatter.ISO_DATE) + " " + time.format(TIME_FORMATTER);
        this.setSubtitle(new Paragraph(subtitle));
        this.setWidth(60.0f, Unit.PERCENTAGE);
        this.add(progressBar);
    }

    public void startProgress() {
        progressBar.setVisible(true);
        progressBar.setIndeterminate(true);
    }

    public void stopProgress() {
        progressBar.setIndeterminate(false);
        progressBar.setVisible(false);
    }

    public void addBody(Component component) {
        this.add(component);
    }

}
