package br.com.sicredi.assembleia.controller;

import static org.mockito.Mockito.times;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDateTime;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.hamcrest.core.Is;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import br.com.sicredi.assembleia.exception.ResourceDuplicatedException;
import br.com.sicredi.assembleia.exception.ResourceNotFoundException;
import br.com.sicredi.assembleia.exception.SessionNotOpenException;
import br.com.sicredi.assembleia.exception.UnableToVoteException;
import br.com.sicredi.assembleia.model.Agenda;
import br.com.sicredi.assembleia.model.Vote;
import br.com.sicredi.assembleia.model.VotePK;
import br.com.sicredi.assembleia.service.AgendaService;
import br.com.sicredi.assembleia.service.VoteService;
import br.com.sicredi.assembleia.util.StatusVote;
import br.com.sicredi.assembleia.v1.controller.VoteController;
import br.com.sicredi.assembleia.v1.dto.request.VoteRequest;
import br.com.sicredi.assembleia.v1.dto.response.SessionResponse;
import br.com.sicredi.assembleia.v1.dto.response.VoteResponse;
import br.com.sicredi.assembleia.v1.mapper.VoteMapper;

@ExtendWith(SpringExtension.class)
@WebMvcTest(VoteController.class)
public class VoteControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@MockBean
	private VoteService voteService;   

	@MockBean
	private AgendaService agendaService;

	private final String INVALID_CPF = "10020030040";
    
    @Test
	void postVote() throws JsonProcessingException, Exception {
		Vote voteReturn = makeVoteReturn(true);
		Mockito.when(voteService.save(makeVote(true))).thenReturn(voteReturn);

		VoteResponse voteResponse = VoteMapper.convertToResponse(voteReturn);

		mockMvc.perform(post("/v1/votes")
			   .contentType("application/json")
			   .content(objectMapper.writeValueAsString(makeVoteRequest("Sim"))))
			   .andExpect(status().isCreated())
			   .andExpect(jsonPath("$.associated", Is.is(voteResponse.getAssociated())))
			   .andExpect(jsonPath("$.agenda.id", Is.is(voteResponse.getAgenda().getId().intValue())))
			   .andExpect(jsonPath("$.agenda.name", Is.is(voteResponse.getAgenda().getName())))
			   .andExpect(jsonPath("$.agenda.description", Is.is(voteResponse.getAgenda().getDescription())))
			   .andExpect(jsonPath("$.agenda.sessionStatus", Is.is(voteResponse.getAgenda().getSessionStatus())))
			   .andExpect(jsonPath("$.decision", Is.is(voteResponse.getDecision())));

		Mockito.verify(voteService, times(1)).save(makeVote(true));
		Mockito.verifyNoMoreInteractions(voteService);
	}

	@Test
	void postVoteWithSessionNotOpen() throws JsonProcessingException, Exception {
		Mockito.when(voteService.save(makeVote(true))).thenThrow(new SessionNotOpenException(""));

		mockMvc.perform(post("/v1/votes")
			   .contentType("application/json")
			   .content(objectMapper.writeValueAsString(makeVoteRequest("Sim"))))
			   .andExpect(status().isBadRequest());

		Mockito.verify(voteService, times(1)).save(makeVote(true));
		Mockito.verifyNoMoreInteractions(voteService);
	}

	@Test
	void postVoteWhenDuplicated() throws JsonProcessingException, Exception {
		Mockito.when(voteService.save(makeVote(true))).thenThrow(new ResourceDuplicatedException(""));

		mockMvc.perform(post("/v1/votes")
			   .contentType("application/json")
			   .content(objectMapper.writeValueAsString(makeVoteRequest("Sim"))))
			   .andExpect(status().isConflict());

		Mockito.verify(voteService, times(1)).save(makeVote(true));
		Mockito.verifyNoMoreInteractions(voteService);
	}

	@Test
	void postVoteWhenUnableToVote() throws JsonProcessingException, Exception {
		Mockito.when(voteService.save(makeVote(true))).thenThrow(new UnableToVoteException(""));

		mockMvc.perform(post("/v1/votes")
			   .contentType("application/json")
			   .content(objectMapper.writeValueAsString(makeVoteRequest("Sim"))))
			   .andExpect(status().isForbidden());

		Mockito.verify(voteService, times(1)).save(makeVote(true));
		Mockito.verifyNoMoreInteractions(voteService);
	}

	@Test
	void postVoteWithInvalidCpf() throws JsonProcessingException, Exception {
		mockMvc.perform(post("/v1/votes")
			   .contentType("application/json")
			   .content(objectMapper.writeValueAsString(makeVoteRequestInvalidCpf("Sim"))))
			   .andExpect(status().isBadRequest());

		Mockito.verifyNoMoreInteractions(voteService);
	}
	
	@Test
	void getResult() throws JsonProcessingException, Exception {
		SessionResponse sessionResponse = makeSessionResponse();
		Mockito.when(voteService.calculateResult(1L)).thenReturn(sessionResponse);

		mockMvc.perform(get("/v1/votes/sessionResult/1")
			   .contentType("application/json"))
			   .andExpect(status().isOk())
			   .andExpect(jsonPath("$.result", Is.is(sessionResponse.getResult())))
			   .andExpect(jsonPath("$.votesTotal", Is.is(sessionResponse.getVotesTotal())))
			   .andExpect(jsonPath("$.votesYes", Is.is(sessionResponse.getVotesYes())))
			   .andExpect(jsonPath("$.votesNo", Is.is(sessionResponse.getVotesNo())));

		Mockito.verify(voteService, times(1)).calculateResult(1L);
		Mockito.verifyNoMoreInteractions(voteService);
	}

	@Test
	void getResultSessionNotOpen() throws JsonProcessingException, Exception {
		Mockito.when(voteService.calculateResult(1L)).thenThrow(new SessionNotOpenException(""));

		mockMvc.perform(get("/v1/votes/sessionResult/1")
			   .contentType("application/json"))
			   .andExpect(status().isBadRequest());

		Mockito.verify(voteService, times(1)).calculateResult(1L);
		Mockito.verifyNoMoreInteractions(voteService);
	}
	
	@Test
	void getResultWithAgenda404() throws JsonProcessingException, Exception {
		Mockito.when(voteService.calculateResult(1L)).thenThrow(new ResourceNotFoundException(""));

		mockMvc.perform(get("/v1/votes/sessionResult/1")
			   .contentType("application/json"))
			   .andExpect(status().isNotFound());

		Mockito.verify(voteService, times(1)).calculateResult(1L);
		Mockito.verifyNoMoreInteractions(voteService);
    }
    
    private Agenda makeAgendaToVote(Long id) {
		return Agenda.builder()
							 .id(id)
							 .build();
	}

	private Agenda makeAgendaWithSession(Long id) {
		return Agenda.builder()
							 .id(id)
							 .name("Teste")
							 .description("Esta é uma pauta de teste")
							 .sessionStart(LocalDateTime.now())
							 .sessionEnd(LocalDateTime.now().plusMinutes(1))
							 .build();
	}
    
	private Vote makeVote(boolean decision) {
		VotePK pk = VotePK.builder()
						  .agenda(makeAgendaToVote(1L))
						  .associated("73529716022")
						  .build();

		return Vote.builder()
					.pk(pk)
					.decision(decision)
					.build();	
	}
	
	private Vote makeVoteReturn(boolean decision) {
		VotePK pk = VotePK.builder()
						  .agenda(makeAgendaWithSession(1L))
						  .associated("73529716022")
						  .build();

		return Vote.builder()
					.pk(pk)
					.decision(decision)
					.status(StatusVote.CONFIRMED)
					.build();	
    }
    
    private VoteRequest makeVoteRequest(String decision) {
        VoteRequest voteRequest = new VoteRequest();
        voteRequest.setAgendaID(1L);
        voteRequest.setAssociated("73529716022");
        voteRequest.setDecision(decision);
        return voteRequest;
	}

	private VoteRequest makeVoteRequestInvalidCpf(String decision) {
        VoteRequest voteRequest = new VoteRequest();
        voteRequest.setAgendaID(1L);
        voteRequest.setAssociated(INVALID_CPF);
        voteRequest.setDecision(decision);
        return voteRequest;
	}
	
	
	private SessionResponse makeSessionResponse() {
		SessionResponse sessionResponse = new SessionResponse();
		sessionResponse.setResult("YES");
		sessionResponse.setVotesNo(0);
		sessionResponse.setVotesYes(1);
		sessionResponse.setVotesTotal(1);
        return sessionResponse;
    }

}