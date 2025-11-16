DO
$$
    DECLARE
        meyerId            INT;
        birnbaumId         INT;
        cyclingId          INT;
        efCYD_INTERMEDIATE INT;
        efNYD_INTERMEDIATE INT;
        efCYD_ADVANCED     INT;
        efNYD_ADVANCED     INT;
    BEGIN
        SELECT id
        INTO meyerId
        FROM t_person
        WHERE first_name = 'Christopher'
          AND last_name = 'Meyer';

        SELECT id
        INTO birnbaumId
        FROM t_person
        WHERE first_name = 'Hildegart'
          AND last_name = 'Birnbaum';

        SELECT id
        INTO cyclingId
        FROM t_hobby
        WHERE name = 'Cycling';

        SELECT id
        INTO efCYD_INTERMEDIATE
        FROM t_event
        WHERE title = 'Eschborn Frankfurt Express' and start_date = cyd(5, 1);
        SELECT id
        INTO efNYD_INTERMEDIATE
        FROM t_event
        WHERE title = 'Eschborn Frankfurt Express' and start_date = nyd(5, 1);
        SELECT id
        INTO efCYD_ADVANCED
        FROM t_event
        WHERE title = 'Eschborn Frankfurt Classic' and start_date = cyd(5, 1);
        SELECT id
        INTO efNYD_ADVANCED
        FROM t_event
        WHERE title = 'Eschborn Frankfurt Classic' and start_date = nyd(5, 1);

        INSERT INTO t_vacation(id, person_fk, start_date, end_date, comment)
        VALUES (nextval('vacation_pk_seq'), meyerId, cyd(1, 1), cyd(1, 10), 'Holiday'),
               (nextval('vacation_pk_seq'), meyerId, cyd(7, 24), cyd(7, 25), 'Rad am Ring'),
               (nextval('vacation_pk_seq'), meyerId, cyd(12, 1), cyd(12, 31), 'Christmas'),
               (nextval('vacation_pk_seq'), meyerId, nyd(1, 1), nyd(1, 10), 'Holiday'),
               (nextval('vacation_pk_seq'), meyerId, nyd(7, 24), nyd(7, 25), 'Rad am Ring'),
               (nextval('vacation_pk_seq'), meyerId, nyd(12, 1), nyd(12, 31), 'Christmas');

        INSERT INTO t_person_event(person_fk, event_fk)
        VALUES (meyerId, efCYD_ADVANCED),
               (meyerId, efNYD_ADVANCED);

        INSERT INTO t_person_hobby (person_fk, hobby_fk, skill_level)
        VALUES (meyerId, cyclingId, 'EXPERT');

        INSERT INTO t_vacation(id, person_fk, start_date, end_date, comment)
        VALUES (nextval('vacation_pk_seq'), birnbaumId, cyd(1, 1), cyd(1, 10), 'Holiday'),
               (nextval('vacation_pk_seq'), birnbaumId, cyd(5, 1), cyd(5, 1), 'Eschborn-Frankfurt'),
               (nextval('vacation_pk_seq'), birnbaumId, cyd(7, 24), cyd(7, 25), 'Rad am Ring'),
               (nextval('vacation_pk_seq'), birnbaumId, cyd(12, 1), cyd(12, 31), 'Christmas'),
               (nextval('vacation_pk_seq'), birnbaumId, nyd(1, 1), nyd(1, 10), 'Holiday'),
               (nextval('vacation_pk_seq'), birnbaumId, nyd(5, 1), nyd(5, 1), 'Eschborn-Frankfurt'),
               (nextval('vacation_pk_seq'), birnbaumId, nyd(7, 24), nyd(7, 25), 'Rad am Ring'),
               (nextval('vacation_pk_seq'), birnbaumId, nyd(12, 1), nyd(12, 31), 'Christmas');

        INSERT INTO t_person_event(person_fk, event_fk)
        VALUES (birnbaumId, efCYD_INTERMEDIATE),
               (birnbaumId, efCYD_ADVANCED);

        INSERT INTO t_person_hobby (person_fk, hobby_fk, skill_level)
        VALUES (birnbaumId, cyclingId, 'ADVANCED');
    END
$$;
