package com.github.sc.apps.saisc.person.view;

import com.github.sc.apps.saisc.common.view.BaseLayout;
import com.github.sc.apps.saisc.common.view.NotFoundView;
import com.github.sc.apps.saisc.event.persistence.EventRepository;
import com.github.sc.apps.saisc.hobby.persistence.HobbyET;
import com.github.sc.apps.saisc.hobby.persistence.HobbyRepository;
import com.github.sc.apps.saisc.person.persistence.*;
import com.github.sc.apps.saisc.vacation.persistence.VacationRepository;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@PageTitle("Person Details")
@Route(value = "persons/:personId", layout = BaseLayout.class)
public class PersonDetailView extends VerticalLayout implements BeforeEnterObserver {

    private final PersonRepository personRepository;
    private final PersonHobbyRepository personHobbyRepository;
    private final HobbyRepository hobbyRepository;
    private final EventRepository eventRepository;
    private final VacationRepository vacationRepository;
    private final VerticalLayout headerSection = new VerticalLayout();
    private final Grid<PersonHobbyRow> hobbyGrid = new Grid<>(PersonHobbyRow.class, false);
    private final Grid<CalendarRow> calendarGrid = new Grid<>(CalendarRow.class, false);
    private Integer personId;
    private PersonET person;

    @Autowired
    public PersonDetailView(PersonRepository personRepository,
                            PersonHobbyRepository personHobbyRepository,
                            HobbyRepository hobbyRepository,
                            EventRepository eventRepository,
                            VacationRepository vacationRepository) {
        this.personRepository = personRepository;
        this.personHobbyRepository = personHobbyRepository;
        this.hobbyRepository = hobbyRepository;
        this.eventRepository = eventRepository;
        this.vacationRepository = vacationRepository;

        setSizeFull();
        setPadding(true);
        setSpacing(true);

        headerSection.setPadding(false);
        headerSection.setSpacing(false);
        add(headerSection);

        var bottom = new HorizontalLayout();
        bottom.setWidthFull();
        bottom.setSpacing(true);
        bottom.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.START);

        configureHobbyGrid();
        configureCalendarGrid();

        bottom.add(hobbyGrid, calendarGrid);
        bottom.setFlexGrow(1, hobbyGrid, calendarGrid);
        add(bottom);
    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        Optional<Integer> idParam = event.getRouteParameters().getInteger("personId");
        if (idParam.isEmpty()) {
            event.rerouteTo(NotFoundView.class);
            return;
        }
        this.personId = idParam.get();
        var personOpt = personRepository.findById(this.personId);
        if (personOpt.isEmpty()) {
            event.rerouteTo(NotFoundView.class);
            return;
        }
        this.person = personOpt.get();

        populateHeader();
        loadHobbies();
        loadCalendar();
    }

    private void populateHeader() {
        headerSection.removeAll();
        var title = new H2(person.getFirstName() + " " + person.getLastName());
        var birthdate = new Span("Birthdate: " + person.getBirthdate());
        headerSection.add(title, birthdate);
    }

    private void configureHobbyGrid() {
        hobbyGrid.addColumn(PersonHobbyRow::hobby).setHeader("Hobby").setAutoWidth(true).setFlexGrow(0);
        hobbyGrid.addColumn(row -> row.skillLevel().name()).setHeader("Skill Level").setAutoWidth(true).setFlexGrow(0);
        hobbyGrid.setWidthFull();
        hobbyGrid.setHeight("400px");
    }

    private void configureCalendarGrid() {
        var df = DateTimeFormatter.ISO_DATE;
        calendarGrid.addColumn(CalendarRow::type).setHeader("Type").setAutoWidth(true).setFlexGrow(0);
        calendarGrid.addColumn(CalendarRow::title).setHeader("Event/Availability").setAutoWidth(true);
        calendarGrid.addColumn(r -> df.format(r.startDate())).setHeader("Start").setAutoWidth(true).setFlexGrow(0);
        calendarGrid.addColumn(r -> df.format(r.endDate())).setHeader("End").setAutoWidth(true).setFlexGrow(0);
        calendarGrid.setWidthFull();
        calendarGrid.setHeight("400px");
    }

    private void loadHobbies() {
        List<PersonHobbyET> personHobbies = personHobbyRepository.findByPerson(personId);
        var hobbyIds = personHobbies.stream().map(PersonHobbyET::getHobby).distinct().toList();
        Map<Integer, String> hobbyNames = hobbyRepository.findAllById(hobbyIds)
                .stream().collect(Collectors.toMap(HobbyET::getId, HobbyET::getName));
        var rows = personHobbies.stream()
                .map(ph -> new PersonHobbyRow(
                        hobbyNames.getOrDefault(ph.getHobby(), "#" + ph.getHobby()),
                        ph.getSkillLevel()
                ))
                .toList();
        hobbyGrid.setItems(rows);
    }

    private void loadCalendar() {
        List<CalendarRow> eventRows = eventRepository.findAllByPerson(personId)
                .stream()
                .map(e -> new CalendarRow("Event", e.getTitle(), e.getStartDate(), e.getEndDate()))
                .toList();

        List<CalendarRow> vacationRows = vacationRepository.findAllByPersonId(personId)
                .stream()
                .map(v -> new CalendarRow("Vacation", v.getComment() == null || v.getComment().isBlank() ? "Vacation" : v.getComment(), v.getStartDate(), v.getEndDate()))
                .toList();

        var allRows = new java.util.ArrayList<CalendarRow>(eventRows.size() + vacationRows.size());
        allRows.addAll(eventRows);
        allRows.addAll(vacationRows);
        allRows.sort(java.util.Comparator.comparing(CalendarRow::startDate).thenComparing(CalendarRow::endDate));

        calendarGrid.setItems(allRows);
    }

    public record PersonHobbyRow(String hobby, SkillLevel skillLevel) {
    }

    public record CalendarRow(String type, String title, java.time.LocalDate startDate, java.time.LocalDate endDate) {
    }
}
