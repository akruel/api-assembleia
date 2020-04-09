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
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import br.com.sicredi.assembleia.mapper.VoteMapper;
import br.com.sicredi.assembleia.model.Vote;
import br.com.sicredi.assembleia.service.VoteService;
import br.com.sicredi.assembleia.v1.dto.request.VoteRequest;
import br.com.sicredi.assembleia.v1.dto.response.SessionResponse;
import br.com.sicredi.assembleia.v1.dto.response.VoteResponse;
import io.swagger.annotations.ApiOperation;



@RestController
@RequestMapping("v1/votes")
public class VotoController {
    @Autowired
    private VoteService votoService;

    @Autowired
    ModelMapper modelMapper;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @ApiOperation(
        value = "Votar em uma pauta",
        notes = "Necessita que a pauta esteja com a sessão aberta"
        )
    public Vote postVoto(@RequestBody VoteRequest voteRequest) {
        return votoService.save(VoteMapper.convertToEntity(voteRequest));
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation("Buscar todos os votos em todas as pautas")
    public List<VoteResponse> getVotos() {
        return votoService.findAll()
                          .stream()
                          .map(VoteMapper::convertToResponse)
                          .collect(Collectors.toList());
    }

    @GetMapping("sessionResult/{agendaID}")
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation("Buscar o resultado da votação em uma pauta")
    public SessionResponse getResult(@PathVariable Long agendaID) {
        return votoService.calculateResult(agendaID);
    }

}