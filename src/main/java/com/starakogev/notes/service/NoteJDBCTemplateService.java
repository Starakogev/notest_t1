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

    /**
     * Create new note to DataBase
     * @param note
     * @return String(message of operation)
     */
    @Override
    public String create(Note note) {
        String sql = "insert into notes_schema.notes (name, note, creation_date, hashtag) values (?, ?, ?, ?)";
        jdbcTemplate.update(sql, note.getName(), note.getNote(), Timestamp.valueOf(LocalDateTime.now()),
                note.getHashtag());
        return "Создана заметка: " + "'" + note.getName() + "'";
    }

    /**
     * Find notes in Data Base by 'hashtag'
     * @param hashtag
     * @return List</NoteDto>
     */
    @Override
    public List<NoteDto> getNotesByHashtag(String hashtag) {
        String sql = "select * from notes_schema.notes where hashtag = ?";
        List<Note> notes = jdbcTemplate.query(sql, new String[]{hashtag}, new NoteMapper());
        return parseListNoteToDto(notes);
    }

    /**
     * Delete note from Data Base
     * @param name
     * @return String(message of operation)
     */
    @Override
    public String delete(String name) {
        String sql = "delete from notes_schema.notes where name = ?";
        jdbcTemplate.update(sql, name);
        return "Заметка с именем: " + "'" + name + "'" + " was deleted!";
    }

    /**
     * Update note in Data Base
     * @param name
     * @param note
     * @return String(message of operation)
     */
    @Override
    public String update(String name, String note) {
        String sql = "update notes_schema.notes set note = ? where name = ?";
        jdbcTemplate.update(sql, note, name);
        return "Заметка с именем: " + "'" + name + "'" + " изменена";
    }

    /**
     * Find notes in Data Base by time params
     * @param min
     * @param max
     * @return List</NoteDto>
     */
    @Override
    public List<NoteDto> listNotesByTime(String min, String max) {
        LocalDateTime minTime = LocalDateTime.parse(min);
        LocalDateTime maxTime = LocalDateTime.parse(max);
        if (min == null && max == null) {
            return listNotes();
        } else if (min != null && max == null) {
            String sql = "select * from notes_schema.notes where creation_date > ?";
            List<Note> notes = jdbcTemplate.query(sql, new LocalDateTime[]{minTime}, new NoteMapper());
            return parseListNoteToDto(notes);
        } else if (min == null) {
            String sql = "select * from notes_schema.notes where creation_date < ?";
            List<Note> notes = jdbcTemplate.query(sql, new LocalDateTime[]{maxTime}, new NoteMapper());
            return parseListNoteToDto(notes);
        } else {
            String sql = "select * from notes_schema.notes where creation_date > ? and creation_date < ?";
            List<Note> notes = jdbcTemplate.query(sql, new LocalDateTime[]{minTime, maxTime}, new NoteMapper());
            return parseListNoteToDto(notes);
        }
    }

    /**
     * Find notes in Data Base by key words
     * @param text
     * @return List</NoteDto>
     */
    @Override
    public List<NoteDto> getNoteByText(String text) {
        String word = text + ":*";
        String sql = "SELECT * FROM notes_schema.notes WHERE to_tsvector(name) || to_tsvector(note) @@ to_tsquery(?)";
        List<Note> notes = jdbcTemplate.query(sql, new String[]{word}, new NoteMapper());
        return parseListNoteToDto(notes);
    }

    /**
     * Return all notes from Data Base
     * @return List</NoteDto>
     */
    @Override
    public List<NoteDto> listNotes() {
        String sql = "select * from notes_schema.notes";
        List<Note> notes = jdbcTemplate.query(sql, new NoteMapper());
        return parseListNoteToDto(notes);
    }

    /**
     * Parse Note entity to NoteDto for user interface
     * @param notes
     * @return List</NoteDto>
     */
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
