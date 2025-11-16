package com.github.sc.apps.saisc.event.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EventRepository extends JpaRepository<EventET, Integer> {
    @Query(value = "select e from EventET e join PersonEventET pe on pe.event = e.id where pe.person = :personId")
    List<EventET> findAllByPerson(int personId);
}
