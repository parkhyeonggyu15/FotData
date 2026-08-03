package com.fotdata.dto.response;

import com.fotdata.entity.TeamStats;

public record RankingResponse(
        Long teamId,
        String teamName,
        String teamCrestUrl,
        int goalsFor,
        int goalsAgainst
) {

    public static RankingResponse from(TeamStats stats) {
        return new RankingResponse(
                stats.getTeam().getId(),
                stats.getTeam().getName(),
                stats.getTeam().getCrestUrl(),
                stats.getGoalsFor(),
                stats.getGoalsAgainst()
        );
    }
}
