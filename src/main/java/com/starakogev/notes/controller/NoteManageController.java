package com.starakogev.notes.controller;

import com.starakogev.notes.dto.NoteDto;
import com.starakogev.notes.service.NoteJDBCTemplateService;
import com.starakogev.notes.entity.Note;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class NoteManageController {

    private final NoteJDBCTemplateService noteJDBCTemplate;

    //создать заметку
    @PostMapping("create/note")
    public String createNote(@RequestBody NoteDto note) {
        noteJDBCTemplate.create(note);
        return "Создана заметка: " + "'" + note.getName() + "'";
    }

    //удалить заметку
    @DeleteMapping("delete/note/{name}")
    public String deleteNoteByName(@PathVariable String name) {
        noteJDBCTemplate.delete(name);
        return "Заметка с именем: " + "'" + name + "'" + " was deleted!";
    }

    //изменить заметку
    @PutMapping("update/note/{name}")
    public String updateNote(@PathVariable String name, @RequestBody String note) {
        noteJDBCTemplate.update(name, note);
        return "Заметка с именем: " + "'" + name + "'" + " изменена";
    }

    //отфильтровать заметки по дате создания
    @GetMapping("find/notes/by-time")
    public List<NoteDto> listNotesByTime(@RequestParam(required = false) String min,
                                         @RequestParam(required = false) String max) {
        return noteJDBCTemplate.listNotesByTime(min, max);
    }

    //найти заметки по ключевым словам в названии и в текстке заметки
    @GetMapping("find/notes/by-text")
    public List<NoteDto> listNotesByKeyWord(@RequestParam(required = false) String text) {
        return noteJDBCTemplate.getNoteByText(text);
    }

    //отфильтровать заметки по хэштэгу
    @GetMapping("find/notes/by-hashtag")
    public List<NoteDto> getNotesByHashTag(@RequestParam String hashtag) {
        return noteJDBCTemplate.getNotesByHashtag(hashtag);
    }
}
