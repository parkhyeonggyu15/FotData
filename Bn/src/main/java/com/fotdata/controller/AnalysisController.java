package com.fotdata.controller;

import java.util.List;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fotdata.dto.response.H2HResponse;
import com.fotdata.dto.response.RankingResponse;
import com.fotdata.service.AnalysisService;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;

@Validated
@RestController
@RequestMapping("/api/analysis")
public class AnalysisController {

    private static final int DEFAULT_RANKING_LIMIT = 10;
    private static final String SEASON_PATTERN = "\\d{4}-\\d{4}";

    private final AnalysisService analysisService;

    public AnalysisController(AnalysisService analysisService) {
        this.analysisService = analysisService;
    }

    @GetMapping("/rankings/top-scorers")
    public List<RankingResponse> getTopScorers(
            @RequestParam @Positive Long leagueId,
            @RequestParam @Pattern(regexp = SEASON_PATTERN) String season) {
        return analysisService.getTopScorers(leagueId, season, DEFAULT_RANKING_LIMIT);
    }

    @GetMapping("/rankings/top-conceders")
    public List<RankingResponse> getTopConceders(
            @RequestParam @Positive Long leagueId,
            @RequestParam @Pattern(regexp = SEASON_PATTERN) String season) {
        return analysisService.getTopConceders(leagueId, season, DEFAULT_RANKING_LIMIT);
    }

    @GetMapping("/h2h")
    public H2HResponse getHeadToHead(
            @RequestParam @Positive Long teamAId,
            @RequestParam @Positive Long teamBId) {
        return analysisService.getHeadToHead(teamAId, teamBId);
    }
}
