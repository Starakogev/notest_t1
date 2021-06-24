create table notes_schema.note_hashtag
(
    note_id bigint references notes_schema.notes(id) on delete cascade ,
    hashtag_id bigint references notes_schema.hashtag(id) on delete cascade
)