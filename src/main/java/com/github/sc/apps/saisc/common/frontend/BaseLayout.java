package com.github.sc.apps.saisc.common.frontend;

import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.router.*;
import com.vaadin.flow.server.menu.MenuConfiguration;
import com.vaadin.flow.server.menu.MenuEntry;

import java.util.List;

@Route
public class BaseLayout extends AppLayout {

    public BaseLayout() {
        createHeader();
        createDrawer();
    }

    private void createHeader() {
        H1 logo = new H1("SpringAI Showcase");
        logo.addClassNames("text-l", "m-m");
        HorizontalLayout header = new HorizontalLayout(new DrawerToggle(), logo);
        header.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.CENTER);
        header.setWidth("100%");
        header.addClassNames("py-0", "px-m");
        addToNavbar(header);
    }

    private void createDrawer() {
        List<MenuEntry> menuEntries = MenuConfiguration.getMenuEntries();
        menuEntries.forEach(entry -> addToDrawer(new Anchor(entry.path(), entry.title())));
//
//        var homeLink = new RouterLink("Home", HomeView.class);
//        homeLink.setHighlightCondition(HighlightConditions.sameLocation());
//
//        var chatModelLink = new RouterLink("LLM Model", ChatModelListView.class);
//        chatModelLink.setHighlightCondition(HighlightConditions.sameLocation());
//
//        var chatLink = new RouterLink("Chat", ChatView.class);
//        chatLink.setHighlightCondition(HighlightConditions.sameLocation());
//
//        var chat2Link = new RouterLink("Chat2", ChatView2.class, new RouteParameters(new RouteParam(ChatView2.QUERY_PARAM_ID, UUID.randomUUID().toString())));
//        chat2Link.setHighlightCondition(HighlightConditions.sameLocation());
//
//        var hobbyCategoryLink = new RouterLink("Hobby Categories", HobbyCategoryListView.class);
//        hobbyCategoryLink.setHighlightCondition(HighlightConditions.sameLocation());
//
//        var hobbyLink = new RouterLink("Hobbies", HobbyListView.class);
//        hobbyLink.setHighlightCondition(HighlightConditions.sameLocation());
//
//        var personLink = new RouterLink("Persons", PersonListView.class);
//        personLink.setHighlightCondition(HighlightConditions.sameLocation());
//
//        addToDrawer(new VerticalLayout(homeLink, chatModelLink, chatLink, chat2Link, hobbyCategoryLink, hobbyLink, personLink));
    }

}
