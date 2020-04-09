package br.com.sicredi.assembleia.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import br.com.sicredi.assembleia.service.AssociatedService;
import br.com.sicredi.assembleia.util.StatusDTO;
import br.com.sicredi.assembleia.util.StatusEnum;

@Service
public class AssociatedServiceImpl implements AssociatedService {

    @Autowired
    private RestTemplate restTemplate;

    @Override
    public StatusEnum checkStatusToVote(String cpf) {
        String fooResourceUrl = "https://user-info.herokuapp.com/users/";
        try {
            ResponseEntity<StatusDTO> response = restTemplate.getForEntity(fooResourceUrl + cpf, StatusDTO.class);
            return response.getBody().getStatus();
        } catch (HttpClientErrorException e) {
            return StatusEnum.INVALID;
        }
    }

}