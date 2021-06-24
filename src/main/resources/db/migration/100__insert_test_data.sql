insert into notes_schema.notes
values (100, 'note first', 'some text first', now()),
       (101, 'note second', 'some text second', now()),
       (102, 'note third', 'some text third', now()),
       (103, 'note fourth', 'some text fourth', now());
insert into notes_schema.hashtag
values (100, '#1'),
       (101, '#2'),
       (102, '#3'),
       (103, '#4'),
       (104, '#5'),
       (105, '#6');
insert into notes_schema.note_hashtag
values (100, 100),
       (101, 101),
       (100, 102),
       (102, 103),
       (103, 104),
       (102, 100),
       (101, 102),
       (100, 101),
       (103, 105);
