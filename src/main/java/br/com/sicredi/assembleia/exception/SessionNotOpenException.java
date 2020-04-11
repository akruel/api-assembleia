package br.com.sicredi.assembleia.exception;

public class SessionNotOpenException extends RuntimeException {
    /**
     *
     */
    private static final long serialVersionUID = 1L;

    public SessionNotOpenException(String message) {
        super(message);
    }
}