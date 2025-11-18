package com.github.sc.apps.saisc.chat.persistence;

import com.github.sc.apps.saisc.chat.model.ChatHistoryVO;
import com.github.sc.apps.saisc.shared.infra.FindAllRepository;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChatHistoryRepository extends FindAllRepository<ChatHistoryVO, String>, org.springframework.data.repository.Repository<ChatHistoryVO, String> {

    List<ChatHistoryVO> findAll();

    @Override
    default List<ChatHistoryVO> findAllForListView() {
        return findAll();
    }

    @Override
    default List<ChatHistoryVO> findAllForListView(Sort sort) {
        return findAll();
    }
}
