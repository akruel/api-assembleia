package br.com.sicredi.assembleia.v1.dto.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@ApiModel("Informação que retorna após criar uma pauta ou buscar pautas")
public class AgendaResponse {
    @ApiModelProperty("ID da pauta")
    private Long id;
    @ApiModelProperty("Nome da pauta")
    private String name;
    @ApiModelProperty("Descrição da pauta")
    private String description;
    @ApiModelProperty("Status da sessão da pauta (ATIVA/INATIVA)")
    private String sessionStatus;
}