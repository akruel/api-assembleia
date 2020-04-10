package br.com.sicredi.assembleia.service.impl;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.sicredi.assembleia.exception.ResourceNotFoundException;
import br.com.sicredi.assembleia.model.Agenda;
import br.com.sicredi.assembleia.repository.AgendaRepository;
import br.com.sicredi.assembleia.service.AgendaService;

@Service
public class AgendaServiceImpl implements AgendaService {

    @Autowired
    private AgendaRepository agendaRepository;

    @Override
    public Agenda save(Agenda pauta) {
        return agendaRepository.save(pauta);
    }

    @Override
    public Agenda findById(Long id) {
        return agendaRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Pauta não encontrada!"));
    }

    @Override
    public List<Agenda> findAll() {
        return agendaRepository.findAll();
    }

    @Override
    public Agenda openSession(Agenda agenda, Long durationInMinutes) {
        Agenda actualAgenda = this.findById(agenda.getId());
        actualAgenda.setSessionStart(LocalDateTime.now());
        if (durationInMinutes != null) {
            actualAgenda.setSessionEnd(actualAgenda.getSessionStart().plusMinutes(durationInMinutes));
        } else {
            actualAgenda.setSessionEnd(actualAgenda.getSessionStart().plusMinutes(1));
        }
        return agendaRepository.save(actualAgenda);
    }

}