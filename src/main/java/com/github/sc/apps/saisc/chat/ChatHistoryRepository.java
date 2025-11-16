package com.github.sc.apps.saisc.chat;

import com.github.sc.apps.saisc.common.mapping.FindAllRepository;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChatHistoryRepository extends FindAllRepository<ChatHistoryTO, String>, org.springframework.data.repository.Repository<ChatHistoryTO, String> {

    List<ChatHistoryTO> findAll();

    boolean existsByConversationId(String conversationId);

    @Override
    default List<ChatHistoryTO> findAllForListView() {
        return findAll();
    }

    @Override
    default List<ChatHistoryTO> findAllForListView(Sort sort) {
        return findAll();
    }
}
