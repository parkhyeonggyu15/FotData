package com.fotdata.dto.response;

public record SeasonPredictionResponse(
        Long teamId,
        String teamName,
        double averageRank,
        double titleProbability,
        double relegationProbability
) {
}
