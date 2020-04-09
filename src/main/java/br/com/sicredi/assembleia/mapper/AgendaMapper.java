package br.com.sicredi.assembleia.mapper;

import java.time.LocalDateTime;

import br.com.sicredi.assembleia.model.Agenda;
import br.com.sicredi.assembleia.v1.dto.request.AgendaRequest;
import br.com.sicredi.assembleia.v1.dto.request.SessionRequest;
import br.com.sicredi.assembleia.v1.dto.response.AgendaResponse;

public class AgendaMapper {

    private AgendaMapper() {
        throw new IllegalStateException("Utility class");
    }

    public static Agenda sessionToAgendaEntity(SessionRequest sessionRequest) {
        return Agenda.builder()
                     .id(sessionRequest.getAgendaID())
                     .build();
    }

    public static Agenda convertToEntity(AgendaRequest agendaRequest) {
        return Agenda.builder()
                     .name(agendaRequest.getName())
                     .description(agendaRequest.getDescription())
                     .build();
    }

    public static AgendaResponse convertToResponse(Agenda agenda){
        String sessionStatus = agenda.getSessionEnd() != null && LocalDateTime.now().isBefore(agenda.getSessionEnd()) ? "ATIVA" : "INATIVA";
        return AgendaResponse.builder()
                             .id(agenda.getId())
                             .name(agenda.getName())
                             .description(agenda.getDescription())
                             .sessionStatus(sessionStatus)
                             .build();
    }

}