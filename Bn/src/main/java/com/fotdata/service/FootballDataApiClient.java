package com.fotdata.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import com.fotdata.dto.external.MatchListExternalResponse;

@Component
public class FootballDataApiClient {

    private final RestClient restClient;
    private final RateLimiter rateLimiter;

    public FootballDataApiClient(
            @Value("${football-data.api.base-url}") String baseUrl,
            @Value("${football-data.api.token}") String apiToken,
            RateLimiter rateLimiter) {
        this.restClient = RestClient.builder()
                .baseUrl(baseUrl)
                .defaultHeader("X-Auth-Token", apiToken)
                .build();
        this.rateLimiter = rateLimiter;
    }

    public MatchListExternalResponse fetchMatches(String competitionCode, int seasonStartYear) {
        rateLimiter.acquire();
        return restClient.get()
                .uri("/v4/competitions/{code}/matches?season={season}", competitionCode, seasonStartYear)
                .retrieve()
                .body(MatchListExternalResponse.class);
    }
}
