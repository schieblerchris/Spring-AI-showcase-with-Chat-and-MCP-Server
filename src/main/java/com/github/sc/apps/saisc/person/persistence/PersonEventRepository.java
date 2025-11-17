package com.github.sc.apps.saisc.person.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface PersonEventRepository extends JpaRepository<PersonEventET, PersonEventET.PersonEventKey> {

    @Query(value = "select pe from PersonEventET pe join EventET e on pe.event = e.id where pe.person = :personId and e.startDate <= :date and e.endDate >= :date")
    PersonEventET findByPersonAndDate(int personId, LocalDate date);

    @Query(value = "select p.id from PersonET p where p.id not in (select distinct pe.person from PersonEventET pe join EventET e on pe.event = e.id where e.startDate <= :date and e.endDate >= :date)")
    List<Integer> findAvailablePersons(LocalDate date);

    List<PersonEventET> findAllByEvent(int event);

    Optional<PersonEventET> findByPersonAndEvent(int person, int event);

    List<PersonEventET> findByPerson(int person);

}
