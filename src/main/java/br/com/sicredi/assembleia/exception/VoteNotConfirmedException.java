package br.com.sicredi.assembleia.exception;

public class VoteNotConfirmedException extends RuntimeException {
    /**
     *
     */
    private static final long serialVersionUID = 1L;

    public VoteNotConfirmedException() {
        super("Voto foi salvo, porém não foi confirmado!");
    }
}