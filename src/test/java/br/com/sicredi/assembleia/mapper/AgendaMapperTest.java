package br.com.sicredi.assembleia.mapper;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import br.com.sicredi.assembleia.model.Agenda;
import br.com.sicredi.assembleia.v1.dto.request.AgendaRequest;
import br.com.sicredi.assembleia.v1.dto.request.SessionRequest;
import br.com.sicredi.assembleia.v1.dto.response.AgendaResponse;
import br.com.sicredi.assembleia.v1.mapper.AgendaMapper;

public class AgendaMapperTest {
    @Test
    void sessionToAgendaEntity() {
        SessionRequest sessionRequest = makeSessionRequest();

        Agenda agenda = AgendaMapper.sessionToAgendaEntity(sessionRequest);
        
        Assertions.assertEquals(sessionRequest.getAgendaID(), agenda.getId());
    }

    @Test
    void convertToEntity() {
        AgendaRequest agendaRequest = makeAgendaRequest();

        Agenda agenda = AgendaMapper.convertToEntity(agendaRequest);
        
        Assertions.assertEquals(agendaRequest.getName(), agenda.getName());
        Assertions.assertEquals(agendaRequest.getDescription(), agenda.getDescription());
        Assertions.assertNull(agenda.getId());
        Assertions.assertNull(agenda.getSessionStart());
        Assertions.assertNull(agenda.getSessionEnd());
    }

    @Test
    void convertToResponseWithoutSession() {
        Agenda agenda = makeAgendaWithouSession(1L);

        AgendaResponse agendaResponse = AgendaMapper.convertToResponse(agenda);
        
        Assertions.assertEquals(agenda.getId(), agendaResponse.getId());
        Assertions.assertEquals(agenda.getName(), agenda.getName());
        Assertions.assertEquals(agenda.getDescription(), agendaResponse.getDescription());
        Assertions.assertEquals("INATIVA", agendaResponse.getSessionStatus());
    }

    @Test
    void convertToResponseWithSession() {
        Agenda agenda = makeAgendaWithSession(1L);

        AgendaResponse agendaResponse = AgendaMapper.convertToResponse(agenda);
        
        Assertions.assertEquals(agenda.getId(), agendaResponse.getId());
        Assertions.assertEquals(agenda.getName(), agenda.getName());
        Assertions.assertEquals(agenda.getDescription(), agendaResponse.getDescription());
        Assertions.assertEquals("ATIVA", agendaResponse.getSessionStatus());
    }

    private SessionRequest makeSessionRequest() {
        SessionRequest sessionRequest = new SessionRequest();
        sessionRequest.setAgendaID(1L);
        return sessionRequest;
    }

	private AgendaRequest makeAgendaRequest() {
		AgendaRequest agendaRequest = new AgendaRequest();
		agendaRequest.setName("Teste");
		agendaRequest.setDescription("Esta é uma pauta de teste");

		return agendaRequest;
    }
    
    private Agenda makeAgendaWithouSession(Long id) {
		return Agenda.builder()
							 .id(id)
							 .name("Teste")
							 .description("Esta é uma pauta de teste")
							 .build();
    }
    
    private Agenda makeAgendaWithSession(Long id) {
		return Agenda.builder()
							 .id(id)
							 .name("Teste")
                             .description("Esta é uma pauta de teste")
                             .sessionStart(LocalDateTime.now())
                             .sessionEnd(LocalDateTime.now().plusMinutes(1L))
							 .build();
	}

}