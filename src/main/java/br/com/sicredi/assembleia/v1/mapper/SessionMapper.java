package br.com.sicredi.assembleia.v1.mapper;

import br.com.sicredi.assembleia.util.ResultEnum;
import br.com.sicredi.assembleia.v1.dto.response.SessionResponse;

public class SessionMapper {
    private SessionMapper() {
        throw new IllegalStateException("Utility class");
    }

    public static SessionResponse convertToResponse(int votesYes, int votesNo, int votesTotal, Integer result) {
        return SessionResponse.builder()
                               .votesTotal(votesTotal)
                               .votesNo(votesNo)
                               .votesYes(votesYes)
                               .result(generateResult(result).toString()).build();
    }

    public static ResultEnum generateResult(Integer result) {
        if (result == null) {
			return ResultEnum.NO_VOTES;
		} else if (result == 0) {
			return ResultEnum.TIED_VOTE;
		} else if (result < 0) {
			return ResultEnum.NO;
		}
		return ResultEnum.YES;
    }
}