package br.com.sicredi.assembleia.v1.dto;

import lombok.Builder;
import lombok.Data;
import lombok.experimental.Tolerate;

@Data
@Builder
public class AssociadoDTO {
    @Tolerate
    public AssociadoDTO() {}

    private String cpf;
    private String nome;
}