CREATE EXTENSION IF NOT EXISTS unaccent;
update t_person
set email = concat(unaccent(last_name), '.', unaccent(first_name), '@demo.saisc')
where email is null;