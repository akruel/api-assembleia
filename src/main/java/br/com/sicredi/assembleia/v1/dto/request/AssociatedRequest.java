package br.com.sicredi.assembleia.v1.dto.request;

import lombok.Data;

@Data
public class AssociatedRequest {
    private String cpf;
    private String name;
}