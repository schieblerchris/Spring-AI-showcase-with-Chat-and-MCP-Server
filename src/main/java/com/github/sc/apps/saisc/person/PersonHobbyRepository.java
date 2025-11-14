package com.github.sc.apps.saisc.person;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PersonHobbyRepository extends JpaRepository<PersonHobbyET, PersonHobbyET.PersonHobbyKey> {

    Optional<PersonHobbyET> findByPersonAndHobby(Integer person, Integer hobby);
    List<PersonHobbyET> findByPerson(Integer person);

}
