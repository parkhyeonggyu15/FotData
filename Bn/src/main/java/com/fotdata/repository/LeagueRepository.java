package com.fotdata.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.fotdata.entity.League;

public interface LeagueRepository extends JpaRepository<League, Long> {

    Optional<League> findByCode(String code);
}
