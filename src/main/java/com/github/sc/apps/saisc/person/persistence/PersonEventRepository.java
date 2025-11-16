package com.github.sc.apps.saisc.person.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PersonEventRepository extends JpaRepository<PersonEventET, PersonEventET.PersonEventKey> {

    Optional<PersonEventET> findByPersonAndEvent(int person, int event);

    List<PersonEventET> findByPerson(int person);

}
