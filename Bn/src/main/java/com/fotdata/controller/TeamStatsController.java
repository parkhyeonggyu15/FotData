package com.fotdata.controller;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fotdata.dto.response.TeamStatsResponse;
import com.fotdata.service.TeamStatsService;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;

@Validated
@RestController
@RequestMapping("/api/teams/{teamId}/stats")
public class TeamStatsController {

    private static final String SEASON_PATTERN = "\\d{4}-\\d{4}";

    private final TeamStatsService teamStatsService;

    public TeamStatsController(TeamStatsService teamStatsService) {
        this.teamStatsService = teamStatsService;
    }

    @GetMapping
    public TeamStatsResponse getTeamStats(
            @PathVariable @Positive Long teamId,
            @RequestParam @Pattern(regexp = SEASON_PATTERN) String season) {
        return teamStatsService.getTeamStats(teamId, season);
    }
}
