package com.github.sc.apps.saisc.chat;

import com.github.sc.apps.saisc.common.frontend.BaseLayout;
import com.github.sc.apps.saisc.common.frontend.BasicListView;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.function.ValueProvider;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteParameters;
import com.vaadin.flow.router.RouterLink;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Map;

@Slf4j
@PageTitle("Chat History")
@Route(value = "chat-history", layout = BaseLayout.class)
public class ChatHistoryView extends BasicListView<ChatHistoryRepository, ChatHistoryTO, String> {

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
    protected Map<ValueProvider<ChatHistoryTO, ?>, String> getColumns() {
        return Map.of(
                ChatHistoryTO::getConversationId, "Conversation ID",
                ChatHistoryTO::getStart, "Start",
                ChatHistoryTO::getEnd, "End",
                ChatHistoryTO::getMessageCount, "Message Count"
        );
    }

    @Override
    protected void customizeGrid(Grid<ChatHistoryTO> grid) {
        grid.addComponentColumn(chat ->
                        new RouterLink("Details", ChatView.class,
                                new RouteParameters("chatId", String.valueOf(chat.conversationId))))
                .setHeader("Detail")
                .setAutoWidth(true)
                .setFlexGrow(0);
    }
}
