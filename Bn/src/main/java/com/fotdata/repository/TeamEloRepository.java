package com.fotdata.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.fotdata.entity.TeamElo;

public interface TeamEloRepository extends JpaRepository<TeamElo, Long> {

    Optional<TeamElo> findByTeamId(Long teamId);

    @Query("""
            select te from TeamElo te
            where te.team.league.id = :leagueId
            order by te.rating desc
            """)
    List<TeamElo> findByLeagueIdOrderByRatingDesc(Long leagueId);
}
