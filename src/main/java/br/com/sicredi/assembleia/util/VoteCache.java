package br.com.sicredi.assembleia.util;

import org.springframework.stereotype.Component;

import br.com.sicredi.assembleia.model.Vote;
import lombok.Getter;
import lombok.Setter;

@Component
@Setter
@Getter
public class VoteCache {
    private Vote vote;    
}