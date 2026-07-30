package com.fotdata.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fotdata.dto.response.MatchResponse;
import com.fotdata.repository.MatchRepository;

@Service
@Transactional(readOnly = true)
public class MatchQueryService {

    private final MatchRepository matchRepository;

    public MatchQueryService(MatchRepository matchRepository) {
        this.matchRepository = matchRepository;
    }

    public List<MatchResponse> getMatchesByMatchday(Long leagueId, String season, Integer matchday) {
        return matchRepository.findByLeagueIdAndSeasonAndMatchday(leagueId, season, matchday)
                .stream()
                .map(MatchResponse::from)
                .toList();
    }
}
