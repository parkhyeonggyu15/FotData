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

import com.fotdata.dto.response.LeagueResponse;
import com.fotdata.dto.response.TeamResponse;
import com.fotdata.service.LeagueService;

@WebMvcTest(LeagueController.class)
class LeagueControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private LeagueService leagueService;

    @Test
    void 리그_목록을_조회하면_200을_반환한다() throws Exception {
        when(leagueService.getLeagues())
                .thenReturn(List.of(new LeagueResponse(1L, "PL", "Premier League")));

        mockMvc.perform(get("/api/leagues"))
                .andExpect(status().isOk());
    }

    @Test
    void 리그의_팀_목록을_조회하면_200을_반환한다() throws Exception {
        when(leagueService.getTeamsByLeague(any()))
                .thenReturn(List.of(new TeamResponse(1L, "Arsenal", "crest.png")));

        mockMvc.perform(get("/api/leagues/1/teams"))
                .andExpect(status().isOk());
    }

    @Test
    void leagueId가_0이하이면_400을_반환한다() throws Exception {
        mockMvc.perform(get("/api/leagues/0/teams"))
                .andExpect(status().isBadRequest());
    }
}
