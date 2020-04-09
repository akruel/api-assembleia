package br.com.sicredi.assembleia.v1.dto.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("Informações necessárias para abrir uma sessão")
public class SessionRequest {
    @ApiModelProperty("ID da pauta que deseja abrir a sessão")
    private Long agendaID;
    @ApiModelProperty("Duração da sessão em minutos (opcional), caso não informado o padrão será de 1 minuto")
    private Long durationInMinutes;
}