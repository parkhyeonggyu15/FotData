package com.fotdata.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.fotdata.dto.response.MatchResponse;
import com.fotdata.entity.MatchStatus;
import com.fotdata.service.MatchQueryService;

@WebMvcTest(MatchController.class)
class MatchControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private MatchQueryService matchQueryService;

    @Test
    void 정상_파라미터로_요청하면_200과_경기목록을_반환한다() throws Exception {
        MatchResponse response = new MatchResponse(
                1L, "Premier League", "Arsenal", "Chelsea",
                LocalDateTime.of(2025, 9, 1, 0, 0), MatchStatus.FINISHED, 2, 1, 3);
        when(matchQueryService.getMatchesByMatchday(any(), any())).thenReturn(List.of(response));

        mockMvc.perform(get("/api/matches").param("leagueId", "1").param("matchday", "3"))
                .andExpect(status().isOk());
    }

    @Test
    void leagueId가_0이하이면_400을_반환한다() throws Exception {
        mockMvc.perform(get("/api/matches").param("leagueId", "0").param("matchday", "3"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void matchday가_음수이면_400을_반환한다() throws Exception {
        mockMvc.perform(get("/api/matches").param("leagueId", "1").param("matchday", "-1"))
                .andExpect(status().isBadRequest());
    }
}
