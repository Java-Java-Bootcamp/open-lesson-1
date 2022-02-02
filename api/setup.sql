create schema api;

create table api.notifications
(
    id                  serial primary key,
    type                text,
    username            text,
    max_heart_beats     integer,
    duration_in_minutes integer
);