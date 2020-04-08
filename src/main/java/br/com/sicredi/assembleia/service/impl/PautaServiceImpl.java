package br.com.sicredi.assembleia.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.sicredi.assembleia.exception.ResourceNotFoundException;
import br.com.sicredi.assembleia.model.Pauta;
import br.com.sicredi.assembleia.repository.PautaRepository;
import br.com.sicredi.assembleia.service.PautaService;

@Service
public class PautaServiceImpl implements PautaService {

    @Autowired
    private PautaRepository pautaRepository;

    @Override
    public Pauta save(Pauta pauta) {
        return pautaRepository.save(pauta);
    }

    @Override
    public Pauta findById(Long id) {
        return pautaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Pauta não encontrada!"));
    }

    @Override
    public List<Pauta> findAll() {
        return pautaRepository.findAll();
    }

}