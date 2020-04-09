package br.com.sicredi.assembleia.v1.dto.request;

import lombok.Data;

@Data
public class VoteRequest {
    private String associated;
    private Long agendaID;
    private Boolean decision;
}