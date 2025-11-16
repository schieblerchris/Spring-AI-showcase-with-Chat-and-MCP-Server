package com.github.sc.apps.saisc.person.view;

import com.github.sc.apps.saisc.common.view.BaseLayout;
import com.github.sc.apps.saisc.common.view.BasicListView;
import com.github.sc.apps.saisc.person.persistence.PersonET;
import com.github.sc.apps.saisc.person.persistence.PersonRepository;
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

@PageTitle("Persons")
@Route(value = "persons", layout = BaseLayout.class)
public class PersonListView extends BasicListView<PersonRepository, PersonET, Integer> {

    @Autowired
    public PersonListView(PersonRepository repository) {
        super(repository);
    }

    @Override
    protected String[] getSortFields() {
        return new String[]{"lastName", "firstName", "birthdate"};
    }

    @Override
    protected List<TextField> getFilters() {
        return List.of();
    }

    @Override
    protected Map<ValueProvider<PersonET, ?>, String> getColumns() {
        var columns = new LinkedHashMap<ValueProvider<PersonET, ?>, String>();
        columns.put(PersonET::getFirstName, "First Name");
        columns.put(PersonET::getLastName, "Last Name");
        columns.put(PersonET::getBirthdate, "Birthdate");
        return columns;
    }

    @Override
    protected void customizeGrid(Grid<PersonET> grid) {
        grid.addComponentColumn(person ->
                        new RouterLink("Details", PersonDetailView.class,
                                new RouteParameters("personId", String.valueOf(person.getId()))))
                .setHeader("Detail")
                .setAutoWidth(true)
                .setFlexGrow(0);
    }
}
