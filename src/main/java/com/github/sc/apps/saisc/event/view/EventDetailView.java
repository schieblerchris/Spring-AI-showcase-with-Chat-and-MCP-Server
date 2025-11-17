package com.github.sc.apps.saisc.event.view;

import com.github.sc.apps.saisc.common.view.BaseLayout;
import com.github.sc.apps.saisc.common.view.NotFoundView;
import com.github.sc.apps.saisc.event.persistence.EventET;
import com.github.sc.apps.saisc.event.persistence.EventRepository;
import com.github.sc.apps.saisc.person.persistence.PersonEventET;
import com.github.sc.apps.saisc.person.persistence.PersonEventRepository;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

@PageTitle("Event Details")
@Route(value = "events/:eventId", layout = BaseLayout.class)
public class EventDetailView extends VerticalLayout implements BeforeEnterObserver {

    private final EventRepository eventRepository;

    private final PersonEventRepository personEventRepository;
    private final VerticalLayout headerSection = new VerticalLayout();
    private final Grid<PersonEventET> personGrid = new Grid<>(PersonEventET.class, false);
    private Integer eventId;
    private EventET event;

    @Autowired
    public EventDetailView(EventRepository eventRepository, PersonEventRepository personEventRepository) {
        this.eventRepository = eventRepository;
        this.personEventRepository = personEventRepository;

        setSizeFull();
        setPadding(true);
        setSpacing(true);

        headerSection.setPadding(false);
        headerSection.setSpacing(false);
        add(headerSection);

        var bottom = new HorizontalLayout();
        bottom.setWidthFull();
        bottom.setSpacing(true);
        bottom.setDefaultVerticalComponentAlignment(Alignment.START);

        configureHobbyGrid();

        bottom.add(personGrid);
        bottom.setSizeFull();
        add(bottom);
    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        Optional<Integer> idParam = event.getRouteParameters().getInteger("eventId");
        if (idParam.isEmpty()) {
            event.rerouteTo(NotFoundView.class);
            return;
        }
        this.eventId = idParam.get();
        var eventOpt = eventRepository.findById(this.eventId);
        if (eventOpt.isEmpty()) {
            event.rerouteTo(NotFoundView.class);
            return;
        }
        this.event = eventOpt.get();

        populateHeader();
        loadPersons();
    }

    private void populateHeader() {
        headerSection.removeAll();
        var title = new H2(event.getTitle());
        var startDate = new Span("Start: " + event.getStartDate());
        var endDate = new Span("End: " + event.getEndDate());
        var skillLevel = new Span("Skill level: " + event.getSkillLevel());
        headerSection.add(title, startDate, endDate, skillLevel);
    }

    private void configureHobbyGrid() {
        personGrid.addColumn(PersonEventET::getPerson).setHeader("Person").setAutoWidth(true).setFlexGrow(0);
        personGrid.setWidthFull();
        personGrid.setHeight("400px");
    }

    private void loadPersons() {
        personGrid.setItems(personEventRepository.findAllByEvent(eventId));
    }

}
