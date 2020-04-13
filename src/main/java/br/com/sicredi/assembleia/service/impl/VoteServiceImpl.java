package br.com.sicredi.assembleia.service.impl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.sicredi.assembleia.client.UserInfoClient;
import br.com.sicredi.assembleia.client.response.UserInfoClientResponse;
import br.com.sicredi.assembleia.exception.ResourceDuplicatedException;
import br.com.sicredi.assembleia.exception.ResourceNotFoundException;
import br.com.sicredi.assembleia.exception.SessionNotOpenException;
import br.com.sicredi.assembleia.exception.UnableToVoteException;
import br.com.sicredi.assembleia.model.Agenda;
import br.com.sicredi.assembleia.model.Vote;
import br.com.sicredi.assembleia.model.VotePK;
import br.com.sicredi.assembleia.repository.VoteRepository;
import br.com.sicredi.assembleia.service.AgendaService;
import br.com.sicredi.assembleia.service.VoteService;
import br.com.sicredi.assembleia.util.StatusEnum;
import br.com.sicredi.assembleia.util.StatusVote;
import br.com.sicredi.assembleia.util.VoteCache;
import br.com.sicredi.assembleia.v1.dto.response.SessionResponse;
import br.com.sicredi.assembleia.v1.mapper.SessionMapper;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class VoteServiceImpl implements VoteService {

    @Autowired
    private VoteRepository voteRepository;

    @Autowired
    private AgendaService agendaService;

    @Autowired
    private VoteCache voteCache;

    private final UserInfoClient userInfoClient;

    @Override
    public Vote save(Vote vote) {
        vote.setStatus(StatusVote.NOT_CONFIRMED);
        voteCache.setVote(vote);
        Long agendaID = vote.getPk().getAgenda().getId();
        String cpf = vote.getPk().getAssociated();
        Agenda agenda = agendaService.findById(agendaID);
        LocalDateTime sessionEnd = agenda.getSessionEnd();
        boolean sessaoAtiva = sessionEnd != null && LocalDateTime.now().isBefore(sessionEnd);

        if (!sessaoAtiva) {
            throw new SessionNotOpenException("Sessão para a pauta " + agenda.getName() + " não está aberta!");
        }

        if (voteRepository.findById(vote.getPk()).isPresent()) {
            throw new ResourceDuplicatedException("Seu voto para esta pauta já foi computado!");
        }

        VotePK pk = VotePK.builder()
                          .associated(cpf)
                          .agenda(agenda)
                          .build();

        vote.setPk(pk);

        UserInfoClientResponse clientResponse = userInfoClient.checkStatusToVote(cpf);
        
        if (clientResponse.getStatus() == StatusEnum.UNABLE_TO_VOTE) {
            throw new UnableToVoteException("Associado " + cpf + " não está habilitado para votar!");
        }

        vote.setStatus(clientResponse.getStatus() == StatusEnum.ABLE_TO_VOTE ? StatusVote.CONFIRMED : StatusVote.NOT_CONFIRMED);
        return voteRepository.save(vote);
    }

    @Override
    public List<Vote> findAll() {
        return voteRepository.findAll();
    }
    
    @Override
    public Vote update(Vote vote) {
        return voteRepository.save(vote);
    }

    @Override
    public void delete(Vote vote) {
        voteRepository.delete(vote);
    }

    @Override
    public SessionResponse calculateResult(Long agendaID) {
        Agenda agenda = agendaService.findById(agendaID);
        Integer result;
        int votesNo;
        int votesYes;

        if (agenda.getSessionStart() == null) {
            throw new SessionNotOpenException("Sessão não iniciada!");
        }

        this.checkAllVotedConfirmed(agendaID);

        List<Vote> votes = voteRepository.findByPkAgendaId(agendaID);

        if (!votes.isEmpty()) {
            votesNo = votes.stream()
                           .filter(v -> v.isDecision() == Boolean.FALSE)
                           .collect(Collectors.toList()).size();
            
            votesYes = votes.stream()
                            .filter(v -> v.isDecision() == Boolean.TRUE)
                            .collect(Collectors.toList()).size();

            result = votes.stream()
                          .map(v -> v.isDecision() ? 1 : -1)
                          .reduce(0, (subtotal, element) -> subtotal + element);
        } else {
            result = null;
            votesYes = 0;
            votesNo = 0;
        }                                   
                                          
        return SessionMapper.convertToResponse(votesYes, votesNo, votes.size(), result);
    }

    private void checkAllVotedConfirmed(Long agendaID) {
        List<Vote> votes = voteRepository.findByPkAgendaId(agendaID);
        Predicate<Vote> isVoteNotConfirmed = v -> v.getStatus() == StatusVote.NOT_CONFIRMED; 
        Predicate<Vote> isAssociatedAbleToVote = a -> this.checkStatusAssociated(a.getPk().getAssociated()) == StatusEnum.ABLE_TO_VOTE;
        
        votes.stream()
             .filter(isVoteNotConfirmed.and(isAssociatedAbleToVote))
             .forEach(v -> update(setConfirmed(v)));
        
        voteRepository.findByPkAgendaId(agendaID)
                      .stream()
                      .filter(v -> v.getStatus() == StatusVote.NOT_CONFIRMED)
                      .forEach(this::delete);
    }

    private Vote setConfirmed(Vote v) {
        v.setStatus(StatusVote.CONFIRMED);
        return v;
    }

    private StatusEnum checkStatusAssociated (String cpf) {
        StatusEnum response = userInfoClient.checkStatusToVote(cpf).getStatus();
        if(response == StatusEnum.NOT_CONFIRMED){
            throw new ResourceNotFoundException("Não foi possível confirmar a votação, pois o serviço de validação do CPF do associado está fora!");
        }
        return response;
    }

}