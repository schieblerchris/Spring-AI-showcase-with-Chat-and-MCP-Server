create table t_hobby
(
    id          int          not null primary key,
    name        varchar(255) not null
);

create sequence hobby_pk_seq start with 800 increment by 1;
