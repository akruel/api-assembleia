package br.com.sicredi.assembleia.model;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;

import lombok.Builder;
import lombok.Data;
import lombok.experimental.Tolerate;

@Entity
@Data
@Builder
public class Vote {

    @Tolerate
    public Vote() {
        // Construtor vazio por necessidade do JPA
    }

    @EmbeddedId
    private VotePK pk;

    private boolean decision;
}