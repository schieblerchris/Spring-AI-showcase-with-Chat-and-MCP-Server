package com.github.sc.apps.saisc.hobby.persistence;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Entity
@Table(name = "t_hobby")
public class HobbyET {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "hobby_pk_seq")
    @SequenceGenerator(sequenceName = "hobby_pk_seq", name = "hobby_pk_seq", initialValue = 800, allocationSize = 1)
    private Integer id;

    @Column(name = "name", nullable = false)
    private String name;

    public HobbyET(String name) {
        this.name = name;
    }
}
