create table t_vacation
(
    id         int  not null primary key,
    person_fk  int  not null,
    start_date date not null,
    end_date   date not null,
    comment    varchar(255),
    constraint fk_vacation_person foreign key (person_fk) references t_person (id) on delete cascade
);

create sequence vacation_pk_seq start with 300 increment by 1;
