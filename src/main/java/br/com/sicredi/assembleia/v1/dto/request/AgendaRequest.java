package br.com.sicredi.assembleia.v1.dto.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("Informações necessárias para criar uma pauta")
public class AgendaRequest {
    @ApiModelProperty("Nome da pauta")
    private String name;
    @ApiModelProperty("Descrição breve da pauta")
    private String description;
}