package br.com.sicredi.assembleia.exception;

public class UnableToVoteException extends RuntimeException {
    /**
     *
     */
    private static final long serialVersionUID = 1L;

    public UnableToVoteException(String message) {
        super(message);
    }
}