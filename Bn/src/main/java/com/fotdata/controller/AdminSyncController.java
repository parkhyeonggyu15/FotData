package com.fotdata.controller;

import java.util.Set;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fotdata.service.MatchSyncService;
import com.fotdata.service.PlayerSyncService;
import com.fotdata.service.SeasonCalculator;
import com.fotdata.service.TeamStatsService;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

@Validated
@RestController
@RequestMapping("/api/admin")
public class AdminSyncController {

    private static final String SEASON_PATTERN = "\\d{4}-\\d{4}";

    private final MatchSyncService matchSyncService;
    private final TeamStatsService teamStatsService;
    private final PlayerSyncService playerSyncService;

    public AdminSyncController(MatchSyncService matchSyncService, TeamStatsService teamStatsService,
                                PlayerSyncService playerSyncService) {
        this.matchSyncService = matchSyncService;
        this.teamStatsService = teamStatsService;
        this.playerSyncService = playerSyncService;
    }

    @PostMapping("/sync")
    public String syncCompetition(
            @RequestParam @NotBlank String leagueCode,
            @RequestParam(required = false) @Pattern(regexp = SEASON_PATTERN) String season) {
        String targetSeason = season != null ? season : SeasonCalculator.currentSeason();
        int seasonStartYear = SeasonCalculator.startYear(targetSeason);

        Set<Long> teamIdsToRecalculate = matchSyncService.syncCompetition(leagueCode, seasonStartYear);

        for (Long teamId : teamIdsToRecalculate) {
            teamStatsService.recalculate(teamId, targetSeason);
        }

        playerSyncService.syncTopScorers(leagueCode, seasonStartYear);

        return "synced %s season %s, recalculated %d teams".formatted(leagueCode, targetSeason, teamIdsToRecalculate.size());
    }
}
