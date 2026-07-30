package com.fotdata.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fotdata.dto.response.TeamStatsResponse;
import com.fotdata.service.TeamStatsService;

@RestController
@RequestMapping("/api/teams/{teamId}/stats")
public class TeamStatsController {

    private final TeamStatsService teamStatsService;

    public TeamStatsController(TeamStatsService teamStatsService) {
        this.teamStatsService = teamStatsService;
    }

    @GetMapping
    public TeamStatsResponse getTeamStats(
            @PathVariable Long teamId,
            @RequestParam String season) {
        return teamStatsService.getTeamStats(teamId, season);
    }
}
