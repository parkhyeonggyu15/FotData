package com.fotdata.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Pageable;
import org.springframework.test.util.ReflectionTestUtils;

import com.fotdata.dto.response.H2HResponse;
import com.fotdata.entity.League;
import com.fotdata.entity.Match;
import com.fotdata.entity.MatchStatus;
import com.fotdata.entity.Team;
import com.fotdata.repository.MatchRepository;
import com.fotdata.repository.TeamStatsRepository;

@ExtendWith(MockitoExtension.class)
class AnalysisServiceTest {

    @Mock
    private MatchRepository matchRepository;

    @Mock
    private TeamStatsRepository teamStatsRepository;

    private final League league = new League("PL", "Premier League");
    private final Team teamA = teamWithId(1L, "Arsenal");
    private final Team teamB = teamWithId(2L, "Chelsea");

    @Test
    void 상대전적을_홈원정_구분없이_팀기준으로_집계한다() {
        AnalysisService analysisService = new AnalysisService(matchRepository, teamStatsRepository);

        Match teamAWinAsHome = matchOf(teamA, teamB, 2, 0);
        Match teamAWinAsAway = matchOf(teamB, teamA, 0, 1);
        Match draw = matchOf(teamA, teamB, 1, 1);

        when(matchRepository.findHeadToHead(any(), any(), any(), any(Pageable.class)))
                .thenReturn(List.of(teamAWinAsHome, teamAWinAsAway, draw));

        H2HResponse result = analysisService.getHeadToHead(1L, 2L);

        assertThat(result.teamAWins()).isEqualTo(2);
        assertThat(result.teamBWins()).isEqualTo(0);
        assertThat(result.draws()).isEqualTo(1);
    }

    private Match matchOf(Team home, Team away, int homeScore, int awayScore) {
        Match match = new Match(league, home, away, LocalDateTime.of(2025, 9, 1, 0, 0),
                MatchStatus.SCHEDULED, 1, "2025-2026");
        match.updateResult(MatchStatus.FINISHED, homeScore, awayScore);
        return match;
    }

    private Team teamWithId(Long id, String name) {
        Team team = new Team(name, league, "crest.png");
        ReflectionTestUtils.setField(team, "id", id);
        return team;
    }
}
