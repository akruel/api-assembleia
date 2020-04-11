package br.com.sicredi.assembleia;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.hamcrest.core.Is;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import br.com.sicredi.assembleia.model.Agenda;
import br.com.sicredi.assembleia.repository.VoteRepository;
import br.com.sicredi.assembleia.service.AgendaService;
import br.com.sicredi.assembleia.v1.controller.AgendaController;
import br.com.sicredi.assembleia.v1.dto.request.AgendaRequest;
import br.com.sicredi.assembleia.v1.dto.request.SessionRequest;
import br.com.sicredi.assembleia.v1.dto.response.AgendaResponse;
import br.com.sicredi.assembleia.v1.mapper.AgendaMapper;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
class AssembleiaApplicationTests {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@InjectMocks
	private AgendaController agendaController;

	@Mock
	private AgendaService agendaService;

	@Mock
	private VoteRepository voteRepository;
		
    @BeforeEach
    public void init(){
        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders
                .standaloneSetup(agendaController)
                .build();
	}
	
	@Test
	void casePostAgenda() throws JsonProcessingException, Exception {

		Agenda agenda = makeAgenda(1);

		Mockito.when(agendaService.save(any())).thenReturn(agenda);

		AgendaResponse agendaResponse = AgendaMapper.convertToResponse(agenda);

		mockMvc.perform(post("/v1/agendas")
			   .contentType("application/json")
			   .content(objectMapper.writeValueAsString(makeAgendaRequest())))
			   .andExpect(status().isCreated())
			   .andExpect(jsonPath("$.id", Is.is(agendaResponse.getId().intValue())))
			   .andExpect(jsonPath("$.name", Is.is(agendaResponse.getName())))
			   .andExpect(jsonPath("$.description", Is.is(agendaResponse.getDescription())))
			   .andExpect(jsonPath("$.sessionStatus", Is.is(agendaResponse.getSessionStatus())));
	}

	@Test
	void caseGetAgendas() throws JsonProcessingException, Exception {

		Agenda agenda1 = makeAgenda(1);
		Agenda agenda2 = makeAgenda(2);
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
	}

	@Test
	void caseGetAgendaById() throws JsonProcessingException, Exception {

		Agenda agenda = makeAgenda(1);

		when(agendaService.findById(anyLong())).thenReturn(agenda);

		AgendaResponse agendaResponse = AgendaMapper.convertToResponse(agenda);

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

		Agenda agenda = makeAgenda(1);
		agenda.setSessionStart(LocalDateTime.now());
		agenda.setSessionEnd(LocalDateTime.now().plusMinutes(1));

		when(agendaService.openSession(any(), anyLong())).thenReturn(agenda);

		AgendaResponse agendaResponse = AgendaMapper.convertToResponse(agenda);

		// mockMvc.perform(put("/v1/agendas/openSession")
		// 	   .contentType(MediaType.APPLICATION_JSON)
		// 	   .content(objectMapper.writeValueAsString(sessionRequest)))
		// 	   .andExpect(status().isNoContent());

		Assertions.assertTrue("ATIVA".equals(agendaResponse.getSessionStatus()));
	}

	// @Test
	// void caseResult() throws JsonProcessingException, Exception {	

	// 	Vote vote1 = makeVote(true);
	// 	Vote vote2 = makeVote(false);
	// 	Vote vote3 = makeVote(true);
		
	// 	SessionResponse sessionResponse = SessionResponse.builder()
	// 													 .result("YES")
	// 													 .votesNo(1)
	// 													 .votesYes(2)
	// 													 .votesTotal(3)
	// 													 .build();
														 
	// 	when(agendaService.findById(anyLong())).thenReturn(makeAgenda(1L));
	// 	when(voteRepository.findByPkAgendaId(anyLong())).thenReturn(Arrays.asList(vote1, vote2, vote3));
									
	// 	mockMvc.perform(get("/v1/votes/sessionResult/1")
	// 		   .contentType(MediaType.APPLICATION_JSON))
	// 		   .andExpect(status().isOk())
	// 		   .andExpect(jsonPath("$.result", Is.is(sessionResponse.getResult())))
	// 		   .andExpect(jsonPath("$.votesNo", Is.is(sessionResponse.getVotesNo())))
	// 		   .andExpect(jsonPath("$.votesYes", Is.is(sessionResponse.getVotesYes())))
	// 		   .andExpect(jsonPath("$.votesTotal", Is.is(sessionResponse.getVotesTotal())));
	// }

	// private Agenda makeAgenda(Long id) {
	// 	return Agenda.builder()
	// 				 .id(id)
	// 				 .build();
					  
	// }

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

	private Agenda makeAgenda(int id) {
		return Agenda.builder()
							 .id(Long.valueOf(id))
							 .name("Teste")
							 .description("Esta é uma pauta de teste")
							 .build();
	}

	// private Vote makeVote(boolean decision) {
	// 	VotePK pk = VotePK.builder()
	// 					  .agenda(makeAgenda(1L))
	// 					  .associated("10020030040")
	// 					  .build();

	// 	return Vote.builder()
	// 				.pk(pk)
	// 				.decision(decision)
	// 				.build();	
	// }
}
