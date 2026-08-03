package com.fotdata.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.within;

import org.junit.jupiter.api.Test;

class MatchPredictorTest {

    @Test
    void 확률의_합은_항상_1이다() {
        MatchPredictor.Prediction prediction = MatchPredictor.predict(1600, 1400);

        double sum = prediction.homeWinProbability() + prediction.drawProbability() + prediction.awayWinProbability();
        assertThat(sum).isCloseTo(1.0, within(0.0001));
    }

    @Test
    void 동일한_레이팅이면_홈팀이_약간_유리하다() {
        MatchPredictor.Prediction prediction = MatchPredictor.predict(1500, 1500);

        assertThat(prediction.homeWinProbability()).isGreaterThan(prediction.awayWinProbability());
    }

    @Test
    void 레이팅_차이가_클수록_무승부_확률이_낮아진다() {
        MatchPredictor.Prediction close = MatchPredictor.predict(1500, 1500);
        MatchPredictor.Prediction lopsided = MatchPredictor.predict(1900, 1300);

        assertThat(lopsided.drawProbability()).isLessThan(close.drawProbability());
    }

    @Test
    void 압도적으로_강한_홈팀은_승리확률이_매우_높다() {
        MatchPredictor.Prediction prediction = MatchPredictor.predict(2000, 1000);

        assertThat(prediction.homeWinProbability()).isGreaterThan(0.9);
    }
}
