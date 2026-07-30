package com.fotdata.dto.external;

public record ScoreResponse(
        FullTime fullTime
) {
    public record FullTime(
            Integer home,
            Integer away
    ) {
    }
}
