package com.github.sc.apps.saisc.shared.web;

import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.sidenav.SideNav;
import com.vaadin.flow.component.sidenav.SideNavItem;
import com.vaadin.flow.router.Route;

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
        sideNavChat.addItem(navItem("Chat", "/" + AppRoute.CHAT.path() + "/" + UUID.randomUUID(), VaadinIcon.CHAT));
        sideNavChat.addItem(navItem("History", "/" + AppRoute.CHAT_HISTORY.path(), VaadinIcon.ARCHIVE));
        sideNavChat.addItem(navItem("Model", "/" + AppRoute.CHAT_MODEL.path(), VaadinIcon.FILE_SEARCH));

        var sideNavInspect = new SideNav();
        sideNavInspect.setLabel("Inspect");
        sideNavInspect.addItem(navItem("Events", "/" + AppRoute.EVENTS.path(), VaadinIcon.CROSSHAIRS));
        sideNavInspect.addItem(navItem("Hobbies", "/" + AppRoute.HOBBIES.path(), VaadinIcon.USER_HEART));
        sideNavInspect.addItem(navItem("Persons", "/" + AppRoute.PERSONS.path(), VaadinIcon.USERS));

        var sideNavAdmin = new SideNav();
        sideNavAdmin.setLabel("Admin");
        sideNavAdmin.addItem(navItem("Clean DB", "/" + AppRoute.DB_CLEAN.path(), VaadinIcon.ERASER));
        sideNavAdmin.addItem(navItem("Dump DB", "/" + AppRoute.DUMP.path(), VaadinIcon.DOWNLOAD));

        addToDrawer(sideNavChat, sideNavInspect, sideNavAdmin);
    }

    private SideNavItem navItem(String label, String path, VaadinIcon icon) {
        var item = new SideNavItem(label);
        item.setPrefixComponent(icon.create());
        item.setPath(path);
        return item;
    }

}
