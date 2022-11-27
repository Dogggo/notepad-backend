package com.example.notepad.repository;

import com.example.notepad.entity.NoteEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NoteRepository extends JpaRepository<NoteEntity, Long> {

    List<NoteEntity> findAllByOrderByLastModificationDate();
}
