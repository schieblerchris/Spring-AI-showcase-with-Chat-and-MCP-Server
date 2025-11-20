package com.github.sc.apps.saisc.person.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;

@Data
@Entity
@Table(name = "t_person")
public class PersonET {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "person_pk_seq")
    @SequenceGenerator(sequenceName = "person_pk_seq", name = "person_pk_seq", initialValue = 200, allocationSize = 1)
    private Integer id;
    @Column(name = "user_fk")
    private Integer user;
    @Column(name = "first_name", nullable = false)
    private String firstName;
    @Column(name = "last_name", nullable = false)
    private String lastName;
    @Column(name = "birthdate", nullable = false)
    private LocalDate birthdate;
    @Column(name = "email", nullable = false)
    private String email;

}
