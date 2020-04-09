package br.com.sicredi.assembleia.v1.dto.request;

import lombok.Data;

@Data
public class AgendaRequest {
    private String name;
    private String description;
}