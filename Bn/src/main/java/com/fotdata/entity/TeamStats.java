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
@Table(name = "team_stats", uniqueConstraints = @UniqueConstraint(columnNames = {"team_id", "season"}))
public class TeamStats {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team_id", nullable = false)
    private Team team;

    @Column(nullable = false, length = 9)
    private String season;

    @Column(nullable = false)
    private int played;

    @Column(nullable = false)
    private int win;

    @Column(nullable = false)
    private int draw;

    @Column(nullable = false)
    private int lose;

    @Column(name = "goals_for", nullable = false)
    private int goalsFor;

    @Column(name = "goals_against", nullable = false)
    private int goalsAgainst;

    @Column(name = "home_goals_for", nullable = false)
    private int homeGoalsFor;

    @Column(name = "home_goals_against", nullable = false)
    private int homeGoalsAgainst;

    @Column(name = "away_goals_for", nullable = false)
    private int awayGoalsFor;

    @Column(name = "away_goals_against", nullable = false)
    private int awayGoalsAgainst;

    @Column(name = "recent_form", length = 5)
    private String recentForm;

    public TeamStats(Team team, String season) {
        this.team = team;
        this.season = season;
        this.played = 0;
        this.win = 0;
        this.draw = 0;
        this.lose = 0;
        this.goalsFor = 0;
        this.goalsAgainst = 0;
        this.homeGoalsFor = 0;
        this.homeGoalsAgainst = 0;
        this.awayGoalsFor = 0;
        this.awayGoalsAgainst = 0;
        this.recentForm = "";
    }

    public void update(int played, int win, int draw, int lose,
                        int goalsFor, int goalsAgainst,
                        int homeGoalsFor, int homeGoalsAgainst,
                        int awayGoalsFor, int awayGoalsAgainst,
                        String recentForm) {
        this.played = played;
        this.win = win;
        this.draw = draw;
        this.lose = lose;
        this.goalsFor = goalsFor;
        this.goalsAgainst = goalsAgainst;
        this.homeGoalsFor = homeGoalsFor;
        this.homeGoalsAgainst = homeGoalsAgainst;
        this.awayGoalsFor = awayGoalsFor;
        this.awayGoalsAgainst = awayGoalsAgainst;
        this.recentForm = recentForm;
    }
}
