package com.github.sc.apps.saisc.shared.web;

import com.github.sc.apps.saisc.shared.infra.FindAllRepository;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.function.ValueProvider;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.Map;

public abstract class BasicListView<R extends FindAllRepository<T, ID>, T, ID> extends VerticalLayout {

    private final R repository;

    public BasicListView(R repository) {
        this.repository = repository;
        createContent();
    }

    protected abstract String[] getSortFields();

    protected abstract List<TextField> getFilters();

    protected abstract Map<ValueProvider<T, ?>, String> getColumns();

    protected void customizeGrid(Grid<T> grid) {
    }

    private void createContent() {
        var filters = getFilters();
        var grid = new Grid<>(getEntities());
        grid.removeAllColumns();

        getColumns().forEach((k, v) -> grid.addColumn(k).setHeader(v));
        customizeGrid(grid);
        grid.setSizeFull();

        filters.forEach(this::add);
        add(grid);
        setSizeFull();
        setPadding(false);
        setSpacing(false);
        setMargin(false);
        expand(grid);
    }

    private List<T> getEntities() {
        if (getSortFields() == null || getSortFields().length == 0) {
            return repository.findAllForListView();
        } else {
            return repository.findAllForListView(Sort.by(getSortFields()).ascending());
        }
    }

}
