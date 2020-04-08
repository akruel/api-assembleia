package br.com.sicredi.assembleia.service;

import java.util.List;

import br.com.sicredi.assembleia.model.Pauta;

public interface PautaService {
    Pauta save(Pauta pauta);
    Pauta findById(Long id);
    List<Pauta> findAll();
}