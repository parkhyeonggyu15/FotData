package com.fotdata.dto.response;

public record MatchPredictionResponse(
        Long homeTeamId,
        Long awayTeamId,
        double homeWinProbability,
        double drawProbability,
        double awayWinProbability
) {
}
