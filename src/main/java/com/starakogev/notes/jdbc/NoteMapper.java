package com.starakogev.notes.jdbc;

import com.starakogev.notes.entity.Note;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class NoteMapper implements RowMapper<Note> {
    @Override
    public Note mapRow(ResultSet resultSet, int i) throws SQLException {
        Note note = new Note();
        note.setId(resultSet.getLong("id"));
        note.setName(resultSet.getString("name"));
        note.setNote(resultSet.getString("note"));
        note.setCreationDate(resultSet.getTimestamp("creation_date"));
        note.setHashtag(resultSet.getString("hashtag"));

        return note;
    }
}
