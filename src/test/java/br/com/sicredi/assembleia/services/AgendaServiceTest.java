package br.com.sicredi.assembleia.services;

import static org.mockito.ArgumentMatchers.anyLong;

import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import br.com.sicredi.assembleia.exception.ResourceNotFoundException;
import br.com.sicredi.assembleia.model.Agenda;
import br.com.sicredi.assembleia.repository.AgendaRepository;
import br.com.sicredi.assembleia.service.impl.AgendaServiceImpl;

@ExtendWith(SpringExtension.class)
public class AgendaServiceTest {
    @InjectMocks
    private AgendaServiceImpl agendaService;

    @Mock
    private AgendaRepository agendaRepository;

    @Test
    void save() {
        agendaService.save(makeAgenda());

        ArgumentCaptor<Agenda> agendaCaptor = ArgumentCaptor.forClass(Agenda.class);
        Mockito.verify(agendaRepository, Mockito.only()).save(agendaCaptor.capture());

        Assertions.assertEquals(1L, agendaCaptor.getValue().getId());
        Assertions.assertEquals("Teste", agendaCaptor.getValue().getName());
        Assertions.assertEquals("Este é um teste", agendaCaptor.getValue().getDescription());
    }

    @Test
    void findById() {
        Mockito.when(agendaRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(new Agenda()));
        agendaService.findById(1L);
        ArgumentCaptor<Long> longCaptor = ArgumentCaptor.forClass(Long.class);
        Mockito.verify(agendaRepository, Mockito.only()).findById(longCaptor.capture());

        Assertions.assertEquals(1L, longCaptor.getValue());
    }

    @Test
    void findByIdNotFound() {
        Mockito.when(agendaRepository.findById(Mockito.anyLong())).thenReturn(Optional.empty());

        Assertions.assertThrows(ResourceNotFoundException.class, () -> agendaService.findById(1L));
    }


    @Test
    void findAll() {
        agendaService.findAll();
        Mockito.verify(agendaRepository, Mockito.only()).findAll();
    }


    @Test
    void openSession() {
        Mockito.when(agendaRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(makeAgenda()));
        agendaService.openSession(makeAgenda(), null);

        ArgumentCaptor<Agenda> agendaCaptor = ArgumentCaptor.forClass(Agenda.class);
        ArgumentCaptor<Long> longCaptor = ArgumentCaptor.forClass(Long.class);
        Mockito.verify(agendaRepository, Mockito.times(1)).save(agendaCaptor.capture());
        Mockito.verify(agendaRepository, Mockito.times(1)).findById(longCaptor.capture());

        Assertions.assertEquals(1L, longCaptor.getValue());
        Assertions.assertEquals(1L, agendaCaptor.getValue().getId());
        Assertions.assertEquals("Teste", agendaCaptor.getValue().getName());
        Assertions.assertEquals("Este é um teste", agendaCaptor.getValue().getDescription());        
    }

    @Test
    void openSessionNotFound() {
        Mockito.when(agendaRepository.findById(Mockito.anyLong())).thenReturn(Optional.empty());

        Assertions.assertThrows(ResourceNotFoundException.class, () -> agendaService.openSession(makeAgenda(), anyLong()));
    }

    private Agenda makeAgenda () {
        return Agenda.builder()
                     .id(1L)
                     .name("Teste")
                     .description("Este é um teste")
                     .build();
    }

}