package com.github.sc.apps.saisc.event.view;

import com.github.sc.apps.saisc.event.model.EventET;
import com.github.sc.apps.saisc.event.persistence.EventRepository;
import com.github.sc.apps.saisc.shared.web.BaseLayout;
import com.github.sc.apps.saisc.shared.web.BasicListView;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.function.ValueProvider;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteParameters;
import com.vaadin.flow.router.RouterLink;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@PageTitle("Events")
@Route(value = "events", layout = BaseLayout.class)
public class EventListView extends BasicListView<EventRepository, EventET, Integer> {

    @Autowired
    public EventListView(EventRepository repository) {
        super(repository);
    }

    @Override
    protected String[] getSortFields() {
        return new String[]{"startDate", "endDate", "title"};
    }

    @Override
    protected List<TextField> getFilters() {
        return List.of();
    }

    @Override
    protected Map<ValueProvider<EventET, ?>, String> getColumns() {
        var columns = new LinkedHashMap<ValueProvider<EventET, ?>, String>();
        columns.put(EventET::getTitle, "Title");
        columns.put(EventET::getStartDate, "Start");
        columns.put(EventET::getEndDate, "End");
        columns.put(EventET::getHobbyId, "Hobby");
        columns.put(EventET::getSkillLevel, "Skill level");
        return columns;
    }

    @Override
    protected void customizeGrid(Grid<EventET> grid) {
        grid.addComponentColumn(event ->
                        new RouterLink("Details", EventDetailView.class,
                                new RouteParameters("eventId", String.valueOf(event.getId()))))
                .setHeader("Detail")
                .setAutoWidth(true)
                .setFlexGrow(0);
    }
}
