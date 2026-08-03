package com.fotdata.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "player_scorer", uniqueConstraints = @UniqueConstraint(columnNames = {"player_id", "league_id", "season"}))
public class PlayerScorer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "player_id", nullable = false)
    private Player player;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team_id", nullable = false)
    private Team team;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "league_id", nullable = false)
    private League league;

    @Column(nullable = false, length = 9)
    private String season;

    @Column(nullable = false)
    private int goals;

    @Column(nullable = false)
    private int assists;

    @Column(name = "played_matches", nullable = false)
    private int playedMatches;

    public PlayerScorer(Player player, Team team, League league, String season,
                         int goals, int assists, int playedMatches) {
        this.player = player;
        this.team = team;
        this.league = league;
        this.season = season;
        this.goals = goals;
        this.assists = assists;
        this.playedMatches = playedMatches;
    }

    public void update(Team team, int goals, int assists, int playedMatches) {
        this.team = team;
        this.goals = goals;
        this.assists = assists;
        this.playedMatches = playedMatches;
    }
}
