package com.github.sc.apps.saisc.chatmodel.view;


import com.github.sc.apps.saisc.chatmodel.api.OpenAIAdapter;
import com.github.sc.apps.saisc.common.view.BaseLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Menu;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;

@Menu(title = "LLM Model")
@Route(value = "chat-model", layout = BaseLayout.class)
public class ChatModelListView extends VerticalLayout {

    private final OpenAIAdapter openAIAdapter;

    @Autowired
    public ChatModelListView(OpenAIAdapter openAIAdapter) {
        this.openAIAdapter = openAIAdapter;
        createContent();
    }


    private void createContent() {
        var grid = new Grid<>(openAIAdapter.getModels());
        grid.addColumn(OpenAIAdapter.ModelData::id).setHeader("Name");
        grid.addColumn(OpenAIAdapter.ModelData::created).setHeader("Created at");

        grid.setSizeFull();

        add(grid);
        setSizeFull();
        setPadding(false);
        setSpacing(false);
        setMargin(false);
        expand(grid);
    }
}
