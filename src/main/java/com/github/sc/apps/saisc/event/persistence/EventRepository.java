package com.github.sc.apps.saisc.event.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface EventRepository extends JpaRepository<EventET, Integer> {
    @Query(value = "select e from EventET e join PersonEventET pe on pe.event = e.id where pe.person = :personId")
    List<EventET> findAllByPerson(int personId);

    @Query(value = "select e from EventET e join PersonEventET pe on pe.event = e.id where pe.person = :personId and e.startDate <= :date and e.endDate >= :date")
    List<EventET> findAllByPersonAndDate(int personId, LocalDate date);

    @Query(value = "select e from EventET e where e.startDate <= :date and e.endDate >= :date")
    List<EventET> findAllByDate(LocalDate date);

}

