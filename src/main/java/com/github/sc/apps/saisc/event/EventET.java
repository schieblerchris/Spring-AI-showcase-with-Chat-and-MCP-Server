package com.github.sc.apps.saisc.event;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;

@Data
@Entity
@Table(name = "t_event")
public class EventET {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "event_pk_seq")
    @SequenceGenerator(name = "event_pk_seq", sequenceName = "event_pk_seq", allocationSize = 1)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(name = "title", nullable = false, length = 255)
    private String title;

    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    @Column(name = "end_date", nullable = false)
    private LocalDate endDate;

    @Column(name = "hobby_fk")
    private Integer hobbyId;
}
