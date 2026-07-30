package com.fotdata.entity;

public enum MatchStatus {
    SCHEDULED,
    LIVE,
    FINISHED,
    POSTPONED,
    CANCELLED;

    public static MatchStatus fromExternalStatus(String externalStatus) {
        return switch (externalStatus) {
            case "SCHEDULED", "TIMED" -> SCHEDULED;
            case "IN_PLAY", "PAUSED" -> LIVE;
            case "FINISHED" -> FINISHED;
            case "SUSPENDED", "POSTPONED" -> POSTPONED;
            case "CANCELLED" -> CANCELLED;
            default -> throw new IllegalArgumentException("Unknown match status: " + externalStatus);
        };
    }
}
