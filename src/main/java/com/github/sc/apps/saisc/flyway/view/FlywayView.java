package com.github.sc.apps.saisc.flyway.view;

import com.github.sc.apps.saisc.common.view.BaseLayout;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.confirmdialog.ConfirmDialog;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.progressbar.ProgressBar;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import lombok.extern.slf4j.Slf4j;
import org.flywaydb.core.Flyway;
import org.springframework.beans.factory.annotation.Autowired;

@Slf4j
@PageTitle("Database cleanup")
@Route(value = "db-clean", layout = BaseLayout.class)
public class FlywayView extends VerticalLayout {

    private final Flyway flyway;

    private final ProgressBar progressBar = new ProgressBar();

    @Autowired
    public FlywayView(Flyway flyway) {
        this.flyway = flyway;
        this.add(progressBar);
        setPadding(true);
        setSpacing(true);

        Button cleanBtn = new Button("Clean database", event -> openConfirmDialog());
        cleanBtn.addThemeVariants(ButtonVariant.LUMO_ERROR, ButtonVariant.LUMO_PRIMARY);
        add(cleanBtn);
        setHorizontalComponentAlignment(FlexComponent.Alignment.CENTER, cleanBtn);
    }

    private void openConfirmDialog() {
        ConfirmDialog dialog = new ConfirmDialog();
        dialog.setHeader("Confirm database clean");
        dialog.setText("This will completely reset the database. All data will be permanently deleted. Do you want to proceed?");
        dialog.setCancelable(true);
        dialog.setCancelText("Cancel");
        dialog.setConfirmText("Proceed");
        dialog.addConfirmListener(e -> cleanDatabase());
        dialog.open();
    }

    private void cleanDatabase() {
        progressBar.setIndeterminate(true);
        try {
            log.warn("User confirmed Flyway clean — cleaning database now");
            flyway.clean();
            flyway.migrate();
            Notification.show("Database cleaned.");
        } catch (Exception ex) {
            log.error("Failed to clean database via Flyway", ex);
            Notification.show("Failed to clean database: " + ex.getMessage());
        }
        progressBar.setIndeterminate(false);
    }
}
