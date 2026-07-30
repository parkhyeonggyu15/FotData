package com.fotdata.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fotdata.dto.response.H2HResponse;
import com.fotdata.dto.response.RankingResponse;
import com.fotdata.service.AnalysisService;

@RestController
@RequestMapping("/api/analysis")
public class AnalysisController {

    private static final int DEFAULT_RANKING_LIMIT = 10;

    private final AnalysisService analysisService;

    public AnalysisController(AnalysisService analysisService) {
        this.analysisService = analysisService;
    }

    @GetMapping("/rankings/top-scorers")
    public List<RankingResponse> getTopScorers(
            @RequestParam Long leagueId,
            @RequestParam String season) {
        return analysisService.getTopScorers(leagueId, season, DEFAULT_RANKING_LIMIT);
    }

    @GetMapping("/rankings/top-conceders")
    public List<RankingResponse> getTopConceders(
            @RequestParam Long leagueId,
            @RequestParam String season) {
        return analysisService.getTopConceders(leagueId, season, DEFAULT_RANKING_LIMIT);
    }

    @GetMapping("/h2h")
    public H2HResponse getHeadToHead(
            @RequestParam Long teamAId,
            @RequestParam Long teamBId) {
        return analysisService.getHeadToHead(teamAId, teamBId);
    }
}
