package br.com.sicredi.assembleia.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import br.com.sicredi.assembleia.client.response.UserInfoClientResponse;

@FeignClient(url = "https://user-info.herokuapp.com/users/", name = "user-info", fallback = UserInfoClientFallback.class)
public interface UserInfoClient {
    @GetMapping("{cpf}")
    public UserInfoClientResponse checkStatusToVote(@PathVariable("cpf") String cpf);
}