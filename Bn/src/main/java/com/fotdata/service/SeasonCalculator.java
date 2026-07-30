package com.fotdata.service;

import java.time.LocalDate;

public final class SeasonCalculator {

    private static final int SEASON_START_MONTH = 8;

    private SeasonCalculator() {
    }

    public static String currentSeason() {
        return currentSeason(LocalDate.now());
    }

    public static String currentSeason(LocalDate date) {
        int startYear = date.getMonthValue() >= SEASON_START_MONTH ? date.getYear() : date.getYear() - 1;
        return startYear + "-" + (startYear + 1);
    }

    public static int startYear(String season) {
        return Integer.parseInt(season.substring(0, 4));
    }
}
