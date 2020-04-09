package br.com.sicredi.assembleia.v1.dto.response;

import lombok.Builder;
import lombok.Data;
import lombok.experimental.Tolerate;

@Data
@Builder
public class SessionResponse {
    @Tolerate
    public SessionResponse() {
        // Construtor vazio por necessidade do JacksonMapper
    }

    int votesTotal;
    int votesYes;
    int votesNo;
    String result;
}