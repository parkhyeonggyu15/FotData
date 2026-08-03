package com.fotdata.dto.response;

import com.fotdata.entity.TeamElo;

public record EloResponse(
        Long teamId,
        String teamName,
        double rating
) {

    public static EloResponse from(TeamElo teamElo) {
        return new EloResponse(
                teamElo.getTeam().getId(),
                teamElo.getTeam().getName(),
                teamElo.getRating()
        );
    }
}
