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
    public Agenda save(Agenda agenda) {
        return agendaRepository.save(agenda);
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
    public Agenda openSession(Long agendaID, Long durationInMinutes) {
        Agenda agenda = findById(agendaID);
        agenda.setSessionStart(LocalDateTime.now());
        if (durationInMinutes != null) {
            agenda.setSessionEnd(agenda.getSessionStart().plusMinutes(durationInMinutes));
        } else {
            agenda.setSessionEnd(agenda.getSessionStart().plusMinutes(1));
        }
        return agendaRepository.save(agenda);
    }

}