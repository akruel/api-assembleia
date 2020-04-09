package br.com.sicredi.assembleia.v1.dto.response;

import lombok.Builder;
import lombok.Data;
import lombok.experimental.Tolerate;

@Data
@Builder
public class VoteResponse {
    @Tolerate
    public VoteResponse() {
        // Construtor vazio por necessidade do JacksonMapper
    }

    private String associated;
    private AgendaResponse agenda;
    private Boolean decision;
}
