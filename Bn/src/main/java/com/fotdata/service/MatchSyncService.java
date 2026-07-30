package com.fotdata.service;

import java.time.LocalDateTime;
import java.time.ZoneId;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fotdata.dto.external.MatchExternalResponse;
import com.fotdata.dto.external.MatchListExternalResponse;
import com.fotdata.entity.League;
import com.fotdata.entity.Match;
import com.fotdata.entity.MatchStatus;
import com.fotdata.entity.Team;
import com.fotdata.repository.LeagueRepository;
import com.fotdata.repository.MatchRepository;
import com.fotdata.repository.TeamRepository;

@Service
public class MatchSyncService {

    private final FootballDataApiClient apiClient;
    private final LeagueRepository leagueRepository;
    private final TeamRepository teamRepository;
    private final MatchRepository matchRepository;

    public MatchSyncService(FootballDataApiClient apiClient,
                             LeagueRepository leagueRepository,
                             TeamRepository teamRepository,
                             MatchRepository matchRepository) {
        this.apiClient = apiClient;
        this.leagueRepository = leagueRepository;
        this.teamRepository = teamRepository;
        this.matchRepository = matchRepository;
    }

    @Transactional
    public void syncCompetition(String competitionCode) {
        MatchListExternalResponse response = apiClient.fetchMatches(competitionCode);
        for (MatchExternalResponse externalMatch : response.matches()) {
            upsertMatch(externalMatch);
        }
    }

    private void upsertMatch(MatchExternalResponse externalMatch) {
        League league = findOrCreateLeague(externalMatch);
        Team homeTeam = findOrCreateTeam(externalMatch.homeTeam().name(),
                externalMatch.homeTeam().crest(), league);
        Team awayTeam = findOrCreateTeam(externalMatch.awayTeam().name(),
                externalMatch.awayTeam().crest(), league);

        LocalDateTime matchDate = externalMatch.utcDate()
                .atZoneSameInstant(ZoneId.of("Asia/Seoul"))
                .toLocalDateTime();
        MatchStatus status = MatchStatus.fromExternalStatus(externalMatch.status());
        Integer homeScore = externalMatch.score().fullTime().home();
        Integer awayScore = externalMatch.score().fullTime().away();

        Match match = matchRepository
                .findByHomeTeamIdAndAwayTeamIdAndMatchDate(homeTeam.getId(), awayTeam.getId(), matchDate)
                .orElseGet(() -> matchRepository.save(
                        new Match(league, homeTeam, awayTeam, matchDate, status, externalMatch.matchday())));

        match.updateResult(status, homeScore, awayScore);
    }

    private League findOrCreateLeague(MatchExternalResponse externalMatch) {
        String code = externalMatch.competition().code();
        return leagueRepository.findByCode(code)
                .orElseGet(() -> leagueRepository.save(
                        new League(code, externalMatch.competition().name())));
    }

    private Team findOrCreateTeam(String name, String crestUrl, League league) {
        return teamRepository.findByNameAndLeagueId(name, league.getId())
                .orElseGet(() -> teamRepository.save(new Team(name, league, crestUrl)));
    }
}
