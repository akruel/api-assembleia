package br.com.sicredi.assembleia.service;

import java.util.List;

import br.com.sicredi.assembleia.model.Agenda;

public interface AgendaService {
    Agenda save(Agenda agenda);
    Agenda findById(Long id);
    List<Agenda> findAll();
    Agenda openSession(Long agendaID, Long durationInMinutes);
}