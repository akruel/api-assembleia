package br.com.sicredi.assembleia.service;

import br.com.sicredi.assembleia.util.StatusEnum;

public interface AssociatedService {

    StatusEnum checkStatusToVote(String cpf);

}