package com.github.sc.apps.saisc.common.view;

import com.github.sc.apps.saisc.chat.view.ChatHistoryView;
import com.github.sc.apps.saisc.chat.view.ChatView;
import com.github.sc.apps.saisc.chatmodel.view.ChatModelListView;
import com.github.sc.apps.saisc.dump.view.DumpView;
import com.github.sc.apps.saisc.flyway.view.FlywayView;
import com.github.sc.apps.saisc.hobby.view.HobbyListView;
import com.github.sc.apps.saisc.person.view.PersonListView;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.sidenav.SideNav;
import com.vaadin.flow.component.sidenav.SideNavItem;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteParameters;

import java.util.UUID;

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
        var sideNavChat = new SideNav();
        sideNavChat.setLabel("LLM");
        sideNavChat.addItem(new SideNavItem("Chat", ChatView.class, new RouteParameters("chatId", UUID.randomUUID().toString()), VaadinIcon.CHAT.create()));
        sideNavChat.addItem(new SideNavItem("History", ChatHistoryView.class, VaadinIcon.ARCHIVE.create()));
        sideNavChat.addItem(new SideNavItem("Model", ChatModelListView.class, VaadinIcon.FILE_SEARCH.create()));

        var sideNavInspect = new SideNav();
        sideNavInspect.setLabel("Inspect");
        sideNavInspect.addItem(new SideNavItem("Hobbies", HobbyListView.class, VaadinIcon.USER_HEART.create()));
        sideNavInspect.addItem(new SideNavItem("Persons", PersonListView.class, VaadinIcon.USERS.create()));

        var sideNavAdmin = new SideNav();
        sideNavAdmin.setLabel("Admin");
        sideNavAdmin.addItem(new SideNavItem("Clean DB", FlywayView.class, VaadinIcon.ERASER.create()));
        sideNavAdmin.addItem(new SideNavItem("Dump DB", DumpView.class, VaadinIcon.DOWNLOAD.create()));

        addToDrawer(sideNavChat, sideNavInspect, sideNavAdmin);
    }

}
