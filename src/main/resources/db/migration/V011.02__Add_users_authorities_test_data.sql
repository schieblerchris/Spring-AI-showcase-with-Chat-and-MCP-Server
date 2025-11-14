INSERT INTO t_users(id, username, password, enabled)
VALUES (nextval('users_pk_seq'), 'admin', '$2a$12$H5avIodL8F078mnhasywQusfEZbPZhQZsUZYnvrkaQ/OockfXL9g.', true),
       (nextval('users_pk_seq'), 'mhm', '$2a$12$H5avIodL8F078mnhasywQusfEZbPZhQZsUZYnvrkaQ/OockfXL9g.', true)
;

INSERT INTO t_authorities (username, authority)
VALUES ('admin', 'ROLE_ADMIN'),
       ('mhm', 'ROLE_USER')
;
