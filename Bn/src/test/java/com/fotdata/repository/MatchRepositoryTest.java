package com.fotdata.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.data.domain.PageRequest;

import com.fotdata.entity.League;
import com.fotdata.entity.Match;
import com.fotdata.entity.MatchStatus;
import com.fotdata.entity.Team;

@DataJpaTest
class MatchRepositoryTest {

    @Autowired
    private MatchRepository matchRepository;

    @Autowired
    private TeamRepository teamRepository;

    @Autowired
    private LeagueRepository leagueRepository;

    @Test
    void 팀ID로_최근_종료된_경기를_최신순으로_조회한다() {
        League league = leagueRepository.save(new League("PL", "Premier League"));
        Team teamA = teamRepository.save(new Team("Arsenal", league, "crest.png"));
        Team teamB = teamRepository.save(new Team("Chelsea", league, "crest2.png"));

        Match older = matchRepository.save(finishedMatch(league, teamA, teamB, LocalDateTime.of(2025, 9, 1, 0, 0)));
        Match newer = matchRepository.save(finishedMatch(league, teamB, teamA, LocalDateTime.of(2025, 9, 8, 0, 0)));

        List<Match> result = matchRepository.findRecentFinishedByTeamId(
                teamA.getId(), MatchStatus.FINISHED, PageRequest.of(0, 5));

        assertThat(result).extracting(Match::getId)
                .containsExactly(newer.getId(), older.getId());
    }

    @Test
    void 리그와_시즌과_라운드로_경기를_조회한다() {
        League league = leagueRepository.save(new League("PL", "Premier League"));
        Team teamA = teamRepository.save(new Team("Arsenal", league, "crest.png"));
        Team teamB = teamRepository.save(new Team("Chelsea", league, "crest2.png"));

        Match match = new Match(league, teamA, teamB, LocalDateTime.of(2025, 9, 1, 0, 0),
                MatchStatus.SCHEDULED, 3, "2025-2026");
        matchRepository.save(match);

        List<Match> result = matchRepository.findByLeagueIdAndSeasonAndMatchday(league.getId(), "2025-2026", 3);

        assertThat(result).hasSize(1);
    }

    private Match finishedMatch(League league, Team home, Team away, LocalDateTime date) {
        Match match = new Match(league, home, away, date, MatchStatus.SCHEDULED, 1, "2025-2026");
        match.updateResult(MatchStatus.FINISHED, 1, 0);
        return match;
    }
}
