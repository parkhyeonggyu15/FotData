package com.fotdata.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.fotdata.dto.response.MatchPredictionResponse;
import com.fotdata.dto.response.PlayerGoalPredictionResponse;
import com.fotdata.dto.response.SeasonPredictionResponse;
import com.fotdata.service.PredictionService;

@WebMvcTest(PredictionController.class)
class PredictionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private PredictionService predictionService;

    @Test
    void 경기_승률_예측을_조회하면_200을_반환한다() throws Exception {
        when(predictionService.predictMatch(any(), any()))
                .thenReturn(new MatchPredictionResponse(1L, 2L, 0.5, 0.25, 0.25));

        mockMvc.perform(get("/api/predictions/match")
                        .param("homeTeamId", "1").param("awayTeamId", "2"))
                .andExpect(status().isOk());
    }

    @Test
    void homeTeamId가_0이하이면_400을_반환한다() throws Exception {
        mockMvc.perform(get("/api/predictions/match")
                        .param("homeTeamId", "0").param("awayTeamId", "2"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void 시즌_순위_예측을_조회하면_200을_반환한다() throws Exception {
        when(predictionService.predictSeason(any(), any()))
                .thenReturn(List.of(new SeasonPredictionResponse(1L, "Arsenal FC", 1.5, 0.3, 0.05)));

        mockMvc.perform(get("/api/predictions/season")
                        .param("leagueId", "1").param("season", "2026-2027"))
                .andExpect(status().isOk());
    }

    @Test
    void 시즌_예측_season_형식이_잘못되면_400을_반환한다() throws Exception {
        mockMvc.perform(get("/api/predictions/season")
                        .param("leagueId", "1").param("season", "2026"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void 선수_득점_예측을_조회하면_200을_반환한다() throws Exception {
        when(predictionService.predictTopScorers(any(), any(), anyInt()))
                .thenReturn(List.of(new PlayerGoalPredictionResponse(1L, "Erling Haaland", 13L, "Manchester City FC", 28.5)));

        mockMvc.perform(get("/api/predictions/top-scorers")
                        .param("leagueId", "1").param("baseSeason", "2025-2026"))
                .andExpect(status().isOk());
    }

    @Test
    void 선수_득점_예측_baseSeason_형식이_잘못되면_400을_반환한다() throws Exception {
        mockMvc.perform(get("/api/predictions/top-scorers")
                        .param("leagueId", "1").param("baseSeason", "2025"))
                .andExpect(status().isBadRequest());
    }
}
