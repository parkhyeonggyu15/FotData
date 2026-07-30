package com.fotdata.service;

import java.util.List;

import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fotdata.dto.response.H2HResponse;
import com.fotdata.dto.response.MatchResponse;
import com.fotdata.dto.response.RankingResponse;
import com.fotdata.entity.Match;
import com.fotdata.entity.MatchStatus;
import com.fotdata.repository.MatchRepository;
import com.fotdata.repository.TeamStatsRepository;

@Service
@Transactional(readOnly = true)
public class AnalysisService {

    private static final int H2H_RECENT_SIZE = 10;

    private final MatchRepository matchRepository;
    private final TeamStatsRepository teamStatsRepository;

    public AnalysisService(MatchRepository matchRepository, TeamStatsRepository teamStatsRepository) {
        this.matchRepository = matchRepository;
        this.teamStatsRepository = teamStatsRepository;
    }

    public List<RankingResponse> getTopScorers(Long leagueId, String season, int limit) {
        return teamStatsRepository.findTopScorersByLeague(leagueId, season, PageRequest.of(0, limit))
                .stream()
                .map(RankingResponse::from)
                .toList();
    }

    public List<RankingResponse> getTopConceders(Long leagueId, String season, int limit) {
        return teamStatsRepository.findTopConcedersByLeague(leagueId, season, PageRequest.of(0, limit))
                .stream()
                .map(RankingResponse::from)
                .toList();
    }

    public H2HResponse getHeadToHead(Long teamAId, Long teamBId) {
        List<Match> matches = matchRepository.findHeadToHead(
                teamAId, teamBId, MatchStatus.FINISHED, PageRequest.of(0, H2H_RECENT_SIZE));

        int teamAWins = 0;
        int teamBWins = 0;
        int draws = 0;

        for (Match match : matches) {
            boolean teamAIsHome = match.getHomeTeam().getId().equals(teamAId);
            int teamAScore = teamAIsHome ? match.getHomeScore() : match.getAwayScore();
            int teamBScore = teamAIsHome ? match.getAwayScore() : match.getHomeScore();

            if (teamAScore > teamBScore) {
                teamAWins++;
            } else if (teamAScore < teamBScore) {
                teamBWins++;
            } else {
                draws++;
            }
        }

        List<MatchResponse> recentMatches = matches.stream()
                .map(MatchResponse::from)
                .toList();

        return new H2HResponse(teamAId, teamBId, teamAWins, teamBWins, draws, recentMatches);
    }
}
