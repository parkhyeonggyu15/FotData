package com.fotdata.dto.response;

import java.util.List;

public record H2HResponse(
        Long teamAId,
        Long teamBId,
        int teamAWins,
        int teamBWins,
        int draws,
        List<MatchResponse> recentMatches
) {
}
