package br.com.sicredi.assembleia.exception.controller;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import br.com.sicredi.assembleia.exception.RecursoDuplicadoException;
import br.com.sicredi.assembleia.exception.RecursoNaoEncontradoException;

@RestControllerAdvice
public class AssociadoControllerAdvice extends ResponseEntityExceptionHandler {

    @ExceptionHandler(RecursoDuplicadoException.class)
    protected ResponseEntity<Object> handleConflict(RecursoDuplicadoException ex, WebRequest request) {
        return handleExceptionInternal(ex, ex.getMessage(), new HttpHeaders(), HttpStatus.CONFLICT, request);
    }

    @ExceptionHandler(RecursoNaoEncontradoException.class)
    protected ResponseEntity<Object> handleNotFound(RecursoNaoEncontradoException ex, WebRequest request) {
        return handleExceptionInternal(ex, ex.getMessage(), new HttpHeaders(), HttpStatus.NOT_FOUND, request);
    }
}