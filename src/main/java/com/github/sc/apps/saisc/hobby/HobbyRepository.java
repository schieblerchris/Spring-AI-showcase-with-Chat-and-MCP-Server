package com.github.sc.apps.saisc.hobby;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface HobbyRepository extends JpaRepository<HobbyET, Integer> {
    Optional<HobbyET> findByNameIgnoreCase(String name);

}
