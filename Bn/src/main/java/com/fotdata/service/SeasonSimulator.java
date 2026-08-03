package com.fotdata.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public final class SeasonSimulator {

    private SeasonSimulator() {
    }

    public static Map<Long, TeamSimulationResult> simulate(
            List<Fixture> fixtures, Map<Long, Double> initialRatings, int iterations) {
        List<Long> teamIds = List.copyOf(initialRatings.keySet());
        Map<Long, int[]> rankCounts = new HashMap<>();
        Map<Long, Integer> titleCounts = new HashMap<>();
        Map<Long, Integer> relegationCounts = new HashMap<>();
        for (Long teamId : teamIds) {
            rankCounts.put(teamId, new int[teamIds.size()]);
            titleCounts.put(teamId, 0);
            relegationCounts.put(teamId, 0);
        }

        Random random = new Random();
        int relegationZoneSize = Math.min(3, teamIds.size());

        for (int i = 0; i < iterations; i++) {
            Map<Long, Integer> points = simulateOnce(fixtures, initialRatings, random);
            List<Long> standings = teamIds.stream()
                    .sorted((a, b) -> points.get(b) - points.get(a))
                    .toList();

            for (int rank = 0; rank < standings.size(); rank++) {
                Long teamId = standings.get(rank);
                rankCounts.get(teamId)[rank]++;
                if (rank == 0) {
                    titleCounts.merge(teamId, 1, Integer::sum);
                }
                if (rank >= standings.size() - relegationZoneSize) {
                    relegationCounts.merge(teamId, 1, Integer::sum);
                }
            }
        }

        Map<Long, TeamSimulationResult> results = new HashMap<>();
        for (Long teamId : teamIds) {
            double averageRank = averageRank(rankCounts.get(teamId), iterations);
            double titleProbability = titleCounts.get(teamId) / (double) iterations;
            double relegationProbability = relegationCounts.get(teamId) / (double) iterations;
            results.put(teamId, new TeamSimulationResult(averageRank, titleProbability, relegationProbability));
        }
        return results;
    }

    private static Map<Long, Integer> simulateOnce(
            List<Fixture> fixtures, Map<Long, Double> initialRatings, Random random) {
        Map<Long, Double> ratings = new HashMap<>(initialRatings);
        Map<Long, Integer> points = new HashMap<>();
        for (Long teamId : initialRatings.keySet()) {
            points.put(teamId, 0);
        }

        for (Fixture fixture : fixtures) {
            double homeRating = ratings.get(fixture.homeTeamId());
            double awayRating = ratings.get(fixture.awayTeamId());
            MatchPredictor.Prediction prediction = MatchPredictor.predict(homeRating, awayRating);

            double roll = random.nextDouble();
            EloCalculator.MatchResult result;
            if (roll < prediction.homeWinProbability()) {
                result = EloCalculator.MatchResult.HOME_WIN;
                points.merge(fixture.homeTeamId(), 3, Integer::sum);
            } else if (roll < prediction.homeWinProbability() + prediction.drawProbability()) {
                result = EloCalculator.MatchResult.DRAW;
                points.merge(fixture.homeTeamId(), 1, Integer::sum);
                points.merge(fixture.awayTeamId(), 1, Integer::sum);
            } else {
                result = EloCalculator.MatchResult.AWAY_WIN;
                points.merge(fixture.awayTeamId(), 3, Integer::sum);
            }

            double[] newRatings = EloCalculator.calculateNewRatings(homeRating, awayRating, result);
            ratings.put(fixture.homeTeamId(), newRatings[0]);
            ratings.put(fixture.awayTeamId(), newRatings[1]);
        }

        return points;
    }

    private static double averageRank(int[] counts, int iterations) {
        double sum = 0;
        for (int rank = 0; rank < counts.length; rank++) {
            sum += (rank + 1) * counts[rank];
        }
        return sum / iterations;
    }

    public record Fixture(Long homeTeamId, Long awayTeamId) {
    }

    public record TeamSimulationResult(double averageRank, double titleProbability, double relegationProbability) {
    }
}
