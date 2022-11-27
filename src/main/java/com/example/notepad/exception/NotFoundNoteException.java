package com.example.notepad.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "Note not found")
public class NotFoundNoteException extends RuntimeException{

}
