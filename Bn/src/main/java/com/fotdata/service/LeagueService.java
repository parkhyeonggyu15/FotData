package com.fotdata.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fotdata.dto.response.LeagueResponse;
import com.fotdata.dto.response.TeamResponse;
import com.fotdata.repository.LeagueRepository;
import com.fotdata.repository.TeamRepository;

@Service
@Transactional(readOnly = true)
public class LeagueService {

    private final LeagueRepository leagueRepository;
    private final TeamRepository teamRepository;

    public LeagueService(LeagueRepository leagueRepository, TeamRepository teamRepository) {
        this.leagueRepository = leagueRepository;
        this.teamRepository = teamRepository;
    }

    public List<LeagueResponse> getLeagues() {
        return leagueRepository.findAll()
                .stream()
                .map(LeagueResponse::from)
                .toList();
    }

    public List<TeamResponse> getTeamsByLeague(Long leagueId) {
        return teamRepository.findByLeagueId(leagueId)
                .stream()
                .map(TeamResponse::from)
                .toList();
    }
}
