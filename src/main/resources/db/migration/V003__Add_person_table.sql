create table t_person
(
    id         int          not null primary key,
    user_fk    int,
    first_name varchar(255) not null,
    last_name  varchar(255) not null,
    birthdate  date         not null,
    constraint fk_person_user foreign key (user_fk) references t_users (id) on delete set null
);

create sequence person_pk_seq start with 200 increment by 1;