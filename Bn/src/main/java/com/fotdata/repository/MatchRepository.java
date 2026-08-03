package com.fotdata.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.fotdata.entity.Match;
import com.fotdata.entity.MatchStatus;

public interface MatchRepository extends JpaRepository<Match, Long> {

    Optional<Match> findByHomeTeamIdAndAwayTeamIdAndMatchDate(
            Long homeTeamId, Long awayTeamId, LocalDateTime matchDate);

    List<Match> findByLeagueIdAndSeasonAndMatchday(Long leagueId, String season, Integer matchday);

    List<Match> findByStatusOrderByMatchDateDesc(MatchStatus status, Pageable pageable);

    List<Match> findByLeagueIdAndSeasonAndStatusOrderByMatchDateAsc(Long leagueId, String season, MatchStatus status);

    @Query("""
            select m from Match m
            where (m.homeTeam.id = :teamId or m.awayTeam.id = :teamId)
              and m.status = :status
            order by m.matchDate desc
            """)
    List<Match> findRecentFinishedByTeamId(
            @Param("teamId") Long teamId,
            @Param("status") MatchStatus status,
            Pageable pageable);

    @Query("""
            select m from Match m
            where ((m.homeTeam.id = :teamAId and m.awayTeam.id = :teamBId)
                or (m.homeTeam.id = :teamBId and m.awayTeam.id = :teamAId))
              and m.status = :status
            order by m.matchDate desc
            """)
    List<Match> findHeadToHead(
            @Param("teamAId") Long teamAId,
            @Param("teamBId") Long teamBId,
            @Param("status") MatchStatus status,
            Pageable pageable);

    @Query("""
            select m from Match m
            where (m.homeTeam.id = :teamId or m.awayTeam.id = :teamId)
              and m.status = :status
            order by m.matchDate asc
            """)
    List<Match> findAllFinishedByTeamId(
            @Param("teamId") Long teamId,
            @Param("status") MatchStatus status);
}
