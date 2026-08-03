package com.fotdata.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fotdata.dto.external.ScorerListExternalResponse;
import com.fotdata.dto.external.ScorerResponse;
import com.fotdata.entity.League;
import com.fotdata.entity.Player;
import com.fotdata.entity.PlayerScorer;
import com.fotdata.entity.Team;
import com.fotdata.repository.LeagueRepository;
import com.fotdata.repository.PlayerRepository;
import com.fotdata.repository.PlayerScorerRepository;
import com.fotdata.repository.TeamRepository;

@Service
public class PlayerSyncService {

    private final FootballDataApiClient apiClient;
    private final LeagueRepository leagueRepository;
    private final TeamRepository teamRepository;
    private final PlayerRepository playerRepository;
    private final PlayerScorerRepository playerScorerRepository;

    public PlayerSyncService(FootballDataApiClient apiClient,
                              LeagueRepository leagueRepository,
                              TeamRepository teamRepository,
                              PlayerRepository playerRepository,
                              PlayerScorerRepository playerScorerRepository) {
        this.apiClient = apiClient;
        this.leagueRepository = leagueRepository;
        this.teamRepository = teamRepository;
        this.playerRepository = playerRepository;
        this.playerScorerRepository = playerScorerRepository;
    }

    @Transactional
    public void syncTopScorers(String competitionCode, int seasonStartYear) {
        League league = leagueRepository.findByCode(competitionCode)
                .orElseThrow(() -> new IllegalArgumentException("League not found: " + competitionCode));
        String season = seasonStartYear + "-" + (seasonStartYear + 1);

        ScorerListExternalResponse response = apiClient.fetchTopScorers(competitionCode, seasonStartYear);
        for (ScorerResponse scorer : response.scorers()) {
            upsertScorer(scorer, league, season);
        }
    }

    private void upsertScorer(ScorerResponse scorer, League league, String season) {
        Player player = playerRepository.findById(scorer.player().id())
                .orElseGet(() -> playerRepository.save(new Player(scorer.player().id(), scorer.player().name())));
        player.rename(scorer.player().name());

        Team team = teamRepository.findByNameAndLeagueId(scorer.team().name(), league.getId())
                .orElseGet(() -> teamRepository.save(new Team(scorer.team().name(), league, scorer.team().crest())));

        int goals = scorer.goals() != null ? scorer.goals() : 0;
        int assists = scorer.assists() != null ? scorer.assists() : 0;
        int playedMatches = scorer.playedMatches() != null ? scorer.playedMatches() : 0;

        playerScorerRepository.findByPlayerIdAndLeagueIdAndSeason(player.getId(), league.getId(), season)
                .ifPresentOrElse(
                        existing -> existing.update(team, goals, assists, playedMatches),
                        () -> playerScorerRepository.save(
                                new PlayerScorer(player, team, league, season, goals, assists, playedMatches)));
    }
}
