create table t_event
(
    id         int          not null primary key,
    title      varchar(255) not null,
    start_date date         not null,
    end_date   date         not null,
    hobby_fk   int,
    skill_level varchar(32) not null default 'NONE',
    constraint fk_event_hobby foreign key (hobby_fk) references t_hobby (id) on delete set null
);

create sequence event_pk_seq start with 400 increment by 1;