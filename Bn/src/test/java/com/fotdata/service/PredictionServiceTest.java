package com.fotdata.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.fotdata.dto.response.PlayerGoalPredictionResponse;
import com.fotdata.entity.League;
import com.fotdata.entity.Player;
import com.fotdata.entity.PlayerScorer;
import com.fotdata.entity.Team;
import com.fotdata.repository.LeagueRepository;
import com.fotdata.repository.MatchRepository;
import com.fotdata.repository.PlayerScorerRepository;
import com.fotdata.repository.TeamEloRepository;

@ExtendWith(MockitoExtension.class)
class PredictionServiceTest {

    @Mock
    private TeamEloRepository teamEloRepository;

    @Mock
    private MatchRepository matchRepository;

    @Mock
    private PlayerScorerRepository playerScorerRepository;

    @Mock
    private LeagueRepository leagueRepository;

    @Test
    void UEFA_챔피언스리그는_시즌_순위_예측을_지원하지_않는다() {
        PredictionService predictionService =
                new PredictionService(teamEloRepository, matchRepository, playerScorerRepository, leagueRepository);
        League cl = new League("CL", "UEFA Champions League");
        when(leagueRepository.findById(9L)).thenReturn(Optional.of(cl));

        assertThatThrownBy(() -> predictionService.predictSeason(9L, "2025-2026"))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void UEFA_챔피언스리그는_득점왕_예측을_지원하지_않는다() {
        PredictionService predictionService =
                new PredictionService(teamEloRepository, matchRepository, playerScorerRepository, leagueRepository);
        League cl = new League("CL", "UEFA Champions League");
        when(leagueRepository.findById(9L)).thenReturn(Optional.of(cl));

        assertThatThrownBy(() -> predictionService.predictTopScorers(9L, "2025-2026", 38))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 일반_리그는_득점왕_예측을_정상_계산한다() {
        PredictionService predictionService =
                new PredictionService(teamEloRepository, matchRepository, playerScorerRepository, leagueRepository);
        League league = new League("PL", "Premier League");
        Team team = new Team("Manchester City FC", league, "crest.png");
        Player player = new Player(1L, "Erling Haaland");
        PlayerScorer scorer = new PlayerScorer(player, team, league, "2025-2026", 27, 8, 36);

        when(leagueRepository.findById(1L)).thenReturn(Optional.of(league));
        when(playerScorerRepository.findByLeagueIdAndSeasonOrderByGoalsDesc(1L, "2025-2026"))
                .thenReturn(List.of(scorer));

        List<PlayerGoalPredictionResponse> result = predictionService.predictTopScorers(1L, "2025-2026", 38);

        assertThat(result).hasSize(1);
        assertThat(result.get(0).predictedGoals()).isGreaterThan(27);
    }
}
