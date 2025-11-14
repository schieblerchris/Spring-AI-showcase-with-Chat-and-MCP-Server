create table t_person_hobby
(
    person_fk   int         not null,
    hobby_fk    int         not null,
    skill_level varchar(32) not null default 'NONE',
    primary key (person_fk, hobby_fk),
    constraint fk_person_hobby_person foreign key (person_fk) references t_person (id) on delete cascade,
    constraint fk_person_hobby_hobby foreign key (hobby_fk) references t_hobby (id) on delete cascade
);
