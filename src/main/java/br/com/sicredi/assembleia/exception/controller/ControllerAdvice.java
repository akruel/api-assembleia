package br.com.sicredi.assembleia.exception.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import br.com.sicredi.assembleia.exception.ExceptionResponse;
import br.com.sicredi.assembleia.exception.ResourceDuplicatedException;
import br.com.sicredi.assembleia.exception.ResourceNotFoundException;
import br.com.sicredi.assembleia.exception.SessionNotOpenException;
import br.com.sicredi.assembleia.exception.UnableToVoteException;

@RestControllerAdvice
public class ControllerAdvice extends ResponseEntityExceptionHandler {

    @ExceptionHandler(ResourceDuplicatedException.class)
    protected ResponseEntity<Object> handleConflict(ResourceDuplicatedException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(new ExceptionResponse(ex, HttpStatus.CONFLICT));
    }

    @ExceptionHandler(SessionNotOpenException.class)
    protected ResponseEntity<Object> handleSessionNotOpenException(SessionNotOpenException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ExceptionResponse(ex, HttpStatus.BAD_REQUEST));
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    protected ResponseEntity<Object> handleNotFound(ResourceNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ExceptionResponse(ex, HttpStatus.NOT_FOUND));
    }

    @ExceptionHandler(UnableToVoteException.class)
    protected ResponseEntity<Object> handleUnableToVoteException(UnableToVoteException ex) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new ExceptionResponse(ex, HttpStatus.FORBIDDEN));
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
            HttpHeaders headers, HttpStatus status, WebRequest request) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return ResponseEntity.status(status).body(errors);
    }
}