package com.github.sc.apps.saisc.dump.service;

import com.github.sc.apps.saisc.dump.model.DumpEventTO;
import com.github.sc.apps.saisc.dump.model.DumpHobbyTO;
import com.github.sc.apps.saisc.dump.model.DumpModelTO;
import com.github.sc.apps.saisc.dump.model.DumpPersonTO;
import com.github.sc.apps.saisc.event.persistence.EventRepository;
import com.github.sc.apps.saisc.hobby.model.HobbyET;
import com.github.sc.apps.saisc.hobby.persistence.HobbyRepository;
import com.github.sc.apps.saisc.person.persistence.PersonHobbyRepository;
import com.github.sc.apps.saisc.person.persistence.PersonRepository;
import com.github.sc.apps.saisc.vacation.persistence.VacationRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@Slf4j
public class DumpService {

    public static final HobbyET DEFAULT_HOBBY = new HobbyET("unknown");
    private final PersonRepository personRepository;
    private final PersonHobbyRepository personHobbyRepository;
    private final HobbyRepository hobbyRepository;
    private final EventRepository eventRepository;
    private final VacationRepository vacationRepository;

    @Autowired
    public DumpService(PersonRepository personRepository, PersonHobbyRepository personHobbyRepository, HobbyRepository hobbyRepository, EventRepository eventRepository, VacationRepository vacationRepository) {
        this.personRepository = personRepository;
        this.personHobbyRepository = personHobbyRepository;
        this.hobbyRepository = hobbyRepository;
        this.eventRepository = eventRepository;
        this.vacationRepository = vacationRepository;
    }

    public DumpModelTO dump() {
        log.debug("dumping data");
        var persons = personRepository.findAll();
        var hobbies = hobbyRepository.findAll();
        var events = eventRepository.findAll();
        var vacations = vacationRepository.findAll();
        var hobbyMap = hobbies.stream().collect(Collectors.toMap(HobbyET::getId, Function.identity()));
        var vacationMap = vacations.stream()
                .collect(Collectors.groupingBy(vacation -> vacation.getPerson().getId()));
        var dumpPersons = persons.stream().map(p -> {
            var personHobbies = personHobbyRepository.findByPerson(p.getId());
            return new DumpPersonTO(
                    p.getId(),
                    p.getFirstName(),
                    p.getLastName(),
                    p.getBirthdate(),
                    personHobbies.stream().map(ph -> new DumpPersonTO.SkillTO(ph.getHobby(), hobbyMap.getOrDefault(ph.getHobby(), DEFAULT_HOBBY).getName(), ph.getSkillLevel().name())).toList(),
                    vacationMap.getOrDefault(p.getId(), List.of()).stream().map(v -> new DumpPersonTO.VacationTO(v.getStartDate(), v.getEndDate(), v.getComment())).toList()
            );
        }).toList();
        return new DumpModelTO(
                dumpPersons,
                events.stream().map(e -> new DumpEventTO(e.getId(), e.getTitle(), e.getStartDate(), e.getEndDate(), e.getHobbyId(), hobbyMap.getOrDefault(e.getHobbyId(), DEFAULT_HOBBY).getName())).toList(),
                hobbies.stream().map(h -> new DumpHobbyTO(h.getId(), h.getName())).toList()
        );
    }
}
