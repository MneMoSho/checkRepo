package com.example.checkrepo.exception;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import lombok.extern.slf4j.Slf4j;
import org.apache.juli.logging.LogFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

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
        log.error("Exception thrown {}, {}", HttpStatus.BAD_REQUEST.value(),
                error.getTimestamp());
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ErrorObject> handleMethodArgumentTypeMismatch(MethodArgumentTypeMismatchException ex) {
        ErrorObject errorObject = new ErrorObject();
        errorObject.setTimestamp(new Date());
        errorObject.setStatusCode(HttpStatus.BAD_REQUEST.value());
        errorObject.setMessage("Invalid parameter type");
        errorObject.setMessage(String.format("Parameter '%s' should be of type %s",
                ex.getName(), ex.getRequiredType().getSimpleName()));
        log.error("Exception thrown {}, {}", HttpStatus.BAD_REQUEST.value(),
                errorObject.getTimestamp());
        return new ResponseEntity<>(errorObject, HttpStatus.BAD_REQUEST);
    }

}
