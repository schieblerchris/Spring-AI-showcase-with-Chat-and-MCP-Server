package com.github.sc.apps.saisc.shared.web;

import com.github.sc.apps.saisc.chat.view.ChatView;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.*;

import java.util.Map;
import java.util.UUID;


@PageTitle("Index")
@Route(value = "", layout = BaseLayout.class)
public class IndexView extends VerticalLayout implements BeforeEnterObserver {

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        var newId = UUID.randomUUID().toString();
        event.rerouteTo(ChatView.class, new RouteParameters(Map.of("chatId", newId)));
    }
}
