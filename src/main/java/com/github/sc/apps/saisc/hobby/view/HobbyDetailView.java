package com.github.sc.apps.saisc.hobby.view;

import com.github.sc.apps.saisc.hobby.model.HobbyET;
import com.github.sc.apps.saisc.hobby.persistence.HobbyRepository;
import com.github.sc.apps.saisc.person.model.SkillLevel;
import com.github.sc.apps.saisc.person.persistence.PersonHobbyRepository;
import com.github.sc.apps.saisc.person.view.PersonDetailView;
import com.github.sc.apps.saisc.shared.web.BaseLayout;
import com.github.sc.apps.saisc.shared.web.NotFoundView;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;

@PageTitle("Hobby")
@Route(value = "hobbies/:hobbyId", layout = BaseLayout.class)
public class HobbyDetailView extends VerticalLayout implements BeforeEnterObserver {

    private final HobbyRepository hobbyRepository;
    private final PersonHobbyRepository personHobbyRepository;
    private final VerticalLayout headerSection = new VerticalLayout();
    private final Grid<HobbyDetailView.PersonHobbyRow> hobbyGrid = new Grid<>(HobbyDetailView.PersonHobbyRow.class, false);
    private final List<PersonHobbyRow> personHobbyRows = new ArrayList<>();
    private Integer hobbyId;
    private HobbyET hobby;

    @Autowired
    public HobbyDetailView(HobbyRepository hobbyRepository, PersonHobbyRepository personHobbyRepository) {
        this.hobbyRepository = hobbyRepository;
        this.personHobbyRepository = personHobbyRepository;

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

        bottom.add(hobbyGrid);
        bottom.setSizeFull();
        add(bottom);
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
        var personHobbies = new HashMap<SkillLevel, List<Integer>>();
        Arrays.stream(SkillLevel.values()).forEach(sl -> personHobbies.put(sl, personHobbyRepository.findByHobbyAndSkillLevel(hobbyId, sl)));
        var maxEntryCount = personHobbies.values().stream().mapToInt(List::size).max().orElse(0);
        var none = personHobbies.get(SkillLevel.NONE);
        var beginner = personHobbies.get(SkillLevel.BEGINNER);
        var intermediate = personHobbies.get(SkillLevel.INTERMEDIATE);
        var advanced = personHobbies.get(SkillLevel.ADVANCED);
        var expert = personHobbies.get(SkillLevel.EXPERT);
        for (int i = 0; i < maxEntryCount; i++) {
            personHobbyRows.add(new PersonHobbyRow(
                    getOrNull(i, none),
                    getOrNull(i, beginner),
                    getOrNull(i, intermediate),
                    getOrNull(i, advanced),
                    getOrNull(i, expert)
            ));
        }
        hobbyGrid.setItems(personHobbyRows);
        populateHeader();
    }

    private void populateHeader() {
        headerSection.removeAll();
        var title = new H2(hobby.getName());
        headerSection.add(title);
    }

    private void configureHobbyGrid() {
        hobbyGrid.addComponentColumn(phr -> createLinkComponentOrNull(phr.nonePersonId)).setHeader("None").setAutoWidth(true).setFlexGrow(0);
        hobbyGrid.addComponentColumn(phr -> createLinkComponentOrNull(phr.beginnerPersonId)).setHeader("Beginner").setAutoWidth(true).setFlexGrow(0);
        hobbyGrid.addComponentColumn(phr -> createLinkComponentOrNull(phr.intermediatePersonId)).setHeader("Intermediate").setAutoWidth(true).setFlexGrow(0);
        hobbyGrid.addComponentColumn(phr -> createLinkComponentOrNull(phr.advancedPersonId)).setHeader("Advanced").setAutoWidth(true).setFlexGrow(0);
        hobbyGrid.addComponentColumn(phr -> createLinkComponentOrNull(phr.expertPersonId)).setHeader("Expert").setAutoWidth(true).setFlexGrow(0);
        hobbyGrid.setSizeFull();
    }

    private Component createLinkComponentOrNull(Integer personId) {
        if (personId == null) {
            return new Text("");
        } else {
            return new RouterLink(
                    String.valueOf(personId),
                    PersonDetailView.class,
                    new RouteParameters("personId", String.valueOf(personId))
            );
        }
    }

    private Integer getOrNull(int index, List<Integer> values) {
        if (values.size() > index) {
            return values.get(index);
        } else {
            return null;
        }
    }

    private record PersonHobbyRow(Integer nonePersonId, Integer beginnerPersonId, Integer intermediatePersonId,
                                  Integer advancedPersonId, Integer expertPersonId) {
    }

}
