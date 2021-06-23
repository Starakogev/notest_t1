package com.starakogev.notes.jdbc;

import com.starakogev.notes.entity.Note;
import com.starakogev.notes.dto.NoteDto;


import java.time.LocalDateTime;
import java.util.List;

public interface NoteDAO {
    String create(Note note);
    NoteDto getNoteByHashtag(String hashtag);
    List<NoteDto> listNotes();
    String delete(String name);
    String update(String name, String note);
    List<NoteDto> listNotesByTime(String min, String max);
    List<NoteDto> getNoteByText(String text);
}
