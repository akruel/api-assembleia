package br.com.sicredi.assembleia.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.sicredi.assembleia.model.Vote;
import br.com.sicredi.assembleia.model.VotePK;

@Repository
public interface VoteRepository extends JpaRepository<Vote, VotePK> {
    List<Vote> findByPkAgendaId(Long agendaID);
}