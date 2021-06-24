create table notes_schema.notes
(
    id bigserial primary key,
    name text unique,
    note text,
    creation_date timestamp
)