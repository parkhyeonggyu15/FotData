package com.fotdata.controller;

import java.util.List;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fotdata.dto.response.EloResponse;
import com.fotdata.service.EloService;

import jakarta.validation.constraints.Positive;

@Validated
@RestController
public class EloController {

    private final EloService eloService;

    public EloController(EloService eloService) {
        this.eloService = eloService;
    }

    @GetMapping("/api/teams/{teamId}/elo")
    public EloResponse getTeamElo(@PathVariable @Positive Long teamId) {
        return eloService.getTeamElo(teamId);
    }

    @GetMapping("/api/analysis/elo-rankings")
    public List<EloResponse> getEloRankings(@RequestParam @Positive Long leagueId) {
        return eloService.getRankingsByLeague(leagueId);
    }
}
