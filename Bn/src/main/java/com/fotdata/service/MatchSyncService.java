package com.fotdata.service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

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
    private final EloService eloService;

    public MatchSyncService(FootballDataApiClient apiClient,
                             LeagueRepository leagueRepository,
                             TeamRepository teamRepository,
                             MatchRepository matchRepository,
                             EloService eloService) {
        this.apiClient = apiClient;
        this.leagueRepository = leagueRepository;
        this.teamRepository = teamRepository;
        this.matchRepository = matchRepository;
        this.eloService = eloService;
    }

    @Transactional
    public Set<Long> syncCompetition(String competitionCode, int seasonStartYear) {
        MatchListExternalResponse response = apiClient.fetchMatches(competitionCode, seasonStartYear);
        String season = seasonStartYear + "-" + (seasonStartYear + 1);
        Set<Long> newlyFinishedTeamIds = new HashSet<>();
        for (MatchExternalResponse externalMatch : response.matches()) {
            upsertMatch(externalMatch, season, newlyFinishedTeamIds);
        }
        return newlyFinishedTeamIds;
    }

    private void upsertMatch(MatchExternalResponse externalMatch, String season, Set<Long> newlyFinishedTeamIds) {
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

        Optional<Match> existingMatch = matchRepository
                .findByHomeTeamIdAndAwayTeamIdAndMatchDate(homeTeam.getId(), awayTeam.getId(), matchDate);
        boolean wasFinished = existingMatch.map(m -> m.getStatus() == MatchStatus.FINISHED).orElse(false);
        Match match = existingMatch.orElseGet(() -> matchRepository.save(
                new Match(league, homeTeam, awayTeam, matchDate, status, externalMatch.matchday(), season)));

        match.updateResult(status, homeScore, awayScore);

        if (!wasFinished && status == MatchStatus.FINISHED) {
            newlyFinishedTeamIds.add(homeTeam.getId());
            newlyFinishedTeamIds.add(awayTeam.getId());
            eloService.applyMatchResult(homeTeam, awayTeam, homeScore, awayScore);
        }
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
