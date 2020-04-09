package br.com.sicredi.assembleia.v1.dto.request;

import lombok.Data;

@Data
public class SessionRequest {
    private Long agendaID;
    private Long durationInMinutes;
}