package com.fotdata.dto.response;

import java.time.LocalDateTime;

import com.fotdata.entity.Match;
import com.fotdata.entity.MatchStatus;

public record MatchResponse(
        Long id,
        String leagueName,
        String homeTeamName,
        String awayTeamName,
        LocalDateTime matchDate,
        MatchStatus status,
        Integer homeScore,
        Integer awayScore,
        Integer matchday
) {

    public static MatchResponse from(Match match) {
        return new MatchResponse(
                match.getId(),
                match.getLeague().getName(),
                match.getHomeTeam().getName(),
                match.getAwayTeam().getName(),
                match.getMatchDate(),
                match.getStatus(),
                match.getHomeScore(),
                match.getAwayScore(),
                match.getMatchday()
        );
    }
}
