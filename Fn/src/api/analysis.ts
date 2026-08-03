import { apiGet } from "./client";
import type { EloResponse, H2HResponse, PlayerScorerResponse, RankingResponse } from "../types/api";

export function fetchTopScorers(leagueId: number, season: string) {
  return apiGet<RankingResponse[]>("/api/analysis/rankings/top-scorers", { leagueId, season });
}

export function fetchTopConceders(leagueId: number, season: string) {
  return apiGet<RankingResponse[]>("/api/analysis/rankings/top-conceders", { leagueId, season });
}

export function fetchHeadToHead(teamAId: number, teamBId: number) {
  return apiGet<H2HResponse>("/api/analysis/h2h", { teamAId, teamBId });
}

export function fetchEloRankings(leagueId: number) {
  return apiGet<EloResponse[]>("/api/analysis/elo-rankings", { leagueId });
}

export function fetchPlayerScorers(leagueId: number, season: string) {
  return apiGet<PlayerScorerResponse[]>("/api/analysis/player-scorers", { leagueId, season });
}
