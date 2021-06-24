package com.starakogev.notes.service;

import com.starakogev.notes.dto.HashtagDto;
import com.starakogev.notes.entity.Hashtag;
import com.starakogev.notes.jdbc.HashtagMapper;
import com.starakogev.notes.jdbc.NoteDAO;
import com.starakogev.notes.jdbc.NoteMapper;
import com.starakogev.notes.entity.Note;
import com.starakogev.notes.dto.NoteDto;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class NoteJDBCTemplateService implements NoteDAO {

    private final JdbcTemplate jdbcTemplate;

    /**
     * Create new note to DataBase
     *
     * @param note
     * @return true if operation successfully
     */
    @Override
    @Transactional
    public boolean create(NoteDto note) {
        String sqlHashtag = "insert into notes_schema.hashtag (hashtag) values (?) on conflict do nothing";
        note.getHashtag().forEach(hashtag -> jdbcTemplate.update(sqlHashtag, hashtag));
        String sql = "insert into notes_schema.notes (name, note, creation_date) values (?, ?, ?) returning *";
        Note noteFromTable = jdbcTemplate.queryForObject(sql, new NoteMapper(), note.getName(), note.getNote(),
                Timestamp.valueOf(LocalDateTime.now()));
        String sqlHashtagNote = "insert into notes_schema.note_hashtag values (?, (select id from notes_schema.hashtag where hashtag.hashtag = ?))";
        note.getHashtag().forEach(hashtag -> jdbcTemplate.update(sqlHashtagNote, noteFromTable.getId(), hashtag));
        return true;
    }

    /**
     * Find notes in Data Base by 'hashtag'
     *
     * @param hashtag
     * @return List of NoteDto
     */
    @Override
    public List<NoteDto> getNotesByHashtag(String hashtag) {
        String sql = "select * from notes_schema.notes n join notes_schema.note_hashtag nh on n.id = nh.note_id join notes_schema.hashtag h on nh.hashtag_id = h.id  where h.hashtag = ?";
        List<Note> notes = jdbcTemplate.query(sql, new String[]{hashtag}, new NoteMapper());
        return parseListNoteToDto(notes);
    }

    /**
     * Delete note from Data Base by name
     *
     * @param name
     * @return true if operation successfully
     */
    @Override
    public boolean delete(String name) {
        String sql = "delete from notes_schema.notes where name = ?";
        jdbcTemplate.update(sql, name);
        return true;
    }

    /**
     * Update text of note in Data Base by name
     *
     * @param name
     * @param note
     * @return true if operation successfully
     */
    @Override
    public boolean update(String name, String note) {
        String sql = "update notes_schema.notes set note = ? where name = ?";
        jdbcTemplate.update(sql, note, name);
        return true;
    }

    /**
     * Find notes in Data Base by time params
     *
     * @param min
     * @param max
     * @return List of NoteDto
     */
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

    /**
     * Find notes in Data Base by key words
     *
     * @param text
     * @return List of NoteDto
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
     *
     * @return List of NoteDto
     */
    @Override
    public List<NoteDto> listNotes() {
        String sql = "select * from notes_schema.notes";
        List<Note> notes = jdbcTemplate.query(sql, new NoteMapper());
        return parseListNoteToDto(notes);
    }

    /**
     * Take all hashtags of note
     * @param note
     * @return List of Tags
     */
    public List<String> takeHashtags(Note note) {
        String sql = "select hashtag from notes_schema.notes n join notes_schema.note_hashtag nh on n.id = nh.note_id join notes_schema.hashtag h on nh.hashtag_id = h.id  where n.id = ?";
        return jdbcTemplate.query(sql, new Long[]{note.getId()}, new HashtagMapper());
    }

    /**
     * Parse Note entity to NoteDto for user interface
     *
     * @param notes
     * @return List of NoteDto
     */
    public List<NoteDto> parseListNoteToDto(List<Note> notes) {
        return notes.stream()
                .map(note -> NoteDto.builder()
                        .name(note.getName())
                        .note(note.getNote())
                        .creationDate(note.getCreationDate())
                        .hashtag(takeHashtags(note))
                        .build())
                .collect(Collectors.toList());
    }


}
