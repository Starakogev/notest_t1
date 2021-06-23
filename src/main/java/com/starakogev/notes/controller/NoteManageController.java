package com.starakogev.notes.controller;

import com.starakogev.notes.dto.NoteDto;
import com.starakogev.notes.service.NoteJDBCTemplateService;
import com.starakogev.notes.entity.Note;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class NoteManageController {

    private final NoteJDBCTemplateService noteJDBCTemplate;

    @PostMapping("create/note")
    public String createNote(@RequestBody Note note) {
        return noteJDBCTemplate.create(note);
    }

    @DeleteMapping("delete/note/{name}")
    public String deleteNoteByName(@PathVariable String name) {
        return noteJDBCTemplate.delete(name);
    }

    @PutMapping("update/note/{name}")
    public String updateNote(@PathVariable String name, @RequestBody String note) {
        return noteJDBCTemplate.update(name, note);
    }

    @GetMapping("find/notes/by-time")
    public List<NoteDto> listNotesByTime(@RequestParam(required = false) String min,
                                         @RequestParam(required = false) String max) {
        return noteJDBCTemplate.listNotesByTime(min, max);
    }

    @GetMapping("find/notes/by-text")
    public List<NoteDto> listNotesByKeyWord(@RequestParam(required = false) String text) {
        return noteJDBCTemplate.getNoteByText(text);
    }

    @GetMapping("find/notes/by-hashtag")
    public NoteDto getNoteByHashTag(@PathVariable String hashtag) {
        return noteJDBCTemplate.getNoteByHashtag(hashtag);
    }
}
