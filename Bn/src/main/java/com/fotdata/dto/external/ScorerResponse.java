package com.fotdata.dto.external;

public record ScorerResponse(
        PlayerResponse player,
        TeamResponse team,
        Integer playedMatches,
        Integer goals,
        Integer assists
) {
}
