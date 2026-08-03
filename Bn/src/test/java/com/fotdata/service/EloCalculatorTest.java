package com.fotdata.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.within;

import org.junit.jupiter.api.Test;

class EloCalculatorTest {

    @Test
    void 동일한_레이팅에서_홈팀이_승리하면_홈팀_레이팅이_오른다() {
        double[] result = EloCalculator.calculateNewRatings(1500, 1500, EloCalculator.MatchResult.HOME_WIN);

        assertThat(result[0]).isGreaterThan(1500);
        assertThat(result[1]).isLessThan(1500);
    }

    @Test
    void 동일한_레이팅에서_원정팀이_승리하면_원정팀_레이팅이_오른다() {
        double[] result = EloCalculator.calculateNewRatings(1500, 1500, EloCalculator.MatchResult.AWAY_WIN);

        assertThat(result[0]).isLessThan(1500);
        assertThat(result[1]).isGreaterThan(1500);
    }

    @Test
    void 무승부면_두_팀_레이팅_변화량의_합이_0에_가깝다() {
        double[] result = EloCalculator.calculateNewRatings(1500, 1500, EloCalculator.MatchResult.DRAW);

        double homeDelta = result[0] - 1500;
        double awayDelta = result[1] - 1500;
        assertThat(homeDelta + awayDelta).isCloseTo(0.0, within(0.001));
    }

    @Test
    void 홈_어드밴티지로_동일_레이팅_무승부여도_홈팀_레이팅이_소폭_하락한다() {
        double[] result = EloCalculator.calculateNewRatings(1500, 1500, EloCalculator.MatchResult.DRAW);

        assertThat(result[0]).isLessThan(1500);
        assertThat(result[1]).isGreaterThan(1500);
    }

    @Test
    void 결과_판정은_스코어_기준으로_정확하다() {
        assertThat(EloCalculator.MatchResult.from(2, 0)).isEqualTo(EloCalculator.MatchResult.HOME_WIN);
        assertThat(EloCalculator.MatchResult.from(0, 2)).isEqualTo(EloCalculator.MatchResult.AWAY_WIN);
        assertThat(EloCalculator.MatchResult.from(1, 1)).isEqualTo(EloCalculator.MatchResult.DRAW);
    }
}
