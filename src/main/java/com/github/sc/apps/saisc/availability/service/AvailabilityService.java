package com.github.sc.apps.saisc.availability.service;

import com.github.sc.apps.saisc.person.persistence.PersonEventRepository;
import com.github.sc.apps.saisc.vacation.persistence.VacationRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Slf4j
public class AvailabilityService {

    private final PersonEventRepository personEventRepository;
    private final VacationRepository vacationRepository;

    @Autowired
    public AvailabilityService(PersonEventRepository personEventRepository, VacationRepository vacationRepository) {
        this.personEventRepository = personEventRepository;
        this.vacationRepository = vacationRepository;
    }

    public boolean isPersonAvailable(int personId, LocalDate date) {
        log.debug("checking availability for person {} on date {}", personId, date);
        var vacation = vacationRepository.findVacationByPersonAndDate(personId, date);
        var event = personEventRepository.findByPersonAndDate(personId, date);
        if (event != null || vacation != null) {
            return false;
        }
        log.debug("person {} is available", personId);
        return true;
    }

    public Set<Integer> findAvailablePersons(LocalDate date) {
        log.debug("finding available persons for date {}", date);
        var notOnVacation = vacationRepository.findAvailablePersons(date);
        var notOnEvent = personEventRepository.findAvailablePersons(date);
        var union = notOnVacation.stream().filter(notOnEvent::contains).collect(Collectors.toSet());
        union.retainAll(notOnEvent);
        log.debug("found {}", union);
        return union;
    }

}
