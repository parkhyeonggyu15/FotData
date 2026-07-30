package com.fotdata.scheduler;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.fotdata.service.MatchSyncService;
import com.fotdata.service.SeasonCalculator;
import com.fotdata.service.TeamStatsService;

@Component
public class MatchSyncScheduler {

    private static final Logger log = LoggerFactory.getLogger(MatchSyncScheduler.class);

    private static final List<String> COMPETITION_CODES = List.of("PL", "PD", "SA", "BL1", "FL1", "CL");

    private final MatchSyncService matchSyncService;
    private final TeamStatsService teamStatsService;

    public MatchSyncScheduler(MatchSyncService matchSyncService, TeamStatsService teamStatsService) {
        this.matchSyncService = matchSyncService;
        this.teamStatsService = teamStatsService;
    }

    @Scheduled(cron = "0 0 6,18 * * *")
    public void syncAllCompetitions() {
        Set<Long> teamIdsToRecalculate = new HashSet<>();
        String season = SeasonCalculator.currentSeason();
        int seasonStartYear = SeasonCalculator.startYear(season);

        for (String code : COMPETITION_CODES) {
            try {
                teamIdsToRecalculate.addAll(matchSyncService.syncCompetition(code, seasonStartYear));
            } catch (Exception e) {
                log.error("Failed to sync competition: {}", code, e);
            }
        }

        for (Long teamId : teamIdsToRecalculate) {
            try {
                teamStatsService.recalculate(teamId, season);
            } catch (Exception e) {
                log.error("Failed to recalculate stats for team: {}", teamId, e);
            }
        }
    }
}
