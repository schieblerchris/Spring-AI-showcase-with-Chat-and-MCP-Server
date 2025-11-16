package com.github.sc.apps.saisc.vacation.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface VacationRepository extends JpaRepository<VacationET, Integer> {

    @Query(value = "select v from VacationET v where v.person.id = :personId and v.startDate <= :date and v.endDate >= :date")
    VacationET findVacationByPersonAndDate(int personId, LocalDate date);

    @Query(value = "select p.id from PersonET p where p.id not in (select distinct v.person.id from VacationET v where v.startDate <= :date and v.endDate >= :date)")
    List<Integer> findAvailablePersons(LocalDate date);

    @Query("select v from VacationET v where v.person.id = :personId")
    List<VacationET> findAllByPersonId(int personId);

}
