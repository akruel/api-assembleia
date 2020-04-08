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

import br.com.sicredi.assembleia.model.Associado;
import br.com.sicredi.assembleia.service.AssociadoService;
import br.com.sicredi.assembleia.v1.dto.AssociadoDTO;

@RestController
@RequestMapping(value = "v1/associados")
public class AssociadoController {
    @Autowired
    private AssociadoService associadoService;

    @Autowired
    ModelMapper modelMapper;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public AssociadoDTO  postAssociado(@RequestBody AssociadoDTO associadoDTO) {    
        Associado associado = convertToEntity(associadoDTO);    
        return convertToDTO(associadoService.save(associado));
    }

    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    @GetMapping
    public List<AssociadoDTO> getAssociados() {
        List<Associado> associados = associadoService.findAll();
        return associados.stream()
                         .map(this::convertToDTO)
                         .collect(Collectors.toList());
    }

    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    @GetMapping(value="/{cpf}")
    public AssociadoDTO getAssociadoById(@PathVariable String cpf) {
        return convertToDTO(associadoService.findById(cpf));
    }
    

    private Associado convertToEntity(AssociadoDTO associadoDTO){
        return modelMapper.map(associadoDTO, Associado.class); 
    }

    private AssociadoDTO convertToDTO(Associado associado){
        return modelMapper.map(associado, AssociadoDTO.class); 
    }
    
}