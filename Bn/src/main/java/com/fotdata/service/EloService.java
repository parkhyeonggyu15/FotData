package com.fotdata.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fotdata.dto.response.EloResponse;
import com.fotdata.entity.Team;
import com.fotdata.entity.TeamElo;
import com.fotdata.repository.TeamEloRepository;

@Service
public class EloService {

    private final TeamEloRepository teamEloRepository;

    public EloService(TeamEloRepository teamEloRepository) {
        this.teamEloRepository = teamEloRepository;
    }

    @Transactional
    public void applyMatchResult(Team homeTeam, Team awayTeam, int homeScore, int awayScore) {
        TeamElo homeElo = findOrCreateElo(homeTeam);
        TeamElo awayElo = findOrCreateElo(awayTeam);

        EloCalculator.MatchResult result = EloCalculator.MatchResult.from(homeScore, awayScore);
        double[] newRatings = EloCalculator.calculateNewRatings(homeElo.getRating(), awayElo.getRating(), result);

        homeElo.updateRating(newRatings[0]);
        awayElo.updateRating(newRatings[1]);
    }

    @Transactional(readOnly = true)
    public EloResponse getTeamElo(Long teamId) {
        TeamElo teamElo = teamEloRepository.findByTeamId(teamId)
                .orElseThrow(() -> new IllegalArgumentException("TeamElo not found for team " + teamId));
        return EloResponse.from(teamElo);
    }

    @Transactional(readOnly = true)
    public List<EloResponse> getRankingsByLeague(Long leagueId) {
        return teamEloRepository.findByLeagueIdOrderByRatingDesc(leagueId)
                .stream()
                .map(EloResponse::from)
                .toList();
    }

    private TeamElo findOrCreateElo(Team team) {
        return teamEloRepository.findByTeamId(team.getId())
                .orElseGet(() -> teamEloRepository.save(new TeamElo(team)));
    }
}
