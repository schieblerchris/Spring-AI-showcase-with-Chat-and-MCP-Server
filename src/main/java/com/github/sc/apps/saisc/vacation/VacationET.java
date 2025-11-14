package com.github.sc.apps.saisc.vacation;

import com.github.sc.apps.saisc.person.PersonET;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;

@Data
@Entity
@Table(name = "t_vacation")
public class VacationET {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "vacation_pk_seq")
    @SequenceGenerator(sequenceName = "vacation_pk_seq", name = "vacation_pk_seq", initialValue = 300, allocationSize = 1)
    private Integer id;
    @ManyToOne
    @JoinColumn(name = "person_fk", nullable = false)
    private PersonET person;
    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;
    @Column(name = "end_date", nullable = false)
    private LocalDate endDate;
    @Column(name = "comment")
    private String comment;
}
