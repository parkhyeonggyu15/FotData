package com.fotdata.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.fotdata.entity.TeamStats;

public interface TeamStatsRepository extends JpaRepository<TeamStats, Long> {

    Optional<TeamStats> findByTeamIdAndSeason(Long teamId, String season);

    @Query("""
            select ts from TeamStats ts
            where ts.team.league.id = :leagueId and ts.season = :season
            order by ts.goalsFor desc
            """)
    List<TeamStats> findTopScorersByLeague(
            @Param("leagueId") Long leagueId,
            @Param("season") String season,
            org.springframework.data.domain.Pageable pageable);

    @Query("""
            select ts from TeamStats ts
            where ts.team.league.id = :leagueId and ts.season = :season
            order by ts.goalsAgainst desc
            """)
    List<TeamStats> findTopConcedersByLeague(
            @Param("leagueId") Long leagueId,
            @Param("season") String season,
            org.springframework.data.domain.Pageable pageable);
}
