package com.fotdata.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.fotdata.dto.response.PlayerScorerResponse;
import com.fotdata.service.PlayerScorerService;

@WebMvcTest(PlayerScorerController.class)
class PlayerScorerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private PlayerScorerService playerScorerService;

    @Test
    void 선수_득점_순위를_조회하면_200을_반환한다() throws Exception {
        when(playerScorerService.getTopScorers(any(), any()))
                .thenReturn(List.of(
                        new PlayerScorerResponse(1L, "Erling Haaland", 13L, "Manchester City FC", "city.png", 27, 8, 36)));

        mockMvc.perform(get("/api/analysis/player-scorers")
                        .param("leagueId", "1").param("season", "2025-2026"))
                .andExpect(status().isOk());
    }

    @Test
    void leagueId가_0이하이면_400을_반환한다() throws Exception {
        mockMvc.perform(get("/api/analysis/player-scorers")
                        .param("leagueId", "0").param("season", "2025-2026"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void season_형식이_잘못되면_400을_반환한다() throws Exception {
        mockMvc.perform(get("/api/analysis/player-scorers")
                        .param("leagueId", "1").param("season", "2025"))
                .andExpect(status().isBadRequest());
    }
}
