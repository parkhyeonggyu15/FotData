package com.fotdata.entity;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.Test;

class MatchStatusTest {

    @Test
    void AWARDED는_FINISHED로_매핑된다() {
        assertThat(MatchStatus.fromExternalStatus("AWARDED")).isEqualTo(MatchStatus.FINISHED);
    }

    @Test
    void 알수없는_상태는_예외를_던진다() {
        assertThatThrownBy(() -> MatchStatus.fromExternalStatus("UNKNOWN"))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
