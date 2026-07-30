package com.fotdata.dto.external;

import java.util.List;

public record MatchListExternalResponse(
        List<MatchExternalResponse> matches
) {
}
