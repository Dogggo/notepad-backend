package com.example.notepad.dto;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Data
public class NoteDto {
    private Long id;
    @NotEmpty
    @Size(max = 200, message = "Note has to contain from 0 to 200 characters")
    private String content;
}
