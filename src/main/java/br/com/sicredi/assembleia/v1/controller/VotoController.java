package br.com.sicredi.assembleia.v1.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import br.com.sicredi.assembleia.model.Agenda;
import br.com.sicredi.assembleia.model.Vote;
import br.com.sicredi.assembleia.model.VotePK;
import br.com.sicredi.assembleia.service.VoteService;
import br.com.sicredi.assembleia.v1.dto.request.VoteRequest;
import br.com.sicredi.assembleia.v1.dto.response.AgendaResponse;
import br.com.sicredi.assembleia.v1.dto.response.SessionResponse;
import br.com.sicredi.assembleia.v1.dto.response.VoteResponse;



@RestController
@RequestMapping("v1/votos")
public class VotoController {
    @Autowired
    private VoteService votoService;

    @Autowired
    ModelMapper modelMapper;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public Vote postVoto(@RequestBody VoteRequest votoDTO) {
        return votoService.save(convertToEntity(votoDTO));
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public List<VoteResponse> getVotos() {
        return votoService.findAll()
                          .stream()
                          .map(this::convertToDTO)
                          .collect(Collectors.toList());
    }

    @GetMapping("agendaResult/{agendaID}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public SessionResponse getResult(@PathVariable Long agendaID) {
        return votoService.calculateResult(agendaID);
    }
    

    private Vote convertToEntity(VoteRequest voteDTO){
        Agenda agenda = Agenda.builder()
                              .id(voteDTO.getAgendaID())
                              .build();

        VotePK pk = VotePK.builder()
                          .associated(voteDTO.getAssociated())
                          .agenda(agenda)
                          .build();

        return Vote.builder()
                   .pk(pk)
                   .decision(voteDTO.getDecision())
                   .build(); 
    }
    
    
    private VoteResponse convertToDTO(Vote vote) {
        String associated = vote.getPk().getAssociated();
        AgendaResponse agendaDTO = convertPautaToDTO(vote.getPk().getAgenda());
        Boolean decision = vote.isDecision();
        return VoteResponse.builder()
                         .associated(associated)
                         .agenda(agendaDTO)
                         .decision(decision)
                         .build();                       
    }

    private AgendaResponse convertPautaToDTO(Agenda agenda){
        return modelMapper.map(agenda, AgendaResponse.class); 
    }

}