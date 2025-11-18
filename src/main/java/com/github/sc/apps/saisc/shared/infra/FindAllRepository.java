package com.github.sc.apps.saisc.shared.infra;

import org.springframework.data.domain.Sort;

import java.util.List;

public interface FindAllRepository<T, ID> {

    List<T> findAllForListView();

    List<T> findAllForListView(Sort sort);
}
