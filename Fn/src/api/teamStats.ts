import { apiGet } from "./client";
import type { TeamStatsResponse } from "../types/api";

export function fetchTeamStats(teamId: number, season: string) {
  return apiGet<TeamStatsResponse>(`/api/teams/${teamId}/stats`, { season });
}
