package br.com.sicredi.assembleia.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.sicredi.assembleia.exception.ResourceDuplicatedException;
import br.com.sicredi.assembleia.exception.ResourceNotFoundException;
import br.com.sicredi.assembleia.model.Associado;
import br.com.sicredi.assembleia.repository.AssociadoRepository;
import br.com.sicredi.assembleia.service.AssociadoService;

@Service
public class AssociadoServiceImpl implements AssociadoService {

    @Autowired
    private AssociadoRepository associadoRepository;

    @Override
    public Associado save(Associado associado) {
        if (associadoRepository.findById(associado.getCpf()).isPresent()) {
            throw new ResourceDuplicatedException("CPF já está cadastrado!");
        }
        return associadoRepository.save(associado);
    }

    @Override
    public Associado findById(String cpf) {
        return associadoRepository.findById(cpf)
                .orElseThrow(() -> new ResourceNotFoundException("Associado não encontrado!"));
    }

    @Override
    public List<Associado> findAll() {
        return associadoRepository.findAll();
    }

}