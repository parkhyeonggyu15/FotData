package com.fotdata.dto.response;

import com.fotdata.entity.TeamStats;

public record TeamStatsResponse(
        Long teamId,
        String teamName,
        String teamCrestUrl,
        String season,
        int played,
        int win,
        int draw,
        int lose,
        int goalsFor,
        int goalsAgainst,
        int homeGoalDifference,
        int awayGoalDifference,
        String recentForm
) {

    public static TeamStatsResponse from(TeamStats stats) {
        return new TeamStatsResponse(
                stats.getTeam().getId(),
                stats.getTeam().getName(),
                stats.getTeam().getCrestUrl(),
                stats.getSeason(),
                stats.getPlayed(),
                stats.getWin(),
                stats.getDraw(),
                stats.getLose(),
                stats.getGoalsFor(),
                stats.getGoalsAgainst(),
                stats.getHomeGoalsFor() - stats.getHomeGoalsAgainst(),
                stats.getAwayGoalsFor() - stats.getAwayGoalsAgainst(),
                stats.getRecentForm()
        );
    }
}
