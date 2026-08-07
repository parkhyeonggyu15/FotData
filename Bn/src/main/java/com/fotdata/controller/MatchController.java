package com.fotdata.controller;

import java.util.List;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fotdata.dto.response.MatchResponse;
import com.fotdata.service.MatchQueryService;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;

@Validated
@RestController
@RequestMapping("/api/matches")
public class MatchController {

    private static final String SEASON_PATTERN = "\\d{4}-\\d{4}";

    private final MatchQueryService matchQueryService;

    public MatchController(MatchQueryService matchQueryService) {
        this.matchQueryService = matchQueryService;
    }

    @GetMapping
    public List<MatchResponse> getMatches(
            @RequestParam @Positive Long leagueId,
            @RequestParam @Pattern(regexp = SEASON_PATTERN) String season,
            @RequestParam @Positive Integer matchday) {
        return matchQueryService.getMatchesByMatchday(leagueId, season, matchday);
    }

    @GetMapping("/recent")
    public List<MatchResponse> getRecentMatches(@RequestParam(defaultValue = "5") @Positive Integer limit) {
        return matchQueryService.getRecentMatches(limit);
    }

    @GetMapping("/live")
    public List<MatchResponse> getLiveMatches() {
        return matchQueryService.getLiveMatches();
    }
}
