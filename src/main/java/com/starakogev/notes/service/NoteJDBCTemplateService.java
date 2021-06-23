package com.starakogev.notes.service;

import com.starakogev.notes.jdbc.NoteDAO;
import com.starakogev.notes.jdbc.NoteMapper;
import com.starakogev.notes.entity.Note;
import com.starakogev.notes.dto.NoteDto;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class NoteJDBCTemplateService implements NoteDAO {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public String create(Note note) {
        String sql = "insert into notes_schema.notes (name, note, creation_date, hashtag) values (?, ?, ?, ?)";
        jdbcTemplate.update(sql, note.getName(), note.getNote(), Timestamp.valueOf(LocalDateTime.now()),
                note.getHashtag());
        return "Создана заметка: " + "'" + note.getName() + "'";
    }

    @Override
    public NoteDto getNoteByHashtag(String hashtag) {
        String sql = "select * from notes_schema.notes where hashtag = ?";
        Note note = jdbcTemplate.queryForObject(sql, new String[]{hashtag}, new NoteMapper());
        return NoteDto.builder()
                .name(note.getName())
                .note(note.getNote())
                .creationDate(note.getCreationDate())
                .build();
    }

    @Override
    public String delete(String name) {
        String sql = "delete from notes_schema.notes where name = ?";
        jdbcTemplate.update(sql, name);
        return "Заметка с именем: " + "'" + name + "'" + " was deleted!";
    }

    @Override
    public String update(String name, String note) {
        String sql = "update notes_schema.notes set note = ? where name = ?";
        jdbcTemplate.update(sql, note, name);
        return "Заметка с именем: " + "'" + name + "'" + " изменена";
    }

    @Override
    public List<NoteDto> listNotesByTime(String min, String max) {
        if (min == null && max == null) {
            return listNotes();
        } else if (min != null && max == null) {
            LocalDateTime minTime = LocalDateTime.parse(min);
            String sql = "select * from notes_schema.notes where creation_date > ?";
            List<Note> notes = jdbcTemplate.query(sql, new LocalDateTime[]{minTime}, new NoteMapper());
            return parseListNoteToDto(notes);
        } else if (min == null) {
            LocalDateTime maxTime = LocalDateTime.parse(max);
            String sql = "select * from notes_schema.notes where creation_date < ?";
            List<Note> notes = jdbcTemplate.query(sql, new LocalDateTime[]{maxTime}, new NoteMapper());
            return parseListNoteToDto(notes);
        } else {
            LocalDateTime minTime = LocalDateTime.parse(min);
            LocalDateTime maxTime = LocalDateTime.parse(max);
            String sql = "select * from notes_schema.notes where creation_date > ? and creation_date < ?";
            List<Note> notes = jdbcTemplate.query(sql, new LocalDateTime[]{minTime, maxTime}, new NoteMapper());
            return parseListNoteToDto(notes);
        }
    }

    @Override
    public List<NoteDto> getNoteByText(String text) {
        String word = text + ":*";
        String sql = "SELECT * FROM notes_schema.notes WHERE to_tsvector(name) || to_tsvector(note) @@ to_tsquery(?)";
        List<Note> notes = jdbcTemplate.query(sql, new String[]{word}, new NoteMapper());
        return parseListNoteToDto(notes);
    }

    @Override
    public List<NoteDto> listNotes() {
        String sql = "select * from notes_schema.notes";
        List<Note> notes = jdbcTemplate.query(sql, new NoteMapper());
        return parseListNoteToDto(notes);
    }

    public List<NoteDto> parseListNoteToDto(List<Note> notes){
        return notes.stream()
                .map(note -> NoteDto.builder()
                        .name(note.getName())
                        .note(note.getNote())
                        .creationDate(note.getCreationDate())
                        .build())
                .collect(Collectors.toList());
    }


}
