package com.fotdata.dto.response;

public record PlayerGoalPredictionResponse(
        Long playerId,
        String playerName,
        Long teamId,
        String teamName,
        String teamCrestUrl,
        double predictedGoals
) {
}
