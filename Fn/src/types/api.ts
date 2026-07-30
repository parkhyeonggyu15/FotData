export type MatchStatus = "SCHEDULED" | "LIVE" | "FINISHED" | "POSTPONED" | "CANCELLED";

export interface MatchResponse {
  id: number;
  leagueName: string;
  homeTeamName: string;
  awayTeamName: string;
  matchDate: string;
  status: MatchStatus;
  homeScore: number | null;
  awayScore: number | null;
  matchday: number;
}

export interface TeamStatsResponse {
  teamId: number;
  teamName: string;
  season: string;
  played: number;
  win: number;
  draw: number;
  lose: number;
  goalsFor: number;
  goalsAgainst: number;
  homeGoalDifference: number;
  awayGoalDifference: number;
  recentForm: string;
}

export interface RankingResponse {
  teamId: number;
  teamName: string;
  goalsFor: number;
  goalsAgainst: number;
}

export interface H2HResponse {
  teamAId: number;
  teamBId: number;
  teamAWins: number;
  teamBWins: number;
  draws: number;
  recentMatches: MatchResponse[];
}

export interface LeagueResponse {
  id: number;
  code: string;
  name: string;
}

export interface TeamResponse {
  id: number;
  name: string;
  crestUrl: string | null;
}
