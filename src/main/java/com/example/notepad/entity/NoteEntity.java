package com.example.notepad.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class NoteEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;
    private String content;
    private LocalDate createDate;
    private LocalDate lastModificationDate;

    @PrePersist
    private void onCreate() {
        createDate = LocalDate.now();
        lastModificationDate = LocalDate.now();
    }

    @PreUpdate
    private void onUpdate() {
        lastModificationDate = LocalDate.now();
    }
}
