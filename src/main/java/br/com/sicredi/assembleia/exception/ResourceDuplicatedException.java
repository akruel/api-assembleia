package br.com.sicredi.assembleia.exception;

public class ResourceDuplicatedException extends RuntimeException {
    /**
     *
     */
    private static final long serialVersionUID = 1L;

    public ResourceDuplicatedException(String message) {
        super(message);
    }
}