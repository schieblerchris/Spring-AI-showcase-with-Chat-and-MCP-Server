create table t_avatar
(
    person_fk int primary key,
    avatar    bytea not null,
    constraint fk_avatar_person foreign key (person_fk) references t_person (id) on delete cascade,
    constraint avatar_size_check check (octet_length(avatar) <= 4194304)
);
