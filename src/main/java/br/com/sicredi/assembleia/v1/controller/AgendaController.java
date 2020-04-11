package br.com.sicredi.assembleia.v1.controller;

import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;

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

import br.com.sicredi.assembleia.service.AgendaService;
import br.com.sicredi.assembleia.v1.dto.request.AgendaRequest;
import br.com.sicredi.assembleia.v1.dto.request.SessionRequest;
import br.com.sicredi.assembleia.v1.dto.response.AgendaResponse;
import br.com.sicredi.assembleia.v1.mapper.AgendaMapper;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("v1/agendas")
public class AgendaController {

    @Autowired
    private AgendaService agendaService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @ApiOperation("Criar uma pauta")
    public AgendaResponse postPauta(@Valid @RequestBody AgendaRequest agendaRequest) {
        return AgendaMapper.convertToResponse(agendaService.save(AgendaMapper.convertToEntity(agendaRequest)));
    }
    
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation("Buscar todas as pautas")
    public List<AgendaResponse> getAgendas() {
        return agendaService.findAll()
                            .stream()
                            .map(AgendaMapper::convertToResponse)
                            .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation("Buscar uma pauta por ID")
    public AgendaResponse getAgendaById(@PathVariable Long id) {
        return AgendaMapper.convertToResponse(agendaService.findById(id));
    }

    @PutMapping("/openSession")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ApiOperation(
        value = "Abrir uma sessão para votação em uma pauta",
        notes = "Necessário um ID de pauta válido"
    )
    public AgendaResponse openSession(@Valid @RequestBody SessionRequest sessionRequest) {
        Long duracaoEmMinutos = sessionRequest.getDurationInMinutes();
        return AgendaMapper.convertToResponse(agendaService.openSession(AgendaMapper.sessionToAgendaEntity(sessionRequest), duracaoEmMinutos));
    }

}