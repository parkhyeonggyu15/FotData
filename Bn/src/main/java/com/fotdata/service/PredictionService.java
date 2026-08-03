package com.fotdata.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fotdata.dto.response.MatchPredictionResponse;
import com.fotdata.dto.response.PlayerGoalPredictionResponse;
import com.fotdata.dto.response.SeasonPredictionResponse;
import com.fotdata.entity.League;
import com.fotdata.entity.Match;
import com.fotdata.entity.MatchStatus;
import com.fotdata.entity.PlayerScorer;
import com.fotdata.entity.Team;
import com.fotdata.entity.TeamElo;
import com.fotdata.repository.LeagueRepository;
import com.fotdata.repository.MatchRepository;
import com.fotdata.repository.PlayerScorerRepository;
import com.fotdata.repository.TeamEloRepository;

@Service
@Transactional(readOnly = true)
public class PredictionService {

    private static final int SIMULATION_ITERATIONS = 2000;
    private static final String UEFA_CHAMPIONS_LEAGUE_CODE = "CL";

    private final TeamEloRepository teamEloRepository;
    private final MatchRepository matchRepository;
    private final PlayerScorerRepository playerScorerRepository;
    private final LeagueRepository leagueRepository;

    public PredictionService(TeamEloRepository teamEloRepository, MatchRepository matchRepository,
                              PlayerScorerRepository playerScorerRepository, LeagueRepository leagueRepository) {
        this.teamEloRepository = teamEloRepository;
        this.matchRepository = matchRepository;
        this.playerScorerRepository = playerScorerRepository;
        this.leagueRepository = leagueRepository;
    }

    public MatchPredictionResponse predictMatch(Long homeTeamId, Long awayTeamId) {
        double homeRating = ratingOf(homeTeamId);
        double awayRating = ratingOf(awayTeamId);

        MatchPredictor.Prediction prediction = MatchPredictor.predict(homeRating, awayRating);
        return new MatchPredictionResponse(
                homeTeamId, awayTeamId,
                prediction.homeWinProbability(), prediction.drawProbability(), prediction.awayWinProbability());
    }

    public List<SeasonPredictionResponse> predictSeason(Long leagueId, String season) {
        assertLeagueSupportsPrediction(leagueId);

        List<Match> scheduledMatches = matchRepository
                .findByLeagueIdAndSeasonAndStatusOrderByMatchDateAsc(leagueId, season, MatchStatus.SCHEDULED);

        Map<Long, Double> initialRatings = new HashMap<>();
        Map<Long, Team> teams = new HashMap<>();
        for (Match match : scheduledMatches) {
            registerTeam(match.getHomeTeam(), initialRatings, teams);
            registerTeam(match.getAwayTeam(), initialRatings, teams);
        }

        List<SeasonSimulator.Fixture> fixtures = scheduledMatches.stream()
                .map(m -> new SeasonSimulator.Fixture(m.getHomeTeam().getId(), m.getAwayTeam().getId()))
                .toList();

        Map<Long, SeasonSimulator.TeamSimulationResult> results =
                SeasonSimulator.simulate(fixtures, initialRatings, SIMULATION_ITERATIONS);

        return results.entrySet().stream()
                .map(entry -> new SeasonPredictionResponse(
                        entry.getKey(),
                        teams.get(entry.getKey()).getName(),
                        teams.get(entry.getKey()).getCrestUrl(),
                        entry.getValue().averageRank(),
                        entry.getValue().titleProbability(),
                        entry.getValue().relegationProbability()))
                .sorted((a, b) -> Double.compare(a.averageRank(), b.averageRank()))
                .toList();
    }

    public List<PlayerGoalPredictionResponse> predictTopScorers(Long leagueId, String baseSeason, int projectedMatches) {
        assertLeagueSupportsPrediction(leagueId);

        List<PlayerScorer> lastSeasonScorers = playerScorerRepository
                .findByLeagueIdAndSeasonOrderByGoalsDesc(leagueId, baseSeason);

        return lastSeasonScorers.stream()
                .map(scorer -> new PlayerGoalPredictionResponse(
                        scorer.getPlayer().getId(),
                        scorer.getPlayer().getName(),
                        scorer.getTeam().getId(),
                        scorer.getTeam().getName(),
                        scorer.getTeam().getCrestUrl(),
                        PlayerGoalPredictor.predictGoals(scorer.getGoals(), scorer.getPlayedMatches(), projectedMatches)))
                .sorted((a, b) -> Double.compare(b.predictedGoals(), a.predictedGoals()))
                .toList();
    }

    private void assertLeagueSupportsPrediction(Long leagueId) {
        League league = leagueRepository.findById(leagueId)
                .orElseThrow(() -> new IllegalArgumentException("League not found: " + leagueId));
        if (UEFA_CHAMPIONS_LEAGUE_CODE.equals(league.getCode())) {
            throw new IllegalArgumentException("Season prediction is not supported for tournament-style competitions");
        }
    }

    private double ratingOf(Long teamId) {
        return teamEloRepository.findByTeamId(teamId)
                .map(TeamElo::getRating)
                .orElse(TeamElo.INITIAL_RATING);
    }

    private void registerTeam(Team team, Map<Long, Double> initialRatings, Map<Long, Team> teams) {
        initialRatings.putIfAbsent(team.getId(), ratingOf(team.getId()));
        teams.putIfAbsent(team.getId(), team);
    }
}
