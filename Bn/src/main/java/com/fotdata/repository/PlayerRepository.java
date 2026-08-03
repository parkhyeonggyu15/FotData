package com.fotdata.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.fotdata.entity.Player;

public interface PlayerRepository extends JpaRepository<Player, Long> {
}
