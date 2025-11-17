package com.github.sc.apps.saisc.person.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PersonHobbyRepository extends JpaRepository<PersonHobbyET, PersonHobbyET.PersonHobbyKey> {

    Optional<PersonHobbyET> findByPersonAndHobby(int person, int hobby);

    List<PersonHobbyET> findByPerson(int person);

    @Query("select ph.person from PersonHobbyET ph where ph.hobby = :hobbyId and ph.skillLevel = :skillLevel")
    List<Integer> findByHobbyAndSkillLevel(int hobbyId, SkillLevel skillLevel);

}
