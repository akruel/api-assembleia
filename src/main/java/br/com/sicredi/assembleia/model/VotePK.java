package br.com.sicredi.assembleia.model;

import java.io.Serializable;

import javax.persistence.Embeddable;
import javax.persistence.ManyToOne;

import lombok.Builder;
import lombok.Data;
import lombok.experimental.Tolerate;

@Embeddable
@Data
@Builder
public class VotePK implements Serializable {
    /**
     *
     */
    private static final long serialVersionUID = 1L;

    @Tolerate
    public VotePK() {
        // Construtor vazio por necessidade do JPA
    }

    private String associated;

    @ManyToOne
    private Agenda agenda;
}