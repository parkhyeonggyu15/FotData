import { apiGet } from "./client";
import type { MatchResponse } from "../types/api";

export function fetchMatches(leagueId: number, season: string, matchday: number) {
  return apiGet<MatchResponse[]>("/api/matches", { leagueId, season, matchday });
}
