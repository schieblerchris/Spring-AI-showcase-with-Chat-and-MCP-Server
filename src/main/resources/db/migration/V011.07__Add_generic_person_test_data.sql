INSERT INTO t_person_hobby (person_fk, hobby_fk, skill_level)
SELECT p2.id, h.id, 'NONE'
from (SELECT p.id FROM t_person p where p.id not in (select person_fk from t_person_hobby) limit 20) p2,
     t_hobby h
order by p2.id;
INSERT INTO t_person_hobby (person_fk, hobby_fk, skill_level)
SELECT p2.id, h.id, 'BEGINNER'
from (SELECT p.id FROM t_person p where p.id not in (select person_fk from t_person_hobby) limit 20) p2,
     t_hobby h
order by p2.id;
INSERT INTO t_person_hobby (person_fk, hobby_fk, skill_level)
SELECT p2.id, h.id, 'INTERMEDIATE'
from (SELECT p.id FROM t_person p where p.id not in (select person_fk from t_person_hobby) limit 20) p2,
     t_hobby h
order by p2.id;
INSERT INTO t_person_hobby (person_fk, hobby_fk, skill_level)
SELECT p2.id, h.id, 'ADVANCED'
from (SELECT p.id FROM t_person p where p.id not in (select person_fk from t_person_hobby) limit 20) p2,
     t_hobby h
order by p2.id;

DO $$
DECLARE
    person_record RECORD;
BEGIN
    FOR person_record IN
        SELECT ph.person_fk, ph.hobby_fk
        FROM t_person_hobby ph
        WHERE ph.skill_level in ('INTERMEDIATE', 'ADVANCED')
        LOOP
            INSERT INTO t_vacation(id, person_fk, start_date, end_date, comment)
            VALUES
                (nextval('vacation_pk_seq'), person_record.person_fk, cyd(1,1), cyd(1,31), 'Holiday January'),
                (nextval('vacation_pk_seq'), person_record.person_fk, cyd(2,1), cyd(2,28), 'Holiday February'),
                (nextval('vacation_pk_seq'), person_record.person_fk, cyd(3,1), cyd(3,31), 'Holiday March'),
                (nextval('vacation_pk_seq'), person_record.person_fk, cyd(4,1), cyd(4,30), 'Holiday April'),
                (nextval('vacation_pk_seq'), person_record.person_fk, cyd(5,1), cyd(5,31), 'Holiday May'),
                (nextval('vacation_pk_seq'), person_record.person_fk, cyd(6,1), cyd(6,30), 'Holiday June'),
                (nextval('vacation_pk_seq'), person_record.person_fk, cyd(7,1), cyd(7,31), 'Holiday July'),
                (nextval('vacation_pk_seq'), person_record.person_fk, cyd(8,1), cyd(8,31), 'Holiday August'),
                (nextval('vacation_pk_seq'), person_record.person_fk, cyd(9,1), cyd(9,30), 'Holiday September'),
                (nextval('vacation_pk_seq'), person_record.person_fk, cyd(10,1), cyd(10,31), 'Holiday October'),
                (nextval('vacation_pk_seq'), person_record.person_fk, cyd(11,1), cyd(11,30), 'Holiday November'),
                (nextval('vacation_pk_seq'), person_record.person_fk, cyd(12,1), cyd(12,31), 'Holiday December'),
                (nextval('vacation_pk_seq'), person_record.person_fk, nyd(1,1), nyd(1,31), 'Holiday January'),
                (nextval('vacation_pk_seq'), person_record.person_fk, nyd(2,1), nyd(2,28), 'Holiday February'),
                (nextval('vacation_pk_seq'), person_record.person_fk, nyd(3,1), nyd(3,31), 'Holiday March'),
                (nextval('vacation_pk_seq'), person_record.person_fk, nyd(4,1), nyd(4,30), 'Holiday April'),
                (nextval('vacation_pk_seq'), person_record.person_fk, nyd(5,1), nyd(5,31), 'Holiday May'),
                (nextval('vacation_pk_seq'), person_record.person_fk, nyd(6,1), nyd(6,30), 'Holiday June'),
                (nextval('vacation_pk_seq'), person_record.person_fk, nyd(7,1), nyd(7,31), 'Holiday July'),
                (nextval('vacation_pk_seq'), person_record.person_fk, nyd(8,1), nyd(8,31), 'Holiday August'),
                (nextval('vacation_pk_seq'), person_record.person_fk, nyd(9,1), nyd(9,30), 'Holiday September'),
                (nextval('vacation_pk_seq'), person_record.person_fk, nyd(10,1), nyd(10,31), 'Holiday October'),
                (nextval('vacation_pk_seq'), person_record.person_fk, nyd(11,1), nyd(11,30), 'Holiday November'),
                (nextval('vacation_pk_seq'), person_record.person_fk, nyd(12,1), nyd(12,31), 'Holiday December');
        END LOOP;
END $$;
