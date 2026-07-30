package com.fotdata.controller;

import java.util.List;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fotdata.dto.response.LeagueResponse;
import com.fotdata.dto.response.TeamResponse;
import com.fotdata.service.LeagueService;

import jakarta.validation.constraints.Positive;

@Validated
@RestController
@RequestMapping("/api/leagues")
public class LeagueController {

    private final LeagueService leagueService;

    public LeagueController(LeagueService leagueService) {
        this.leagueService = leagueService;
    }

    @GetMapping
    public List<LeagueResponse> getLeagues() {
        return leagueService.getLeagues();
    }

    @GetMapping("/{leagueId}/teams")
    public List<TeamResponse> getTeams(@PathVariable @Positive Long leagueId) {
        return leagueService.getTeamsByLeague(leagueId);
    }
}
