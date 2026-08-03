package com.fotdata.dto.response;

import com.fotdata.entity.PlayerScorer;

public record PlayerScorerResponse(
        Long playerId,
        String playerName,
        Long teamId,
        String teamName,
        String teamCrestUrl,
        int goals,
        int assists,
        int playedMatches
) {

    public static PlayerScorerResponse from(PlayerScorer scorer) {
        return new PlayerScorerResponse(
                scorer.getPlayer().getId(),
                scorer.getPlayer().getName(),
                scorer.getTeam().getId(),
                scorer.getTeam().getName(),
                scorer.getTeam().getCrestUrl(),
                scorer.getGoals(),
                scorer.getAssists(),
                scorer.getPlayedMatches()
        );
    }
}
