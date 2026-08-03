package com.fotdata.service;

public final class MatchPredictor {

    private static final double BASE_DRAW_PROBABILITY = 0.26;

    private MatchPredictor() {
    }

    public static Prediction predict(double homeRating, double awayRating) {
        double expectedHome = EloCalculator.expectedScore(homeRating + EloCalculator.HOME_ADVANTAGE, awayRating);

        double drawProbability = BASE_DRAW_PROBABILITY * (1 - Math.abs(2 * expectedHome - 1));
        double remaining = 1 - drawProbability;
        double homeWinProbability = remaining * expectedHome;
        double awayWinProbability = remaining * (1 - expectedHome);

        return new Prediction(homeWinProbability, drawProbability, awayWinProbability);
    }

    public record Prediction(double homeWinProbability, double drawProbability, double awayWinProbability) {
    }
}
