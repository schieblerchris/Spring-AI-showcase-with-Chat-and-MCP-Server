package com.github.sc.apps.saisc.person.persistence;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "t_person_hobby")
@IdClass(PersonHobbyET.PersonHobbyKey.class)
public class PersonHobbyET {


    @Id
    @Column(name = "person_fk")
    private int person;
    @Id
    @Column(name = "hobby_fk")
    private int hobby;

    @Column(name = "skill_level")
    @Enumerated(EnumType.STRING)
    private SkillLevel skillLevel;

    @Data
    public static class PersonHobbyKey {
        private int person;
        private int hobby;
    }

    public enum SkillLevel {
        NONE,
        BEGINNER,
        INTERMEDIATE,
        ADVANCED,
        EXPERT;
    }


}
