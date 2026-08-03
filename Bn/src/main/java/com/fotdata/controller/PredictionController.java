package com.fotdata.controller;

import java.util.List;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fotdata.dto.response.MatchPredictionResponse;
import com.fotdata.dto.response.PlayerGoalPredictionResponse;
import com.fotdata.dto.response.SeasonPredictionResponse;
import com.fotdata.service.PredictionService;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;

@Validated
@RestController
@RequestMapping("/api/predictions")
public class PredictionController {

    private static final String SEASON_PATTERN = "\\d{4}-\\d{4}";

    private final PredictionService predictionService;

    public PredictionController(PredictionService predictionService) {
        this.predictionService = predictionService;
    }

    @GetMapping("/match")
    public MatchPredictionResponse predictMatch(
            @RequestParam @Positive Long homeTeamId,
            @RequestParam @Positive Long awayTeamId) {
        return predictionService.predictMatch(homeTeamId, awayTeamId);
    }

    @GetMapping("/season")
    public List<SeasonPredictionResponse> predictSeason(
            @RequestParam @Positive Long leagueId,
            @RequestParam @Pattern(regexp = SEASON_PATTERN) String season) {
        return predictionService.predictSeason(leagueId, season);
    }

    @GetMapping("/top-scorers")
    public List<PlayerGoalPredictionResponse> predictTopScorers(
            @RequestParam @Positive Long leagueId,
            @RequestParam @Pattern(regexp = SEASON_PATTERN) String baseSeason,
            @RequestParam(defaultValue = "38") @Positive Integer totalMatches) {
        return predictionService.predictTopScorers(leagueId, baseSeason, totalMatches);
    }
}
