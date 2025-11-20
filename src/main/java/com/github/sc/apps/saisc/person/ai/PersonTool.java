package com.github.sc.apps.saisc.person.ai;

import com.github.sc.apps.saisc.person.model.PersonHobbyET;
import com.github.sc.apps.saisc.person.model.SkillLevel;
import com.github.sc.apps.saisc.person.persistence.PersonHobbyRepository;
import com.github.sc.apps.saisc.person.persistence.PersonRepository;
import com.github.sc.apps.saisc.shared.mcp.ToolMarkerInterface;
import lombok.extern.slf4j.Slf4j;
import org.springaicommunity.mcp.annotation.McpTool;
import org.springaicommunity.mcp.annotation.McpToolParam;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Component
@Slf4j
public class PersonTool implements ToolMarkerInterface {

    private final PersonRepository personRepository;
    private final PersonHobbyRepository personHobbyRepository;

    @Autowired
    public PersonTool(PersonRepository personRepository, PersonHobbyRepository personHobbyRepository) {
        this.personRepository = personRepository;
        this.personHobbyRepository = personHobbyRepository;
    }

    @McpTool(name = "get_person_by_id", description = "get a specific person by id")
    @Tool(name = "get_person_by_id", description = "get a specific person by id")
    public PersonToolResponse getPersonById(@McpToolParam(description = "The id of the person") @ToolParam(description = "The id of the person") Integer id) {
        log.debug("get person by id {}", id);
        var result = personRepository.findById(id).map(p -> new PersonToolResponse(p.getId(), p.getFirstName(), p.getLastName(), p.getBirthdate(), p.getEmail())).orElse(null);
        log.debug("found person: {}", result);
        return result;
    }

    @McpTool(name = "get_every_persons_hobby_skill_level", description = "get the skill level of every person for the hobby")
    @Tool(name = "get_every_persons_hobby_skill_level", description = "get the skill level of every person for the hobby")
    public List<PersonHobbyToolResponse> getAllPersonSkillLevelForHobby(@McpToolParam(description = "The hobby id") @ToolParam(description = "The hobby id") Integer hobbyId) {
        log.debug("get every persons hobby skill level for hobby {}", hobbyId);
        var result = personRepository.findAllByHobby(hobbyId).stream().map(p -> new PersonHobbyToolResponse(p.getPerson(), p.getHobby(), p.getSkillLevel())).collect(Collectors.toSet()).stream().toList();
        log.debug("found: {}", result);
        return result;
    }

    @McpTool(name = "get_person_by_first_name_and_last_name", description = "get a specific person by their first name and last name")
    @Tool(name = "get_person_by_first_name_and_last_name", description = "get a specific person by their first name and last name")
    public List<PersonToolResponse> getPersonByFirstnameAndLastname(@McpToolParam(description = "The first name of the person") @ToolParam(description = "The first name of the person") String firstName, @McpToolParam(description = "The last name of the person") @ToolParam(description = "The last name of the person") String lastName) {
        log.debug("get person by first name {} and last name {}", firstName, lastName);
        var result = personRepository.findAllByFirstNameLikeIgnoreCaseAndLastNameIgnoreCase(firstName, lastName).stream().map(p -> new PersonToolResponse(p.getId(), p.getFirstName(), p.getLastName(), p.getBirthdate(), p.getEmail())).toList();
        log.debug("found: {}", result);
        return result;
    }

    @McpTool(name = "get_person_hobby_skill_level", description = "get the skill level of the person for the hobby")
    @Tool(name = "get_person_hobby_skill_level", description = "get the skill level of the person for the hobby")
    public SkillLevel getPersonSkillLevelForHobby(
            @McpToolParam(description = "The id of the person") @ToolParam(description = "The id of the person") Integer id,
            @McpToolParam(description = "The hobby id") @ToolParam(description = "The hobby id") Integer hobbyId
    ) {
        log.debug("get person hobby skill level for person {} and hobby {}", id, hobbyId);
        var result = personHobbyRepository.findByPersonAndHobby(id, hobbyId).map(PersonHobbyET::getSkillLevel).orElse(SkillLevel.NONE);
        log.debug("found skill level: {} for person {} and hobby {}", result, id, hobbyId);
        return result;
    }

    @Override
    public String name() {
        return "Person";
    }

    public record PersonToolResponse(int personId, String fistName, String lastName, LocalDate birthday, String email) {
    }

    public record PersonHobbyToolResponse(int personId, int hobbyId, SkillLevel skillLevel) {
    }

}
