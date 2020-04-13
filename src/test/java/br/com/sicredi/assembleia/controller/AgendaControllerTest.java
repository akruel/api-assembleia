package br.com.sicredi.assembleia.controller;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.hamcrest.core.Is;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import br.com.sicredi.assembleia.exception.ResourceNotFoundException;
import br.com.sicredi.assembleia.model.Agenda;
import br.com.sicredi.assembleia.service.AgendaService;
import br.com.sicredi.assembleia.v1.controller.AgendaController;
import br.com.sicredi.assembleia.v1.dto.request.AgendaRequest;
import br.com.sicredi.assembleia.v1.dto.request.SessionRequest;
import br.com.sicredi.assembleia.v1.dto.response.AgendaResponse;
import br.com.sicredi.assembleia.v1.mapper.AgendaMapper;

@ExtendWith(SpringExtension.class)
@WebMvcTest(AgendaController.class)
class AgendaControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@MockBean
	private AgendaService agendaService;
	
	@Test
	void postAgenda() throws JsonProcessingException, Exception {

		Mockito.when(agendaService.save(makeAgenda(null))).thenReturn(makeAgenda(1L));

		AgendaResponse agendaResponse = AgendaMapper.convertToResponse(makeAgenda(1L));

		mockMvc.perform(post("/v1/agendas")
			   .contentType("application/json")
			   .content(objectMapper.writeValueAsString(makeAgendaRequest())))
			   .andExpect(status().isCreated())
			   .andExpect(jsonPath("$.id", Is.is(agendaResponse.getId().intValue())))
			   .andExpect(jsonPath("$.name", Is.is(agendaResponse.getName())))
			   .andExpect(jsonPath("$.description", Is.is(agendaResponse.getDescription())))
			   .andExpect(jsonPath("$.sessionStatus", Is.is(agendaResponse.getSessionStatus())));

		Mockito.verify(agendaService, times(1)).save(makeAgenda(null));
		Mockito.verifyNoMoreInteractions(agendaService);
	}

	@Test
	void getAgendas() throws JsonProcessingException, Exception {

		Agenda agenda1 = makeAgenda(1L);
		Agenda agenda2 = makeAgenda(2L);
		List<Agenda> agendas = Arrays.asList(agenda1, agenda2);
		AgendaResponse agendaResponse1 = makeAgendaResponse(1, "INATIVA");
		AgendaResponse agendaResponse2 = makeAgendaResponse(2, "INATIVA");

		Mockito.when(agendaService.findAll()).thenReturn(agendas);

		mockMvc.perform(get("/v1/agendas")
			   .contentType(MediaType.APPLICATION_JSON))
			   .andExpect(status().isOk())
			   .andExpect(jsonPath("$[0].id", Is.is(agendaResponse1.getId().intValue())))
			   .andExpect(jsonPath("$[0].name", Is.is(agendaResponse1.getName())))
			   .andExpect(jsonPath("$[0].description", Is.is(agendaResponse1.getDescription())))
			   .andExpect(jsonPath("$[0].sessionStatus", Is.is(agendaResponse1.getSessionStatus())))
			   .andExpect(jsonPath("$[1].id", Is.is(agendaResponse2.getId().intValue())))
			   .andExpect(jsonPath("$[1].name", Is.is(agendaResponse2.getName())))
			   .andExpect(jsonPath("$[1].description", Is.is(agendaResponse2.getDescription())))
			   .andExpect(jsonPath("$[1].sessionStatus", Is.is(agendaResponse2.getSessionStatus())));
	
			   Mockito.verify(agendaService, times(1)).findAll();
			   Mockito.verifyNoMoreInteractions(agendaService);		
	}

	@Test
	void getAgendaById() throws JsonProcessingException, Exception {

		Agenda agenda = makeAgenda(1L);

		when(agendaService.findById(anyLong())).thenReturn(agenda);

		AgendaResponse agendaResponse = AgendaMapper.convertToResponse(agenda);

		mockMvc.perform(get("/v1/agendas/1")
			   .contentType(MediaType.APPLICATION_JSON))
			   .andExpect(status().isOk())
			   .andExpect(jsonPath("$.id", Is.is(agendaResponse.getId().intValue())))
			   .andExpect(jsonPath("$.name", Is.is(agendaResponse.getName())))
			   .andExpect(jsonPath("$.description", Is.is(agendaResponse.getDescription())))
			   .andExpect(jsonPath("$.sessionStatus", Is.is(agendaResponse.getSessionStatus())));

		Mockito.verify(agendaService, times(1)).findById(makeAgenda(1L).getId());
		Mockito.verifyNoMoreInteractions(agendaService);
	}

	@Test
	void getAgendaByIdWith404() throws JsonProcessingException, Exception {
		when(agendaService.findById(anyLong())).thenThrow(new ResourceNotFoundException(""));

		mockMvc.perform(get("/v1/agendas/1")
			   .contentType(MediaType.APPLICATION_JSON))
			   .andExpect(status().isNotFound());

		Mockito.verify(agendaService, times(1)).findById(makeAgenda(1L).getId());
		Mockito.verifyNoMoreInteractions(agendaService);
	}

	
	@Test
	void openSession() throws JsonProcessingException, Exception {
		SessionRequest sessionRequest = new SessionRequest();
		sessionRequest.setAgendaID(1L);

		Mockito.when(agendaService.findById(makeAgenda(1L).getId())).thenReturn(makeAgenda(1L));
		Mockito.when(agendaService.openSession(makeAgenda(1L).getId(), null)).thenReturn(makeAgendaWithSession(1));
		mockMvc.perform(put("/v1/agendas/openSession")
			   .contentType(MediaType.APPLICATION_JSON)
			   .content(objectMapper.writeValueAsString(sessionRequest)))
			   .andExpect(status().isNoContent());
		
		Mockito.verify(agendaService, times(1)).openSession(1L, null);
		Mockito.verifyNoMoreInteractions(agendaService);
	}

	@Test
	void openSessionWithAgendaNotFound() throws JsonProcessingException, Exception {
		SessionRequest sessionRequest = new SessionRequest();
		sessionRequest.setAgendaID(1L);

		Mockito.when(agendaService.openSession(1L, null)).thenThrow(new ResourceNotFoundException(""));
		mockMvc.perform(put("/v1/agendas/openSession")
			   .contentType(MediaType.APPLICATION_JSON)
			   .content(objectMapper.writeValueAsString(sessionRequest)))
			   .andExpect(status().isNotFound());
		
		Mockito.verify(agendaService, times(1)).openSession(1L, null);
		Mockito.verifyNoMoreInteractions(agendaService);
	}

	private AgendaRequest makeAgendaRequest() {
		AgendaRequest agendaRequest = new AgendaRequest();
		agendaRequest.setName("Teste");
		agendaRequest.setDescription("Esta é uma pauta de teste");

		return agendaRequest;
	}

	private AgendaResponse makeAgendaResponse(int id, String status) {
		return AgendaResponse.builder()
							 .id(Long.valueOf(id))
							 .name("Teste")
							 .description("Esta é uma pauta de teste")
							 .sessionStatus(status)
							 .build();
	}

	private Agenda makeAgenda(Long id) {
		return Agenda.builder()
							 .id(id)
							 .name("Teste")
							 .description("Esta é uma pauta de teste")
							 .build();
	}

	private Agenda makeAgendaWithSession(int id) {
		return Agenda.builder()
					 .id(Long.valueOf(id))
					 .name("Teste")
					 .description("Esta é uma pauta de teste")
					 .sessionStart(LocalDateTime.now())
					 .sessionEnd(LocalDateTime.now().plusMinutes(1))
					 .build();
	}
}
