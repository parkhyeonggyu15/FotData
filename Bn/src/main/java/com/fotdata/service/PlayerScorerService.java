package com.fotdata.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fotdata.dto.response.PlayerScorerResponse;
import com.fotdata.repository.PlayerScorerRepository;

@Service
@Transactional(readOnly = true)
public class PlayerScorerService {

    private final PlayerScorerRepository playerScorerRepository;

    public PlayerScorerService(PlayerScorerRepository playerScorerRepository) {
        this.playerScorerRepository = playerScorerRepository;
    }

    public List<PlayerScorerResponse> getTopScorers(Long leagueId, String season) {
        return playerScorerRepository.findByLeagueIdAndSeasonOrderByGoalsDesc(leagueId, season)
                .stream()
                .map(PlayerScorerResponse::from)
                .toList();
    }
}
