package com.fotdata.controller;

import java.util.Set;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fotdata.service.MatchSyncService;
import com.fotdata.service.SeasonCalculator;
import com.fotdata.service.TeamStatsService;

import jakarta.validation.constraints.NotBlank;

@Validated
@RestController
@RequestMapping("/api/admin")
public class AdminSyncController {

    private final MatchSyncService matchSyncService;
    private final TeamStatsService teamStatsService;

    public AdminSyncController(MatchSyncService matchSyncService, TeamStatsService teamStatsService) {
        this.matchSyncService = matchSyncService;
        this.teamStatsService = teamStatsService;
    }

    @PostMapping("/sync")
    public String syncCompetition(@RequestParam @NotBlank String leagueCode) {
        Set<Long> teamIdsToRecalculate = matchSyncService.syncCompetition(leagueCode);

        String season = SeasonCalculator.currentSeason();
        for (Long teamId : teamIdsToRecalculate) {
            teamStatsService.recalculate(teamId, season);
        }

        return "synced %s, recalculated %d teams".formatted(leagueCode, teamIdsToRecalculate.size());
    }
}
