package com.github.sc.apps.saisc.chat;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.orderedlayout.FlexLayout;

import java.util.List;
import java.util.function.Consumer;

public class StarterSuggestionsComponent extends FlexLayout {

    private final List<String> suggestions = List.of(
            "What is the current time?",
            "List all hobbies",
            "List all hobby categories"
    );

    private final Consumer<String> handleSelection;
    private boolean hasSentMessage = false;

    public StarterSuggestionsComponent(Consumer<String> handleSelection) {
        this.handleSelection = handleSelection;
        initComponents();
    }

    private void initComponents() {
        this.setWidthFull();
        this.getStyle().set("gap", "var(--lumo-space-s)");
        this.getStyle().set("padding", "var(--lumo-space-s)");
        this.getStyle().set("align-items", "center");
        this.setFlexWrap(FlexLayout.FlexWrap.WRAP);
        this.setVisible(!hasSentMessage);

        // Small description label in front of the items
        var label = new com.vaadin.flow.component.html.Span("Suggestions:");
        label.getStyle().set("font-size", "var(--lumo-font-size-s)");
        label.getStyle().set("color", "var(--lumo-secondary-text-color)");
        label.getStyle().set("margin-right", "var(--lumo-space-s)");
        this.add(label);

        suggestions.forEach(s -> {
            var btn = new Button(s);
            btn.addThemeVariants(ButtonVariant.LUMO_TERTIARY, ButtonVariant.LUMO_SMALL);
            btn.getStyle().set("border-radius", "9999px");
            btn.getStyle().set("background-color", "var(--lumo-contrast-10pct)");
            btn.getStyle().set("padding", "var(--lumo-space-xs) var(--lumo-space-s)");
            btn.addClickListener(e -> {
                setHasSentMessage();
                handleSelection.accept(s);
            });
            this.add(btn);
        });
    }

    public void setHasSentMessage() {
        this.hasSentMessage = true;
        this.setVisible(false);
    }
}
