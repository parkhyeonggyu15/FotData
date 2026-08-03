package com.fotdata.dto.external;

import java.util.List;

public record ScorerListExternalResponse(
        List<ScorerResponse> scorers
) {
}
