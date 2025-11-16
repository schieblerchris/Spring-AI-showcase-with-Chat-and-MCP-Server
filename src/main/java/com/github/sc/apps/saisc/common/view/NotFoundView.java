package com.github.sc.apps.saisc.common.view;

import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@PageTitle("404 - Not Found")
@Route(value = "404", layout = BaseLayout.class)
public class NotFoundView extends VerticalLayout {

    public NotFoundView() {
        setSizeFull();
        setPadding(true);
        setSpacing(true);
        add(new H2("404 - Not Found"), new Paragraph("The requested resource could not be found."));
    }
}
