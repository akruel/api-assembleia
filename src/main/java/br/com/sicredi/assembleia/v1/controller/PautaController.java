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

import br.com.sicredi.assembleia.model.Pauta;
import br.com.sicredi.assembleia.service.PautaService;
import br.com.sicredi.assembleia.v1.dto.request.PautaReqDTO;
import br.com.sicredi.assembleia.v1.dto.response.PautaResDTO;




@RestController
@RequestMapping("v1/pautas")
public class PautaController {

    @Autowired
    private PautaService pautaService;

    @Autowired
    ModelMapper modelMapper;

    @PostMapping()
    public PautaResDTO postPauta(@RequestBody PautaReqDTO pautaReqDTO) {
        return convertToDTO(pautaService.save(convertToEntity(pautaReqDTO)));
    }
    

    @GetMapping()
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public List<PautaResDTO> getPautas() {
        return pautaService.findAll()
                           .stream()
                           .map(this::convertToDTO)
                           .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public PautaResDTO getPautaById(@PathVariable Long id) {
        return convertToDTO(pautaService.findById(id));
    }
    
    
    private Pauta convertToEntity(PautaReqDTO pautaDTO){
        return modelMapper.map(pautaDTO, Pauta.class); 
    }

    private PautaResDTO convertToDTO(Pauta pauta){
        return modelMapper.map(pauta, PautaResDTO.class); 
    }

}