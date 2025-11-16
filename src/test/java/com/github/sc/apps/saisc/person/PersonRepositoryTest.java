package com.github.sc.apps.saisc.person;

import com.github.sc.apps.saisc.TestcontainersConfiguration;
import com.github.sc.apps.saisc.hobby.persistence.HobbyRepository;
import com.github.sc.apps.saisc.person.persistence.PersonRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

@DataJpaTest
@Import(TestcontainersConfiguration.class)
class PersonRepositoryTest {

    @Autowired
    private PersonRepository personRepository;

    @Autowired
    private HobbyRepository hobbyRepository;

    @Test
    void testFindByHobby() {
        var hobby = hobbyRepository.findByNameIgnoreCase("Cycling");
        var persons = personRepository.findAllByHobby(hobby.get().getId());
        Assertions.assertThat(persons).isNotEmpty();
    }

}