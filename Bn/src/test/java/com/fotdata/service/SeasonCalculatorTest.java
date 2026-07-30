package com.fotdata.service;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;

class SeasonCalculatorTest {

    @Test
    void 시즌시작월_이후면_현재연도로_시작하는_시즌을_반환한다() {
        LocalDate date = LocalDate.of(2025, 9, 1);

        String season = SeasonCalculator.currentSeason(date);

        assertThat(season).isEqualTo("2025-2026");
    }

    @Test
    void 시즌시작월_이전이면_이전연도로_시작하는_시즌을_반환한다() {
        LocalDate date = LocalDate.of(2026, 3, 1);

        String season = SeasonCalculator.currentSeason(date);

        assertThat(season).isEqualTo("2025-2026");
    }

    @Test
    void 시즌시작월_당일이면_현재연도로_시작하는_시즌을_반환한다() {
        LocalDate date = LocalDate.of(2025, 8, 1);

        String season = SeasonCalculator.currentSeason(date);

        assertThat(season).isEqualTo("2025-2026");
    }
}
