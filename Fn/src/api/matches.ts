import { apiGet } from "./client";
import type { MatchResponse } from "../types/api";

export function fetchMatches(leagueId: number, season: string, matchday: number) {
  return apiGet<MatchResponse[]>("/api/matches", { leagueId, season, matchday });
}

export function fetchRecentMatches(limit: number) {
  return apiGet<MatchResponse[]>("/api/matches/recent", { limit });
}

export function fetchLiveMatches() {
  return apiGet<MatchResponse[]>("/api/matches/live");
}
