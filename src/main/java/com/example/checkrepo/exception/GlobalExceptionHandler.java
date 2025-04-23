package com.example.checkrepo.exception;

import java.util.Date;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(ObjectNotFoundException.class)
    public ResponseEntity<ErrorObject> handleNotFound(ObjectNotFoundException except) {
        ErrorObject error = new ErrorObject();
        error.setStatusCode(HttpStatus.NOT_FOUND.value());
        error.setMessage(except.getMessage());
        error.setTimestamp(new Date());
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(IncorrectInputException.class)
    public ResponseEntity<ErrorObject> incorrectInputHandler(IncorrectInputException incorrect) {
        ErrorObject error = new ErrorObject();
        error.setStatusCode(HttpStatus.BAD_REQUEST.value());
        error.setMessage(incorrect.getMessage());
        error.setTimestamp(new Date());
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(FileNotFound.class)
    public ResponseEntity<ErrorObject> fileNotCreated(ObjectNotFoundException except) {
        ErrorObject error = new ErrorObject();
        error.setStatusCode(HttpStatus.BAD_REQUEST.value());
        error.setMessage(except.getMessage());
        error.setTimestamp(new Date());
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }
}
