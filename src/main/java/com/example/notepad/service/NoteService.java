package com.example.notepad.service;

import com.example.notepad.dto.NoteDto;
import com.example.notepad.entity.NoteEntity;
import com.example.notepad.exception.NotFoundNoteException;
import com.example.notepad.repository.NoteRepository;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class NoteService {

    private final NoteRepository noteRepository;
    private final ModelMapper modelMapper;

    public NoteService(NoteRepository noteRepository, ModelMapper modelMapper) {
        this.noteRepository = noteRepository;
        this.modelMapper = modelMapper;
    }

    public List<NoteDto> list() {
        log.info("list()");
        List<NoteDto> list = noteRepository.findAllByOrderByLastModificationDate().stream().map(n -> modelMapper.map(n, NoteDto.class)).collect(Collectors.toList());
        log.info("list() -> {}", list);
        return list;
    }

    public NoteDto create(NoteDto noteDto) {
        log.info("create({})", noteDto);
        NoteEntity noteEntity = modelMapper.map(noteDto, NoteEntity.class);
        noteEntity = noteRepository.save(noteEntity);
        NoteDto createdNoteDto = modelMapper.map(noteEntity, NoteDto.class);
        log.info("create({}) -> {}", noteDto, createdNoteDto);
        return createdNoteDto;
    }

    public NoteDto update(Long id, NoteDto noteDto) {
        log.info("update({}, {})", id, noteDto);
        if (!noteRepository.existsById(id)) {
            log.error("update({}, {}) -> note by id not found", id, noteDto);
            throw new NotFoundNoteException();
        }

        noteDto.setId(id);
        NoteEntity updatedNoteEntity = noteRepository.save(modelMapper.map(noteDto, NoteEntity.class));
        NoteDto updatedNoteDto = modelMapper.map(updatedNoteEntity, NoteDto.class);
        log.info("update({}, {}) -> {}", id, noteDto, updatedNoteDto);
        return updatedNoteDto;
    }

    public void delete(Long id) {
        log.info("delete({})", id);
        if (!noteRepository.existsById(id)) {
            log.error("delete({}) -> note by id not found", id);
            throw new NotFoundNoteException();
        }
        noteRepository.deleteById(id);
        log.info("delete({}) -> success", id);
    }

}
