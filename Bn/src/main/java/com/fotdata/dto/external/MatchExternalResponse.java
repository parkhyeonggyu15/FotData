package com.fotdata.dto.external;

import java.time.OffsetDateTime;

public record MatchExternalResponse(
        Long id,
        OffsetDateTime utcDate,
        String status,
        Integer matchday,
        TeamResponse homeTeam,
        TeamResponse awayTeam,
        ScoreResponse score,
        CompetitionResponse competition
) {
}
