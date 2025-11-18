package com.github.sc.apps.saisc.chat.view;

import com.github.sc.apps.saisc.chat.model.ChatHistoryVO;
import com.github.sc.apps.saisc.chat.persistence.ChatHistoryRepository;
import com.github.sc.apps.saisc.shared.web.BaseLayout;
import com.github.sc.apps.saisc.shared.web.BasicListView;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.function.ValueProvider;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteParameters;
import com.vaadin.flow.router.RouterLink;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.LinkedHashMap;
import java.util.List;

@Slf4j
@PageTitle("Chat History")
@Route(value = "chat-history", layout = BaseLayout.class)
public class ChatHistoryView extends BasicListView<ChatHistoryRepository, ChatHistoryVO, String> {

    @Autowired
    public ChatHistoryView(ChatHistoryRepository repository) {
        super(repository);
    }

    @Override
    protected String[] getSortFields() {
        return new String[0];
    }

    @Override
    protected List<TextField> getFilters() {
        return List.of();
    }

    @Override
    protected LinkedHashMap<ValueProvider<ChatHistoryVO, ?>, String> getColumns() {
        var columns = new LinkedHashMap<ValueProvider<ChatHistoryVO, ?>, String>();
        columns.put(ChatHistoryVO::getConversationId, "Conversation ID");
        columns.put(ChatHistoryVO::getStart, "Start");
        columns.put(ChatHistoryVO::getEnd, "End");
        columns.put(ChatHistoryVO::getMessageCount, "Message Count");
        return columns;
    }

    @Override
    protected void customizeGrid(Grid<ChatHistoryVO> grid) {
        grid.addComponentColumn(chat ->
                        new RouterLink("Details", ChatView.class,
                                new RouteParameters("chatId", String.valueOf(chat.getConversationId()))))
                .setHeader("Detail")
                .setAutoWidth(true)
                .setFlexGrow(0);
    }
}
