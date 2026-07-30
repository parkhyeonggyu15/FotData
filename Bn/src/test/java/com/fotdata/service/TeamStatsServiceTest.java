package com.fotdata.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Pageable;
import org.springframework.test.util.ReflectionTestUtils;

import com.fotdata.entity.League;
import com.fotdata.entity.Match;
import com.fotdata.entity.MatchStatus;
import com.fotdata.entity.Team;
import com.fotdata.entity.TeamStats;
import com.fotdata.repository.MatchRepository;
import com.fotdata.repository.TeamRepository;
import com.fotdata.repository.TeamStatsRepository;

@ExtendWith(MockitoExtension.class)
class TeamStatsServiceTest {

    @Mock
    private TeamRepository teamRepository;

    @Mock
    private MatchRepository matchRepository;

    @Mock
    private TeamStatsRepository teamStatsRepository;

    private TeamStatsService teamStatsService;

    private final League league = new League("PL", "Premier League");
    private final Team team = teamWithId(1L, "Arsenal");
    private final Team opponent = teamWithId(2L, "Chelsea");

    @Test
    void 경기결과를_바탕으로_승무패와_득실점을_집계한다() {
        teamStatsService = new TeamStatsService(teamRepository, matchRepository, teamStatsRepository);
        String season = "2025-2026";

        Match win = matchOf(team, opponent, 3, 1, LocalDateTime.of(2025, 9, 1, 0, 0));
        Match draw = matchOf(opponent, team, 1, 1, LocalDateTime.of(2025, 9, 8, 0, 0));
        Match lose = matchOf(team, opponent, 0, 2, LocalDateTime.of(2025, 9, 15, 0, 0));

        when(teamRepository.findById(1L)).thenReturn(Optional.of(team));
        when(matchRepository.findAllFinishedByTeamId(1L, MatchStatus.FINISHED))
                .thenReturn(List.of(win, draw, lose));
        when(matchRepository.findRecentFinishedByTeamId(any(), any(), any(Pageable.class)))
                .thenReturn(List.of(lose, draw, win));
        when(teamStatsRepository.findByTeamIdAndSeason(1L, season))
                .thenReturn(Optional.of(new TeamStats(team, season)));

        teamStatsService.recalculate(1L, season);

        TeamStats stats = teamStatsRepository.findByTeamIdAndSeason(1L, season).orElseThrow();
        assertThat(stats.getPlayed()).isEqualTo(3);
        assertThat(stats.getWin()).isEqualTo(1);
        assertThat(stats.getDraw()).isEqualTo(1);
        assertThat(stats.getLose()).isEqualTo(1);
        assertThat(stats.getGoalsFor()).isEqualTo(3 + 1 + 0);
        assertThat(stats.getGoalsAgainst()).isEqualTo(1 + 1 + 2);
        assertThat(stats.getRecentForm()).isEqualTo("WDL");
    }

    @Test
    void 존재하지_않는_팀_통계를_조회하면_예외가_발생한다() {
        teamStatsService = new TeamStatsService(teamRepository, matchRepository, teamStatsRepository);
        when(teamStatsRepository.findByTeamIdAndSeason(anyLong(), any()))
                .thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class,
                () -> teamStatsService.getTeamStats(999L, "2025-2026"));
    }

    private Match matchOf(Team home, Team away, int homeScore, int awayScore, LocalDateTime date) {
        Match match = new Match(league, home, away, date, MatchStatus.SCHEDULED, 1);
        match.updateResult(MatchStatus.FINISHED, homeScore, awayScore);
        return match;
    }

    private Team teamWithId(Long id, String name) {
        Team team = new Team(name, league, "crest.png");
        ReflectionTestUtils.setField(team, "id", id);
        return team;
    }
}
