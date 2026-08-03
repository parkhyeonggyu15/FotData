package com.fotdata.service;

public final class EloCalculator {

    private static final double K_FACTOR = 30.0;
    public static final double HOME_ADVANTAGE = 60.0;

    private EloCalculator() {
    }

    public static double[] calculateNewRatings(double homeRating, double awayRating, MatchResult result) {
        double expectedHome = expectedScore(homeRating + HOME_ADVANTAGE, awayRating);
        double actualHome = result.homeScore();

        double newHomeRating = homeRating + K_FACTOR * (actualHome - expectedHome);
        double newAwayRating = awayRating + K_FACTOR * ((1 - actualHome) - (1 - expectedHome));

        return new double[] {newHomeRating, newAwayRating};
    }

    public static double expectedScore(double ratingA, double ratingB) {
        return 1.0 / (1.0 + Math.pow(10, (ratingB - ratingA) / 400.0));
    }

    public enum MatchResult {
        HOME_WIN(1.0),
        DRAW(0.5),
        AWAY_WIN(0.0);

        private final double homeScore;

        MatchResult(double homeScore) {
            this.homeScore = homeScore;
        }

        public double homeScore() {
            return homeScore;
        }

        public static MatchResult from(int homeGoals, int awayGoals) {
            if (homeGoals > awayGoals) {
                return HOME_WIN;
            }
            if (homeGoals < awayGoals) {
                return AWAY_WIN;
            }
            return DRAW;
        }
    }
}
