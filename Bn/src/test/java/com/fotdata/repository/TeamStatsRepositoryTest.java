package com.fotdata.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.data.domain.PageRequest;

import com.fotdata.entity.League;
import com.fotdata.entity.Team;
import com.fotdata.entity.TeamStats;

@DataJpaTest
class TeamStatsRepositoryTest {

    @Autowired
    private TeamStatsRepository teamStatsRepository;

    @Autowired
    private TeamRepository teamRepository;

    @Autowired
    private LeagueRepository leagueRepository;

    @Test
    void 리그내_득점상위_팀을_득점순으로_조회한다() {
        League league = leagueRepository.save(new League("PL", "Premier League"));
        Team teamA = teamRepository.save(new Team("Arsenal", league, "crest.png"));
        Team teamB = teamRepository.save(new Team("Chelsea", league, "crest2.png"));
        String season = "2025-2026";

        TeamStats statsA = new TeamStats(teamA, season);
        statsA.update(3, 2, 1, 0, 8, 3, 4, 2, 4, 1, "WWD");
        teamStatsRepository.save(statsA);

        TeamStats statsB = new TeamStats(teamB, season);
        statsB.update(3, 1, 1, 1, 5, 4, 2, 2, 3, 2, "WDL");
        teamStatsRepository.save(statsB);

        List<TeamStats> result = teamStatsRepository.findTopScorersByLeague(
                league.getId(), season, PageRequest.of(0, 10));

        assertThat(result).extracting(TeamStats::getGoalsFor)
                .containsExactly(8, 5);
    }

    @Test
    void 팀ID와_시즌으로_통계를_조회한다() {
        League league = leagueRepository.save(new League("PL", "Premier League"));
        Team team = teamRepository.save(new Team("Arsenal", league, "crest.png"));
        String season = "2025-2026";
        teamStatsRepository.save(new TeamStats(team, season));

        var result = teamStatsRepository.findByTeamIdAndSeason(team.getId(), season);

        assertThat(result).isPresent();
    }
}
