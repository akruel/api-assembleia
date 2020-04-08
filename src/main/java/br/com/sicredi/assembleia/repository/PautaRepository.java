package br.com.sicredi.assembleia.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.sicredi.assembleia.model.Pauta;

@Repository
public interface PautaRepository extends JpaRepository<Pauta, Long>{

}