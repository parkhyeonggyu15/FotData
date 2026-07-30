package com.fotdata.dto.response;

import com.fotdata.entity.League;

public record LeagueResponse(
        Long id,
        String code,
        String name
) {

    public static LeagueResponse from(League league) {
        return new LeagueResponse(
                league.getId(),
                league.getCode(),
                league.getName()
        );
    }
}
