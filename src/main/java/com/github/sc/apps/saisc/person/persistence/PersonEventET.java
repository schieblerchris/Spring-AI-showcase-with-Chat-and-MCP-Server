package com.github.sc.apps.saisc.person.persistence;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "t_person_event")
@IdClass(PersonEventET.PersonEventKey.class)
public class PersonEventET {

    @Id
    @Column(name = "person_fk")
    private int person;
    @Id
    @Column(name = "event_fk")
    private int event;

    @Data
    public static class PersonEventKey {
        private int person;
        private int event;
    }

}
