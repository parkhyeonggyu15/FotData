package com.fotdata.service;

public final class PlayerGoalPredictor {

    private PlayerGoalPredictor() {
    }

    public static double predictGoals(int previousGoals, int previousPlayedMatches, int projectedMatches) {
        if (previousPlayedMatches == 0) {
            return 0;
        }
        double goalsPerMatch = (double) previousGoals / previousPlayedMatches;
        return goalsPerMatch * projectedMatches;
    }
}
