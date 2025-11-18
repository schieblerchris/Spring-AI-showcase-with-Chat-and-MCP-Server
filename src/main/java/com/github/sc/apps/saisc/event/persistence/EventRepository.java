package com.github.sc.apps.saisc.event.persistence;

import com.github.sc.apps.saisc.event.model.EventET;
import com.github.sc.apps.saisc.shared.infra.FindAllRepository;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface EventRepository extends JpaRepository<EventET, Integer>, FindAllRepository<EventET, Integer> {
    @Query(value = "select e from EventET e join PersonEventET pe on pe.event = e.id where pe.person = :personId")
    List<EventET> findAllByPerson(int personId);

    @Query(value = "select e from EventET e join PersonEventET pe on pe.event = e.id where pe.person = :personId and e.startDate <= :date and e.endDate >= :date")
    List<EventET> findAllByPersonAndDate(int personId, LocalDate date);

    @Query(value = "select e from EventET e where e.startDate <= :date and e.endDate >= :date")
    List<EventET> findAllByDate(LocalDate date);

    List<EventET> findByTitleOrderByStartDateAscTitleAsc(String title);

    @Override
    default List<EventET> findAllForListView() {
        return findAll();
    }

    @Override
    default List<EventET> findAllForListView(Sort sort) {
        return findAll(sort);
    }
}

