create table t_authorities
(
    username  varchar(255)  not null,
    authority varchar(255)  not null,
    constraint pk_authorities primary key (username,authority),
    constraint fk_authorities_users foreign key (username) references t_users (username)
);

