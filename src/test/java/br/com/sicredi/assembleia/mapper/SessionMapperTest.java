package br.com.sicredi.assembleia.mapper;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import br.com.sicredi.assembleia.util.ResultEnum;
import br.com.sicredi.assembleia.v1.dto.response.SessionResponse;
import br.com.sicredi.assembleia.v1.mapper.SessionMapper;

public class SessionMapperTest {
    int votesYes;
    int votesNo;
    int votesTotal; 
    Integer result;

    @Test
    void convertToResponseWithYesWins() {
        this.makeYesWins();

        SessionResponse sessionResponse = SessionMapper.convertToResponse(votesYes, votesNo, votesTotal, result);

        Assertions.assertEquals(sessionResponse.getVotesYes(), votesYes);
        Assertions.assertEquals(sessionResponse.getVotesNo(), votesNo);
        Assertions.assertEquals(sessionResponse.getVotesTotal(), votesTotal);
        Assertions.assertEquals(sessionResponse.getResult(), ResultEnum.YES.toString());
    }

    @Test
    void convertToResponseWithNoWins() {
        this.makeNoWins();

        SessionResponse sessionResponse = SessionMapper.convertToResponse(votesYes, votesNo, votesTotal, result);

        Assertions.assertEquals(sessionResponse.getVotesYes(), votesYes);
        Assertions.assertEquals(sessionResponse.getVotesNo(), votesNo);
        Assertions.assertEquals(sessionResponse.getVotesTotal(), votesTotal);
        Assertions.assertEquals(sessionResponse.getResult(), ResultEnum.NO.toString());
    }

    @Test
    void convertToResponseWithTied() {
        this.makeTied();

        SessionResponse sessionResponse = SessionMapper.convertToResponse(votesYes, votesNo, votesTotal, result);

        Assertions.assertEquals(sessionResponse.getVotesYes(), votesYes);
        Assertions.assertEquals(sessionResponse.getVotesNo(), votesNo);
        Assertions.assertEquals(sessionResponse.getVotesTotal(), votesTotal);
        Assertions.assertEquals(sessionResponse.getResult(), ResultEnum.TIED_VOTE.toString());
    }

    
    @Test
    void convertToResponseNoVotes() {
        this.makeNoVotes();

        SessionResponse sessionResponse = SessionMapper.convertToResponse(votesYes, votesNo, votesTotal, result);

        Assertions.assertEquals(sessionResponse.getVotesYes(), votesYes);
        Assertions.assertEquals(sessionResponse.getVotesNo(), votesNo);
        Assertions.assertEquals(sessionResponse.getVotesTotal(), votesTotal);
        Assertions.assertEquals(sessionResponse.getResult(), ResultEnum.NO_VOTES.toString());
    }

    private void makeYesWins() {
        this.votesYes = 2;
        this.votesNo = 1;
        this.votesTotal = 3; 
        this.result = 1;
    }

    private void makeNoWins() {
        this.votesYes = 1;
        this.votesNo = 2;
        this.votesTotal = 3; 
        this.result = -1;
    }

    private void makeTied() {
        this.votesYes = 2;
        this.votesNo = 2;
        this.votesTotal = 4; 
        this.result = 0;
    }

    
    private void makeNoVotes() {
        this.votesYes = 0;
        this.votesNo = 0;
        this.votesTotal = 0; 
        this.result = null;
    }
}