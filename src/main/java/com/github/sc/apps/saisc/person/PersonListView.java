package com.github.sc.apps.saisc.person;

import com.github.sc.apps.saisc.common.frontend.BaseLayout;
import com.github.sc.apps.saisc.common.frontend.BasicListView;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.function.ValueProvider;
import com.vaadin.flow.router.Menu;
import com.vaadin.flow.router.Route;
import jakarta.persistence.*;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Menu(title = "Person")
@Route(value = "person", layout = BaseLayout.class)
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
        return Map.of(
                PersonET::getFirstName, "First Name",
                PersonET::getLastName, "Last Name",
                PersonET::getBirthdate, "Birthdate"
        );
    }
}
