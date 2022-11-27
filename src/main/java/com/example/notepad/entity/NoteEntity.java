package com.example.notepad.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Entity;
import org.springframework.data.annotation.Id;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
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
