package com.fotdata.controller;

import java.util.List;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fotdata.dto.response.PlayerScorerResponse;
import com.fotdata.service.PlayerScorerService;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;

@Validated
@RestController
@RequestMapping("/api/analysis/player-scorers")
public class PlayerScorerController {

    private static final String SEASON_PATTERN = "\\d{4}-\\d{4}";

    private final PlayerScorerService playerScorerService;

    public PlayerScorerController(PlayerScorerService playerScorerService) {
        this.playerScorerService = playerScorerService;
    }

    @GetMapping
    public List<PlayerScorerResponse> getTopScorers(
            @RequestParam @Positive Long leagueId,
            @RequestParam @Pattern(regexp = SEASON_PATTERN) String season) {
        return playerScorerService.getTopScorers(leagueId, season);
    }
}
