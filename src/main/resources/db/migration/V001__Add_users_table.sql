create table t_users
(
    id       int          not null primary key,
    username varchar(255) not null,
    password varchar(255) not null,
    enabled  boolean      not null,
    constraint uq_username unique (username)
);

create sequence users_pk_seq start with 100 increment by 1;