package br.com.sicredi.assembleia.service.impl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.sicredi.assembleia.exception.RecursoDuplicadoException;
import br.com.sicredi.assembleia.exception.RecursoNaoEncontradoException;
import br.com.sicredi.assembleia.model.Agenda;
import br.com.sicredi.assembleia.model.Vote;
import br.com.sicredi.assembleia.model.VotePK;
import br.com.sicredi.assembleia.repository.VoteRepository;
import br.com.sicredi.assembleia.service.AgendaService;
import br.com.sicredi.assembleia.service.AssociatedService;
import br.com.sicredi.assembleia.service.VoteService;
import br.com.sicredi.assembleia.util.ResultEnum;
import br.com.sicredi.assembleia.v1.dto.response.SessionResponse;

@Service
public class VoteServiceImpl implements VoteService {

    @Autowired
    private VoteRepository voteRepository;

    @Autowired
    private AgendaService agendaService;

    @Autowired
    private AssociatedService associatedService;

    @Override
    public Vote save(Vote vote) {
        Long agendaID = vote.getPk().getAgenda().getId();
        String cpf = vote.getPk().getAssociated();
        Agenda agenda = agendaService.findById(agendaID);
        LocalDateTime sessionEnd = agenda.getSessionEnd();
        boolean sessaoAtiva = sessionEnd != null && LocalDateTime.now().isBefore(sessionEnd);

        if (!sessaoAtiva) {
            throw new RecursoNaoEncontradoException("Sessão para a pauta " + agenda.getName() + " não está aberta!");
        }

        switch (associatedService.checkStatusToVote(cpf)) {
            case UNABLE_TO_VOTE:
                throw new RecursoNaoEncontradoException("Associado " + cpf + " não está habilitado para votar!");
            case INVALID:
                throw new RecursoNaoEncontradoException(
                        "O CPF " + cpf + " não é válido. Por favor, informe um CPF válido para votar!");
            default:
                break;
        }

        if (voteRepository.findByPkAssociated(cpf).isPresent()) {
            throw new RecursoDuplicadoException("Seu voto para esta pauta já foi computado!");
        }

        VotePK pk = VotePK.builder().associated(cpf).agenda(agenda).build();

        vote.setPk(pk);

        return voteRepository.save(vote);
    }

    @Override
    public List<Vote> findAll() {
        return voteRepository.findAll();
    }

    @Override
    public SessionResponse calculateResult(Long agendaID) {
        List <Vote> votes = voteRepository.findByPkAgendaId(agendaID);

        List <Vote> votesNo = votes.stream()
                                   .filter(v -> v.isDecision() == false)
                                   .collect(Collectors.toList());

        
        List <Vote> votesYes = votes.stream()
                                   .filter(v -> v.isDecision() == true)
                                   .collect(Collectors.toList());

        int result = votes.stream()
                          .map(v -> v.isDecision() ? 1 : -1)
                          .reduce(0, (subtotal, element) -> subtotal + element);
                                          
        return SessionResponse.builder()
                               .votesTotal(votes.size())
                               .votesNo(votesNo.size())
                               .votesYes(votesYes.size())
                               .result(this.generateResult(result).toString())
                               .build();
    }

    private ResultEnum generateResult(Integer result) {
        if (result == null) {
			return ResultEnum.NO_VOTES;
		} else if (result == 0) {
			return ResultEnum.TIED_VOTE;
		} else if (result < 0) {
			return ResultEnum.NO;
		}
		return ResultEnum.YES;
    }

}