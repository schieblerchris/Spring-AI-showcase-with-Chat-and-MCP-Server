package com.github.sc.apps.saisc.common.frontend;

import com.github.sc.apps.saisc.common.mapping.FindAllRepository;
import com.github.sc.apps.saisc.hobby.HobbyET;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.PropertyDefinition;
import com.vaadin.flow.data.provider.ListDataProvider;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.function.ValueProvider;
import org.springframework.core.GenericTypeResolver;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Arrays;
import java.util.Collection;
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

    private void createContent() {
        var filters = getFilters();
        var grid = new Grid<>(getEntities());
        grid.removeAllColumns();

        getColumns().forEach((k, v) -> grid.addColumn(k).setHeader(v));
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
