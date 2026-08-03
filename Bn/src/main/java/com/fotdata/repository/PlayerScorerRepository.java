package com.fotdata.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.fotdata.entity.PlayerScorer;

public interface PlayerScorerRepository extends JpaRepository<PlayerScorer, Long> {

    Optional<PlayerScorer> findByPlayerIdAndLeagueIdAndSeason(Long playerId, Long leagueId, String season);

    List<PlayerScorer> findByLeagueIdAndSeasonOrderByGoalsDesc(Long leagueId, String season);
}
