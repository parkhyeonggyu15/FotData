package com.fotdata.service;

import java.util.List;

import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fotdata.dto.response.TeamStatsResponse;
import com.fotdata.entity.Match;
import com.fotdata.entity.MatchStatus;
import com.fotdata.entity.Team;
import com.fotdata.entity.TeamStats;
import com.fotdata.repository.MatchRepository;
import com.fotdata.repository.TeamRepository;
import com.fotdata.repository.TeamStatsRepository;

@Service
public class TeamStatsService {

    private static final int RECENT_FORM_SIZE = 5;

    private final TeamRepository teamRepository;
    private final MatchRepository matchRepository;
    private final TeamStatsRepository teamStatsRepository;

    public TeamStatsService(TeamRepository teamRepository,
                             MatchRepository matchRepository,
                             TeamStatsRepository teamStatsRepository) {
        this.teamRepository = teamRepository;
        this.matchRepository = matchRepository;
        this.teamStatsRepository = teamStatsRepository;
    }

    @Transactional(readOnly = true)
    public TeamStatsResponse getTeamStats(Long teamId, String season) {
        TeamStats stats = teamStatsRepository.findByTeamIdAndSeason(teamId, season)
                .orElseThrow(() -> new IllegalArgumentException(
                        "TeamStats not found for team %d, season %s".formatted(teamId, season)));
        return TeamStatsResponse.from(stats);
    }

    @Transactional
    public void recalculate(Long teamId, String season) {
        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new IllegalArgumentException("Team not found: " + teamId));

        List<Match> finishedMatches = matchRepository.findAllFinishedByTeamId(teamId, MatchStatus.FINISHED);

        int played = 0;
        int win = 0;
        int draw = 0;
        int lose = 0;
        int goalsFor = 0;
        int goalsAgainst = 0;
        int homeGoalsFor = 0;
        int homeGoalsAgainst = 0;
        int awayGoalsFor = 0;
        int awayGoalsAgainst = 0;

        for (Match match : finishedMatches) {
            boolean isHome = match.getHomeTeam().getId().equals(teamId);
            int teamScore = isHome ? match.getHomeScore() : match.getAwayScore();
            int opponentScore = isHome ? match.getAwayScore() : match.getHomeScore();

            played++;
            goalsFor += teamScore;
            goalsAgainst += opponentScore;
            if (isHome) {
                homeGoalsFor += teamScore;
                homeGoalsAgainst += opponentScore;
            } else {
                awayGoalsFor += teamScore;
                awayGoalsAgainst += opponentScore;
            }

            if (teamScore > opponentScore) {
                win++;
            } else if (teamScore == opponentScore) {
                draw++;
            } else {
                lose++;
            }
        }

        String recentForm = buildRecentForm(teamId);

        TeamStats stats = teamStatsRepository.findByTeamIdAndSeason(teamId, season)
                .orElseGet(() -> teamStatsRepository.save(new TeamStats(team, season)));

        stats.update(played, win, draw, lose, goalsFor, goalsAgainst,
                homeGoalsFor, homeGoalsAgainst, awayGoalsFor, awayGoalsAgainst, recentForm);
    }

    private String buildRecentForm(Long teamId) {
        List<Match> recentMatches = matchRepository.findRecentFinishedByTeamId(
                teamId, MatchStatus.FINISHED, PageRequest.of(0, RECENT_FORM_SIZE));

        StringBuilder form = new StringBuilder();
        for (int i = recentMatches.size() - 1; i >= 0; i--) {
            Match match = recentMatches.get(i);
            boolean isHome = match.getHomeTeam().getId().equals(teamId);
            int teamScore = isHome ? match.getHomeScore() : match.getAwayScore();
            int opponentScore = isHome ? match.getAwayScore() : match.getHomeScore();

            if (teamScore > opponentScore) {
                form.append('W');
            } else if (teamScore == opponentScore) {
                form.append('D');
            } else {
                form.append('L');
            }
        }
        return form.toString();
    }
}
