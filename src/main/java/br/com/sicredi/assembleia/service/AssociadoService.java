package br.com.sicredi.assembleia.service;

import java.util.List;

import br.com.sicredi.assembleia.model.Associado;

public interface AssociadoService {
    Associado save(Associado associado);
    Associado findById(String cpf);
    List<Associado> findAll();
}