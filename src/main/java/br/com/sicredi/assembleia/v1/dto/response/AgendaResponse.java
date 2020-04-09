package br.com.sicredi.assembleia.v1.dto.response;

import lombok.Data;

@Data
public class AgendaResponse {
    private String id;
    private String name;
    private String description;
    private String sessionStatus;
}