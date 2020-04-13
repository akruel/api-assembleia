package br.com.sicredi.assembleia.v1.dto.request;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.br.CPF;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("Informações necessárias para votar")
public class VoteRequest {
    @ApiModelProperty("CPF do associado que irá votar")
    @CPF
    private String associated;
    @ApiModelProperty("ID da pauta em que o associado está votando")
    @NotNull
    private Long agendaID;
    @ApiModelProperty("Decisão do associado sobre essa pauta (Sim/Não)")
    @Pattern(regexp = "(Sim|Não)")
    private String decision;
}