package com.starakogev.notes.jdbc;

import com.starakogev.notes.entity.Note;
import com.starakogev.notes.dto.NoteDto;


import java.time.LocalDateTime;
import java.util.List;

public interface NoteDAO {
    boolean create(NoteDto note);
    List<NoteDto> getNotesByHashtag(String hashtag);
    List<NoteDto> listNotes();
    boolean delete(String name);
    boolean update(String name, String note);
    List<NoteDto> listNotesByTime(String min, String max);
    List<NoteDto> getNoteByText(String text);
}
