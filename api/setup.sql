create schema api;

create table api.notifications
(
    id                  serial primary key,
    type                text,
    username            text,
    max_heart_beats     integer,
    duration_in_minutes integer
);

create table api.telemetry_violation
(
    id          serial primary key,
    username    text,
    occurred_at timestamptz,
    message     text,
    is_new      bool default true
);

insert into api.telemetry_violation(id, username, occurred_at, message)
values (nextval('api.telemetry_violation_id_seq'), 'oagafonov', '2022-02-02T01:00:00+03:00', 'test 1'),
       (nextval('api.telemetry_violation_id_seq'), 'oagafonov', '2022-02-02T02:00:00+03:00', 'test 2');