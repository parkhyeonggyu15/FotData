package com.fotdata.dto.response;

public record SeasonPredictionResponse(
        Long teamId,
        String teamName,
        String teamCrestUrl,
        double averageRank,
        double titleProbability,
        double relegationProbability
) {
}
