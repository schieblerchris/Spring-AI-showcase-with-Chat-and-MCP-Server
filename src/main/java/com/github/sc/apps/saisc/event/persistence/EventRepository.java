package com.github.sc.apps.saisc.event.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EventRepository extends JpaRepository<EventET, Integer> {
    @Query(value = "select e.* from t_event e join t_person_event pe on pe.event_fk = e.id where pe.person_fk = :personId", nativeQuery = true)
    List<EventET> findAllByPerson(int personId);
}
