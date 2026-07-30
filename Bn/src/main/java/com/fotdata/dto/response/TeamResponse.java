package com.fotdata.dto.response;

import com.fotdata.entity.Team;

public record TeamResponse(
        Long id,
        String name,
        String crestUrl
) {

    public static TeamResponse from(Team team) {
        return new TeamResponse(
                team.getId(),
                team.getName(),
                team.getCrestUrl()
        );
    }
}
