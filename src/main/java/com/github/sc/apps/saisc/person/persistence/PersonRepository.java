package com.github.sc.apps.saisc.person.persistence;

import com.github.sc.apps.saisc.common.mapping.FindAllRepository;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PersonRepository extends FindAllRepository<PersonET, Integer>, JpaRepository<PersonET, Integer> {

    List<PersonET> findAllByFirstNameLikeIgnoreCase(String firstName);

    List<PersonET> findAllByLastNameLikeIgnoreCase(String lastName);

    List<PersonET> findAllByFirstNameLikeIgnoreCaseAndLastNameIgnoreCase(String firstName, String lastName);

    @Query("select ph from PersonHobbyET ph where ph.hobby = :hobbyId")
    List<PersonHobbyET> findAllByHobby(Integer hobbyId);

    @Override
    default List<PersonET> findAllForListView() {
        return findAll();
    }

    @Override
    default List<PersonET> findAllForListView(Sort sort) {
        return findAll(sort);
    }
}
