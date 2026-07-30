import { apiGet } from "./client";
import type { LeagueResponse, TeamResponse } from "../types/api";

export function fetchLeagues() {
  return apiGet<LeagueResponse[]>("/api/leagues");
}

export function fetchTeamsByLeague(leagueId: number) {
  return apiGet<TeamResponse[]>(`/api/leagues/${leagueId}/teams`);
}
