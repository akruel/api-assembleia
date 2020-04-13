package br.com.sicredi.assembleia.exception;

import org.springframework.http.HttpStatus;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ExceptionResponse {
    private int status;
    private String error;
    private String message;

    public ExceptionResponse(Exception ex, HttpStatus status) {
        this.status = status.value();
        this.error = status.getReasonPhrase();
        this.message = ex.getMessage();
    }
}