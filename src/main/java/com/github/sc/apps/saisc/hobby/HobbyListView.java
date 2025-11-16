package com.github.sc.apps.saisc.hobby;

import com.github.sc.apps.saisc.common.frontend.BaseLayout;
import com.github.sc.apps.saisc.common.frontend.BasicListView;
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

@PageTitle("Hobbies")
@Route(value = "hobby", layout = BaseLayout.class)
public class HobbyListView extends BasicListView<HobbyRepository, HobbyET, Integer> {


    @Autowired
    public HobbyListView(HobbyRepository repository) {
        super(repository);
    }

    @Override
    protected String[] getSortFields() {
        return new String[]{"name"};
    }

    @Override
    protected List<TextField> getFilters() {
        return List.of();
    }

    @Override
    protected Map<ValueProvider<HobbyET, ?>, String> getColumns() {
        var columns = new LinkedHashMap<ValueProvider<HobbyET, ?>, String>();
        columns.put(HobbyET::getName, "Name");
        return columns;
    }

    @Override
    protected void customizeGrid(Grid<HobbyET> grid) {
        grid.addComponentColumn(person ->
                        new RouterLink("Details", HobbyDetailView.class,
                                new RouteParameters("hobbyId", String.valueOf(person.getId()))))
                .setHeader("Detail")
                .setAutoWidth(true)
                .setFlexGrow(0);
    }

}
