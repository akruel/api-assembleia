package br.com.sicredi.assembleia.service.impl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.sicredi.assembleia.client.UserInfoClient;
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

    private final UserInfoClient userInfoClient;

    @Override
    public Vote save(Vote vote) {
        Long agendaID = vote.getPk().getAgenda().getId();
        String cpf = vote.getPk().getAssociated();
        Agenda agenda = agendaService.findById(agendaID);
        LocalDateTime sessionEnd = agenda.getSessionEnd();
        boolean sessaoAtiva = sessionEnd != null && LocalDateTime.now().isBefore(sessionEnd);

        if (!sessaoAtiva) {
            throw new SessionNotOpenException("Sessão para a pauta " + agenda.getName() + " não está aberta!");
        }

        if (voteRepository.findByPkAssociated(cpf).isPresent()) {
            throw new ResourceDuplicatedException("Seu voto para esta pauta já foi computado!");
        }

        if (userInfoClient.checkStatusToVote(cpf).getStatus() == StatusEnum.UNABLE_TO_VOTE) {
            throw new UnableToVoteException("Associado " + cpf + " não está habilitado para votar!");
        }

        VotePK pk = VotePK.builder()
                          .associated(cpf)
                          .agenda(agenda)
                          .build();

        vote.setPk(pk);

        return voteRepository.save(vote);
    }

    @Override
    public List<Vote> findAll() {
        return voteRepository.findAll();
    }

    @Override
    public SessionResponse calculateResult(Long agendaID) {
        Agenda agenda = agendaService.findById(agendaID);

        if (agenda.getSessionStart() == null) {
            throw new ResourceNotFoundException("Sessão não iniciada!");
        }

        List <Vote> votes = voteRepository.findByPkAgendaId(agendaID);

        List <Vote> votesNo = votes.stream()
                                   .filter(v -> v.isDecision() == Boolean.FALSE)
                                   .collect(Collectors.toList());

        
        List <Vote> votesYes = votes.stream()
                                   .filter(v -> v.isDecision() == Boolean.TRUE)
                                   .collect(Collectors.toList());

        int result = votes.stream()
                          .map(v -> v.isDecision() ? 1 : -1)
                          .reduce(0, (subtotal, element) -> subtotal + element);
                                          
        return SessionMapper.convertToResponse(votesYes.size(), votesNo.size(), votes.size(), result);
    }

}