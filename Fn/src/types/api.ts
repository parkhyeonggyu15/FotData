export type MatchStatus = "SCHEDULED" | "LIVE" | "FINISHED" | "POSTPONED" | "CANCELLED";

export interface MatchResponse {
  id: number;
  leagueName: string;
  homeTeamId: number;
  homeTeamName: string;
  homeTeamCrestUrl: string | null;
  awayTeamId: number;
  awayTeamName: string;
  awayTeamCrestUrl: string | null;
  matchDate: string;
  status: MatchStatus;
  homeScore: number | null;
  awayScore: number | null;
  matchday: number;
}

export interface TeamStatsResponse {
  teamId: number;
  teamName: string;
  teamCrestUrl: string | null;
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
  teamCrestUrl: string | null;
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

export interface EloResponse {
  teamId: number;
  teamName: string;
  teamCrestUrl: string | null;
  rating: number;
}

export interface MatchPredictionResponse {
  homeTeamId: number;
  awayTeamId: number;
  homeWinProbability: number;
  drawProbability: number;
  awayWinProbability: number;
}

export interface SeasonPredictionResponse {
  teamId: number;
  teamName: string;
  teamCrestUrl: string | null;
  averageRank: number;
  titleProbability: number;
  relegationProbability: number;
}

export interface PlayerScorerResponse {
  playerId: number;
  playerName: string;
  teamId: number;
  teamName: string;
  teamCrestUrl: string | null;
  goals: number;
  assists: number;
  playedMatches: number;
}

export interface PlayerGoalPredictionResponse {
  playerId: number;
  playerName: string;
  teamId: number;
  teamName: string;
  teamCrestUrl: string | null;
  predictedGoals: number;
}
