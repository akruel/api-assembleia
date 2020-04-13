package br.com.sicredi.assembleia.v1.dto.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.Tolerate;

@Data
@Builder
@ApiModel("Informação que retorna após criar um voto")
public class VoteResponse {
    @Tolerate
    public VoteResponse() {
        // Construtor vazio por necessidade do JacksonMapper
    }

    @ApiModelProperty("CPF do associado que votou")
    private String associated;
    @ApiModelProperty("Pauta em que o associado votou")
    private AgendaResponse agenda;
    @ApiModelProperty("Decisão do associado")
    private Boolean decision;
    @ApiModelProperty("Status do voto")
    private String status;
}
