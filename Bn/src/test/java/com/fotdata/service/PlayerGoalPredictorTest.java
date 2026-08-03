package com.fotdata.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.within;

import org.junit.jupiter.api.Test;

class PlayerGoalPredictorTest {

    @Test
    void 경기당_득점률에_예상_경기수를_곱해_예측한다() {
        double result = PlayerGoalPredictor.predictGoals(27, 36, 38);

        assertThat(result).isCloseTo(27.0 / 36 * 38, within(0.001));
    }

    @Test
    void 출전경기가_0이면_예측_득점도_0이다() {
        double result = PlayerGoalPredictor.predictGoals(0, 0, 38);

        assertThat(result).isEqualTo(0);
    }

    @Test
    void 득점이_0이어도_출전경기가_있으면_0을_반환한다() {
        double result = PlayerGoalPredictor.predictGoals(0, 20, 38);

        assertThat(result).isEqualTo(0);
    }
}
