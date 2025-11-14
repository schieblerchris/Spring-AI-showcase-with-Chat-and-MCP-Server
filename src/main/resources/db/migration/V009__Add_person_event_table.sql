create table t_person_event
(
    person_fk int not null,
    event_fk  int not null,
    primary key (person_fk, event_fk),
    constraint fk_person_event_person foreign key (person_fk) references t_person (id) on delete cascade,
    constraint fk_person_event_event foreign key (event_fk) references t_event (id) on delete cascade
);
