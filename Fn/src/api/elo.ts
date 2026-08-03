import { apiGet } from "./client";
import type { EloResponse } from "../types/api";

export function fetchTeamElo(teamId: number) {
  return apiGet<EloResponse>(`/api/teams/${teamId}/elo`);
}
