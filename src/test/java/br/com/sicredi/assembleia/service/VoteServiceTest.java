package br.com.sicredi.assembleia.service;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;

import java.time.LocalDateTime;
import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import br.com.sicredi.assembleia.client.UserInfoClient;
import br.com.sicredi.assembleia.client.response.UserInfoClientResponse;
import br.com.sicredi.assembleia.exception.ResourceDuplicatedException;
import br.com.sicredi.assembleia.exception.SessionNotOpenException;
import br.com.sicredi.assembleia.exception.UnableToVoteException;
import br.com.sicredi.assembleia.model.Agenda;
import br.com.sicredi.assembleia.model.Vote;
import br.com.sicredi.assembleia.model.VotePK;
import br.com.sicredi.assembleia.repository.AgendaRepository;
import br.com.sicredi.assembleia.repository.VoteRepository;
import br.com.sicredi.assembleia.service.impl.VoteServiceImpl;
import br.com.sicredi.assembleia.util.StatusEnum;

@ExtendWith(SpringExtension.class)
public class VoteServiceTest {
    @InjectMocks
    private VoteServiceImpl voteService;

    @Mock
    private VoteRepository voteRepository;
    
    @Mock
    private AgendaRepository agendaRepository;

    @Mock
    private AgendaService agendaService;

    @Mock
    private UserInfoClient userInfoClient;

    @Test
    void save() {
        UserInfoClientResponse userInfo = new UserInfoClientResponse();
        userInfo.setStatus(StatusEnum.ABLE_TO_VOTE);

        Mockito.when(agendaService.findById(anyLong())).thenReturn(makeAgendaWithSession(1));
        Mockito.when(userInfoClient.checkStatusToVote(Mockito.anyString())).thenReturn(userInfo);

        voteService.save(makeVote(true));

        ArgumentCaptor<Vote> topicCaptor = ArgumentCaptor.forClass(Vote.class);
        Mockito.verify(voteRepository, Mockito.times(1)).findByPkAssociated(makeVote(true).getPk().getAssociated());
        Mockito.verify(voteRepository, Mockito.times(1)).save(topicCaptor.capture());

        Assertions.assertEquals(makeVote(true).getPk().getAssociated(), topicCaptor.getValue().getPk().getAssociated());
        Assertions.assertEquals(makeVote(true).getPk().getAgenda().getId(), topicCaptor.getValue().getPk().getAgenda().getId());
    }

    @Test
    void saveSessioNotOpen() {
        Mockito.when(agendaService.findById(anyLong())).thenReturn(makeAgenda(1));
        Mockito.when(agendaRepository.findById(anyLong())).thenReturn(Optional.of(makeAgenda(1)));

        Assertions.assertThrows(SessionNotOpenException.class, () -> voteService.save(makeVote(true)));
    }

    @Test
    void saveCpfDuplicated() {
        Mockito.when(agendaService.findById(anyLong())).thenReturn(makeAgendaWithSession(1));
        Mockito.when(voteRepository.findByPkAssociated(anyString())).thenReturn(Optional.of(new Vote()));

        Assertions.assertThrows(ResourceDuplicatedException.class, () -> voteService.save(makeVote(true)));
    }

    @Test
    void saveUnableToVote() {
        UserInfoClientResponse userInfo = new UserInfoClientResponse();
        userInfo.setStatus(StatusEnum.UNABLE_TO_VOTE);

        Agenda agenda = makeAgenda(1);
        agenda.setSessionEnd(LocalDateTime.now());
        agenda.setSessionEnd(LocalDateTime.now().plusMinutes(1));

        Mockito.when(agendaService.findById(anyLong())).thenReturn(agenda);
        Mockito.when(userInfoClient.checkStatusToVote(Mockito.anyString())).thenReturn(userInfo);

        Assertions.assertThrows(UnableToVoteException.class, () -> voteService.save(makeVote(true)));
    }

    
	private Agenda makeAgenda(int id) {
		return Agenda.builder()
					 .id(Long.valueOf(id))
					 .name("Teste")
					 .description("Esta é uma pauta de teste")
					 .build();
    }
    
    private Agenda makeAgendaWithSession(int id) {
		return Agenda.builder()
					 .id(Long.valueOf(id))
					 .name("Teste")
					 .description("Esta é uma pauta de teste")
					 .sessionStart(LocalDateTime.now())
					 .sessionEnd(LocalDateTime.now().plusMinutes(1))
					 .build();
	}

    private Vote makeVote(boolean decision) {
		VotePK pk = VotePK.builder()
						  .agenda(makeAgenda(1))
						  .associated("10020030040")
						  .build();

		return Vote.builder()
					.pk(pk)
					.decision(decision)
					.build();	
	}
}