package com.example.notepad.controller;

import com.example.notepad.dto.NoteDto;
import com.example.notepad.service.NoteService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@Slf4j
@RequestMapping("/api/note")
@CrossOrigin(origins = "http://localhost:4200")
public class NoteController {

    private final NoteService noteService;

    public NoteController(NoteService noteService) {
        this.noteService = noteService;
    }

    @GetMapping
    public List<NoteDto> listOfNotes() {
        log.info("listOfNotes() called");
        return noteService.list();
    }

    @PostMapping
    public NoteDto createNote(@Valid @RequestBody NoteDto noteDto) {
        log.info("createNote({}) called", noteDto);
        return noteService.create(noteDto);
    }

    @PutMapping("/{id}")
    public NoteDto updateNote(@PathVariable Long id, @Valid @RequestBody NoteDto noteDto) {
        log.info("update({}, {}) called", id, noteDto);
        return this.noteService.update(id, noteDto);
    }

    @DeleteMapping("/{id}")
    public void deleteNote(@PathVariable Long id) {
        log.info("deleteNote({}) called", id);
        this.noteService.delete(id);
    }
}
