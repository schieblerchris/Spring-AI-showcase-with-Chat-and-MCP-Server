package com.github.sc.apps.saisc.hobby.persistence;

import com.github.sc.apps.saisc.common.mapping.FindAllRepository;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface HobbyRepository extends JpaRepository<HobbyET, Integer>, FindAllRepository<HobbyET, Integer> {
    Optional<HobbyET> findByNameIgnoreCase(String name);

    @Override
    default List<HobbyET> findAllForListView() {
        return findAll();
    }

    @Override
    default List<HobbyET> findAllForListView(Sort sort) {
        return findAll(sort);
    }
}
