package com.fotdata.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.fotdata.entity.Team;

public interface TeamRepository extends JpaRepository<Team, Long> {

    List<Team> findByLeagueId(Long leagueId);

    Optional<Team> findByNameAndLeagueId(String name, Long leagueId);
}
