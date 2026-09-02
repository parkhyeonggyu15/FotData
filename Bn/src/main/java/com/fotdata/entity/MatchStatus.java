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
            case "FINISHED", "AWARDED" -> FINISHED;
            case "SUSPENDED", "POSTPONED" -> POSTPONED;
            case "CANCELLED" -> CANCELLED;
            // football-data.org가 시간 미확정 미래 경기에 status 대신 날짜 문자열을 내려주는 경우가 있어 SCHEDULED로 처리
            default -> SCHEDULED;
        };
    }
}
