package br.com.sicredi.assembleia.v1.mapper;

import br.com.sicredi.assembleia.model.Agenda;
import br.com.sicredi.assembleia.model.Vote;
import br.com.sicredi.assembleia.model.VotePK;
import br.com.sicredi.assembleia.util.StatusVote;
import br.com.sicredi.assembleia.v1.dto.request.VoteRequest;
import br.com.sicredi.assembleia.v1.dto.response.AgendaResponse;
import br.com.sicredi.assembleia.v1.dto.response.VoteResponse;

public class VoteMapper {

    private VoteMapper() {
        throw new IllegalStateException("Utility class");
    }

    public static VoteResponse convertToResponse(Vote vote) {
        String associated = vote.getPk().getAssociated();
        AgendaResponse agendaResponse = AgendaMapper.convertToResponse(vote.getPk().getAgenda());
        Boolean decision = vote.isDecision();
        StatusVote status = vote.getStatus();
        return VoteResponse.builder()
                         .associated(associated)
                         .agenda(agendaResponse)
                         .decision(decision)
                         .status(status.toString())
                         .build();                       
    }

    public static Vote convertToEntity(VoteRequest voteRequest){
        Agenda agenda = Agenda.builder()
                              .id(voteRequest.getAgendaID())
                              .build();

        VotePK pk = VotePK.builder()
                          .associated(voteRequest.getAssociated())
                          .agenda(agenda)
                          .build();

        return Vote.builder()
                   .pk(pk)
                   .decision(voteRequest.getDecision().equals("Sim") ? Boolean.TRUE : Boolean.FALSE)
                   .build(); 
    }
}