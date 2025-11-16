package com.github.sc.apps.saisc.hobby;

import com.github.sc.apps.saisc.common.frontend.BaseLayout;
import com.github.sc.apps.saisc.common.frontend.NotFoundView;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

@PageTitle("Hobby")
@Route(value = "hobby/:hobbyId", layout = BaseLayout.class)
public class HobbyDetailView extends VerticalLayout implements BeforeEnterObserver {

    private final HobbyRepository hobbyRepository;
    private Integer hobbyId;
    private HobbyET hobby;

    @Autowired
    public HobbyDetailView(HobbyRepository hobbyRepository) {
        this.hobbyRepository = hobbyRepository;
    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        Optional<Integer> idParam = event.getRouteParameters().getInteger("hobbyId");
        if (idParam.isEmpty()) {
            event.rerouteTo(NotFoundView.class);
            return;
        }
        this.hobbyId = idParam.get();
        var hobbyOpt = hobbyRepository.findById(this.hobbyId);
        if (hobbyOpt.isEmpty()) {
            event.rerouteTo(NotFoundView.class);
            return;
        }
        this.hobby = hobbyOpt.get();
    }

}
