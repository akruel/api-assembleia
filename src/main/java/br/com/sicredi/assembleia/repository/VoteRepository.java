package br.com.sicredi.assembleia.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.sicredi.assembleia.model.Vote;
import br.com.sicredi.assembleia.model.VotePK;

@Repository
public interface VoteRepository extends JpaRepository<Vote, VotePK> {
    Optional<Vote> findByPkAssociated(String cpf);
    List<Vote> findByPkAgendaId(Long agendaID);
}