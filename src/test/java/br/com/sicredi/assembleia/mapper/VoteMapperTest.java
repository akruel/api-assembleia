package br.com.sicredi.assembleia.mapper;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import br.com.sicredi.assembleia.model.Agenda;
import br.com.sicredi.assembleia.model.Vote;
import br.com.sicredi.assembleia.model.VotePK;
import br.com.sicredi.assembleia.v1.dto.request.VoteRequest;
import br.com.sicredi.assembleia.v1.dto.response.VoteResponse;
import br.com.sicredi.assembleia.v1.mapper.VoteMapper;

public class VoteMapperTest {

    @Test
    void voteToResponse() {
        Vote vote = makeVote(true);
        VoteResponse voteResponse = VoteMapper.convertToResponse(vote);

        Assertions.assertEquals(vote.getPk().getAgenda().getId(), voteResponse.getAgenda().getId());
        Assertions.assertEquals(vote.getPk().getAgenda().getName(), voteResponse.getAgenda().getName());
        Assertions.assertEquals(vote.getPk().getAgenda().getDescription(), voteResponse.getAgenda().getDescription());
        Assertions.assertEquals(vote.getPk().getAssociated(), voteResponse.getAssociated());
        Assertions.assertEquals(vote.isDecision(), voteResponse.getDecision());
    }

    @Test
    void voteToEntity() {
        VoteRequest voteRequest = makeVoteRequest("Sim");
        Vote vote = VoteMapper.convertToEntity(voteRequest);

        Assertions.assertEquals(voteRequest.getAgendaID(), vote.getPk().getAgenda().getId());
        Assertions.assertEquals(voteRequest.getAssociated(), vote.getPk().getAssociated());
        Assertions.assertEquals(makeVoteDecision(voteRequest.getDecision()), vote.isDecision());
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
    
    private Agenda makeAgendaToVote(Long id) {
		return Agenda.builder()
					 .id(id)
					 .build();
    }
    
    private VoteRequest makeVoteRequest(String decision) {
        VoteRequest voteRequest = new VoteRequest();
        voteRequest.setAgendaID(1L);
        voteRequest.setAssociated("73529716022");
        voteRequest.setDecision(decision);
        return voteRequest;
    }
    
    private Boolean makeVoteDecision(String decision) {
        return decision.equals("Sim") ? Boolean.TRUE : Boolean.FALSE;
    }

}