package br.com.sicredi.assembleia.client;

import org.springframework.stereotype.Component;

import br.com.sicredi.assembleia.client.response.UserInfoClientResponse;
import br.com.sicredi.assembleia.util.StatusEnum;

@Component
public class UserInfoClientFallback implements UserInfoClient {
 
    @Override
    public UserInfoClientResponse checkStatusToVote(String cpf) {
        UserInfoClientResponse response = new UserInfoClientResponse();
        response.setStatus(StatusEnum.NOT_CONFIRMED);
        return response;  
    }
}