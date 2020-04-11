package br.com.sicredi.assembleia;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.hamcrest.core.Is;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import br.com.sicredi.assembleia.model.Agenda;
import br.com.sicredi.assembleia.model.Vote;
import br.com.sicredi.assembleia.model.VotePK;
import br.com.sicredi.assembleia.repository.VoteRepository;
import br.com.sicredi.assembleia.service.AgendaService;
import br.com.sicredi.assembleia.v1.dto.request.AgendaRequest;
import br.com.sicredi.assembleia.v1.dto.request.SessionRequest;
import br.com.sicredi.assembleia.v1.dto.response.AgendaResponse;
import br.com.sicredi.assembleia.v1.dto.response.SessionResponse;
import br.com.sicredi.assembleia.v1.mapper.AgendaMapper;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
class AssembleiaApplicationTests {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@Autowired
	private AgendaService agendaService;

	@Autowired
	private VoteRepository voteRepository;

	@Test
	void casePostAgenda() throws JsonProcessingException, Exception {

		mockMvc.perform(post("/v1/agendas")
			   .contentType("application/json")
			   .content(objectMapper.writeValueAsString(makeAgendaRequest())))
			   .andExpect(status().isCreated());

		AgendaResponse agendaResponse = AgendaMapper.convertToResponse(agendaService.findById(Long.valueOf(1)));

		Assertions.assertEquals(Long.valueOf(1), agendaResponse.getId());
		Assertions.assertEquals("Teste", agendaResponse.getName());
		Assertions.assertEquals("Esta é uma pauta de teste", agendaResponse.getDescription());
	}

	@Test
	void caseGetAgendas() throws JsonProcessingException, Exception {

		AgendaResponse agendaResponse = AgendaResponse.builder()
													  .id(Long.valueOf(2))
													  .name("Teste")
													  .description("Esta é uma pauta de teste")
													  .sessionStatus("INATIVA")
													  .build();

		agendaService.save(AgendaMapper.convertToEntity(makeAgendaRequest()));

		mockMvc.perform(get("/v1/agendas")
			   .contentType(MediaType.APPLICATION_JSON))
			   .andExpect(status().isOk())
			   .andExpect(jsonPath("$[1].id", Is.is(agendaResponse.getId().intValue())))
			   .andExpect(jsonPath("$[1].name", Is.is(agendaResponse.getName())))
			   .andExpect(jsonPath("$[1].description", Is.is(agendaResponse.getDescription())))
			   .andExpect(jsonPath("$[1].sessionStatus", Is.is(agendaResponse.getSessionStatus())));
	}

	@Test
	void caseGetAgendaById() throws JsonProcessingException, Exception {

		AgendaResponse agendaResponse = AgendaResponse.builder()
													  .id(Long.valueOf(1))
													  .name("Teste")
													  .description("Esta é uma pauta de teste")
													  .sessionStatus("INATIVA")
													  .build();

		agendaService.save(AgendaMapper.convertToEntity(makeAgendaRequest()));

		mockMvc.perform(get("/v1/agendas/1")
			   .contentType(MediaType.APPLICATION_JSON))
			   .andExpect(status().isOk())
			   .andExpect(jsonPath("$.id", Is.is(agendaResponse.getId().intValue())))
			   .andExpect(jsonPath("$.name", Is.is(agendaResponse.getName())))
			   .andExpect(jsonPath("$.description", Is.is(agendaResponse.getDescription())))
			   .andExpect(jsonPath("$.sessionStatus", Is.is(agendaResponse.getSessionStatus())));
	}

	
	@Test
	void caseOpenSession() throws JsonProcessingException, Exception {
		SessionRequest sessionRequest = new SessionRequest();
		sessionRequest.setAgendaID(1L);

		AgendaResponse agendaResponse = AgendaResponse.builder()
													  .id(Long.valueOf(1))
													  .name("Teste")
													  .description("Esta é uma pauta de teste")
													  .sessionStatus("ATIVA")
													  .build();


		mockMvc.perform(put("/v1/agendas/openSession")
			   .contentType(MediaType.APPLICATION_JSON)
			   .content(objectMapper.writeValueAsString(sessionRequest)))
			   .andExpect(status().isNoContent())
			   .andExpect(jsonPath("$.id", Is.is(agendaResponse.getId().intValue())))
			   .andExpect(jsonPath("$.name", Is.is(agendaResponse.getName())))
			   .andExpect(jsonPath("$.description", Is.is(agendaResponse.getDescription())))
			   .andExpect(jsonPath("$.sessionStatus", Is.is(agendaResponse.getSessionStatus())));
	}

	@Test
	void caseResult() throws JsonProcessingException, Exception {	

		VotePK pk = VotePK.builder()
						  .agenda(makeAgenda(1L))
						  .associated("10020030040")
						  .build();

		Vote vote = Vote.builder()
						.pk(pk)
						.decision(false)
						.build();	

		voteRepository.save(vote);
		
		SessionResponse sessionResponse = new SessionResponse();

		sessionResponse.setResult("NO");
		sessionResponse.setVotesNo(1);
		sessionResponse.setVotesYes(0);
		sessionResponse.setVotesTotal(1);
									
		mockMvc.perform(get("/v1/votes/sessionResult/1")
			   .contentType(MediaType.APPLICATION_JSON))
			   .andExpect(status().isOk())
			   .andExpect(jsonPath("$.result", Is.is(sessionResponse.getResult())))
			   .andExpect(jsonPath("$.votesNo", Is.is(sessionResponse.getVotesNo())))
			   .andExpect(jsonPath("$.votesYes", Is.is(sessionResponse.getVotesYes())))
			   .andExpect(jsonPath("$.votesTotal", Is.is(sessionResponse.getVotesTotal())));
	}

	private Agenda makeAgenda(Long id) {
		return Agenda.builder()
					 .id(id)
					 .build();
					  
	}

	private AgendaRequest makeAgendaRequest() {
		AgendaRequest agendaRequest = new AgendaRequest();
		agendaRequest.setName("Teste");
		agendaRequest.setDescription("Esta é uma pauta de teste");

		return agendaRequest;
	}

}
