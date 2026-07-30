package com.fotdata.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Set;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.fotdata.service.MatchSyncService;
import com.fotdata.service.TeamStatsService;

@WebMvcTest(AdminSyncController.class)
@TestPropertySource(properties = "admin.api-key=test-admin-key")
class AdminSyncControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private MatchSyncService matchSyncService;

    @MockitoBean
    private TeamStatsService teamStatsService;

    @Test
    void 올바른_관리자키를_보내면_동기화가_수행된다() throws Exception {
        when(matchSyncService.syncCompetition(any())).thenReturn(Set.of(1L, 2L));

        mockMvc.perform(post("/api/admin/sync")
                        .param("leagueCode", "PL")
                        .header("X-Admin-Key", "test-admin-key"))
                .andExpect(status().isOk());
    }

    @Test
    void 관리자키가_없으면_401을_반환한다() throws Exception {
        mockMvc.perform(post("/api/admin/sync").param("leagueCode", "PL"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void 관리자키가_틀리면_401을_반환한다() throws Exception {
        mockMvc.perform(post("/api/admin/sync")
                        .param("leagueCode", "PL")
                        .header("X-Admin-Key", "wrong-key"))
                .andExpect(status().isUnauthorized());
    }
}
