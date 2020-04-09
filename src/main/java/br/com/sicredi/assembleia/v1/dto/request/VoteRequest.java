package br.com.sicredi.assembleia.v1.dto.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("Informações necessárias para votar")
public class VoteRequest {
    @ApiModelProperty("CPF do associado que irá votar")
    private String associated;
    @ApiModelProperty("ID da pauta em que o associado está votando")
    private Long agendaID;
    @ApiModelProperty("Decisão do associado sobre essa pauta (SIM/NÃO)")
    private Boolean decision;
}