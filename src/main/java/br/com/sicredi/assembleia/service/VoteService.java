package br.com.sicredi.assembleia.service;

import java.util.List;

import br.com.sicredi.assembleia.model.Vote;
import br.com.sicredi.assembleia.v1.dto.response.SessionResponse;

public interface VoteService {
    Vote save(Vote voto);
    List<Vote> findAll();
    SessionResponse calculateResult(Long agendaID);
}