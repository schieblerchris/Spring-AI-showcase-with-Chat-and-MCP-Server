package com.github.sc.apps.saisc.hobby;

import com.github.sc.apps.saisc.common.frontend.BaseLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.provider.ListDataProvider;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.Menu;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;

import java.util.Collection;

@Menu(title = "Hobbies")
@Route(value = "hobby", layout = BaseLayout.class)
public class HobbyListView extends VerticalLayout {

    private final HobbyRepository repository;

    @Autowired
    public HobbyListView(HobbyRepository repository) {
        this.repository = repository;
        createContent();
    }

    private void createContent() {
        var dataProvider = new HobbyListProvider(repository.findAll(Sort.by("name").ascending()));

        var nameFilter = new TextField("Name");
        nameFilter.setPlaceholder("Fitness");
        nameFilter.setClearButtonVisible(true);
        nameFilter.setValueChangeMode(ValueChangeMode.EAGER);

        Runnable applyFilters = () -> {
            dataProvider.setFilter(hobby -> hobby.getName().contains(nameFilter.getValue()));
        };
        nameFilter.addValueChangeListener(e -> applyFilters.run());

        var grid = new Grid<>(HobbyET.class, false);
        grid.setDataProvider(dataProvider);
        grid.addColumn(HobbyET::getName).setHeader("Name");
        grid.setSizeFull();

        add(nameFilter, grid);
        setSizeFull();
        setPadding(false);
        setSpacing(false);
        setMargin(false);
        expand(grid);
    }

    public static class HobbyListProvider extends ListDataProvider<HobbyET> {

        public HobbyListProvider(Collection<HobbyET> items) {
            super(items);
        }
    }

}
