package br.com.sicredi.assembleia.v1.controller;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import br.com.sicredi.assembleia.model.Agenda;
import br.com.sicredi.assembleia.service.AgendaService;
import br.com.sicredi.assembleia.v1.dto.request.AgendaRequest;
import br.com.sicredi.assembleia.v1.dto.request.SessionRequest;
import br.com.sicredi.assembleia.v1.dto.response.AgendaResponse;

@RestController
@RequestMapping("v1/agendas")
public class AgendaController {

    @Autowired
    private AgendaService agendaService;

    @Autowired
    ModelMapper modelMapper;

    @PostMapping
    public AgendaResponse postPauta(@RequestBody AgendaRequest agendaDTO) {
        return convertToDTO(agendaService.save(convertToEntity(agendaDTO)));
    }
    
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<AgendaResponse> getPautas() {
        return agendaService.findAll()
                            .stream()
                            .map(this::convertToDTO)
                            .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public AgendaResponse getPautaById(@PathVariable Long id) {
        return convertToDTO(agendaService.findById(id));
    }

    @PutMapping("/openSession")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public AgendaResponse abrirSessao(@RequestBody SessionRequest sessionDTO) {
        Long duracaoEmMinutos = sessionDTO.getDurationInMinutes();
        return convertToDTO(agendaService.openSession(convertSessaoReqToPautaEntity(sessionDTO), duracaoEmMinutos));
    }
    
    private Agenda convertSessaoReqToPautaEntity(SessionRequest sessionDTO){
        return Agenda.builder()
                     .id(sessionDTO.getAgendaID())
                     .build(); 
    }
    
    private Agenda convertToEntity(AgendaRequest agendaDTO){
        return modelMapper.map(agendaDTO, Agenda.class); 
    }

    private AgendaResponse convertToDTO(Agenda agenda){
        AgendaResponse pautaRestDTO = modelMapper.map(agenda, AgendaResponse.class); 
        pautaRestDTO.setSessionStatus(agenda.getSessionEnd() != null && LocalDateTime.now().isBefore(agenda.getSessionEnd()) ? "ATIVA" : "INATIVA");
        return pautaRestDTO;
    }

}