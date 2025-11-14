package com.github.sc.apps.saisc.common.frontend;

import com.vaadin.flow.component.page.AppShellConfigurator;
import com.vaadin.flow.component.page.Push;
import com.vaadin.flow.shared.communication.PushMode;
import com.vaadin.flow.theme.Theme;
import com.vaadin.flow.theme.lumo.Lumo;

@Theme(variant = Lumo.DARK)
@Push
public class Index implements AppShellConfigurator {
}
