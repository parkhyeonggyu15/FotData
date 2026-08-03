package com.fotdata.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.within;

import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;

class SeasonSimulatorTest {

    @Test
    void 압도적으로_강한_팀이_평균_순위가_더_낮은_숫자다() {
        List<SeasonSimulator.Fixture> fixtures = List.of(
                new SeasonSimulator.Fixture(1L, 2L),
                new SeasonSimulator.Fixture(2L, 1L));
        Map<Long, Double> ratings = Map.of(1L, 2000.0, 2L, 1000.0);

        Map<Long, SeasonSimulator.TeamSimulationResult> results = SeasonSimulator.simulate(fixtures, ratings, 2000);

        assertThat(results.get(1L).averageRank()).isLessThan(results.get(2L).averageRank());
    }

    @Test
    void 압도적으로_강한_팀의_우승확률이_매우_높다() {
        List<SeasonSimulator.Fixture> fixtures = List.of(
                new SeasonSimulator.Fixture(1L, 2L),
                new SeasonSimulator.Fixture(2L, 1L));
        Map<Long, Double> ratings = Map.of(1L, 2200.0, 2L, 1000.0);

        Map<Long, SeasonSimulator.TeamSimulationResult> results = SeasonSimulator.simulate(fixtures, ratings, 2000);

        assertThat(results.get(1L).titleProbability()).isGreaterThan(0.9);
    }

    @Test
    void 모든_팀의_강등확률과_우승확률의_합은_1이다() {
        List<SeasonSimulator.Fixture> fixtures = List.of(
                new SeasonSimulator.Fixture(1L, 2L),
                new SeasonSimulator.Fixture(2L, 3L),
                new SeasonSimulator.Fixture(3L, 1L));
        Map<Long, Double> ratings = Map.of(1L, 1600.0, 2L, 1500.0, 3L, 1400.0);

        Map<Long, SeasonSimulator.TeamSimulationResult> results = SeasonSimulator.simulate(fixtures, ratings, 2000);

        double titleSum = results.values().stream().mapToDouble(SeasonSimulator.TeamSimulationResult::titleProbability).sum();
        assertThat(titleSum).isCloseTo(1.0, within(0.01));
    }
}
