package br.com.sicredi.assembleia.v1.dto.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.Tolerate;

@Data
@Builder
@ApiModel("Informação do resultado da votação")
public class SessionResponse {
    @Tolerate
    public SessionResponse() {
        // Construtor vazio por necessidade do JacksonMapper
    }

    @ApiModelProperty("Total de votos computados para esta pauta")
    int votesTotal;
    @ApiModelProperty("Total de votos SIM para esta pauta")
    int votesYes;
    @ApiModelProperty("Total de votos NÃO para esta pauta")
    int votesNo;
    @ApiModelProperty("Resultado da votação")
    String result;
}