import { apiGet } from "./client";
import type { MatchPredictionResponse, PlayerGoalPredictionResponse, SeasonPredictionResponse } from "../types/api";

export function fetchMatchPrediction(homeTeamId: number, awayTeamId: number) {
  return apiGet<MatchPredictionResponse>("/api/predictions/match", { homeTeamId, awayTeamId });
}

export function fetchSeasonPrediction(leagueId: number, season: string) {
  return apiGet<SeasonPredictionResponse[]>("/api/predictions/season", { leagueId, season });
}

export function fetchTopScorerPrediction(leagueId: number, baseSeason: string) {
  return apiGet<PlayerGoalPredictionResponse[]>("/api/predictions/top-scorers", { leagueId, baseSeason });
}
