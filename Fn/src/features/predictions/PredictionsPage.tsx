import { useState } from "react";
import { useQuery } from "@tanstack/react-query";
import { fetchLeagues } from "../../api/leagues";
import { fetchMatchPrediction, fetchSeasonPrediction, fetchTopScorerPrediction } from "../../api/predictions";
import { currentSeason } from "../../lib/season";
import { LoadingState } from "../../components/LoadingState";
import { ErrorState } from "../../components/ErrorState";
import { TeamPicker } from "../h2h/TeamPicker";
import { MatchPredictionResult } from "./MatchPredictionResult";
import { SeasonPredictionTable } from "./SeasonPredictionTable";
import { TopScorerPredictionTable } from "./TopScorerPredictionTable";

const season = currentSeason();
const BASE_SEASON = "2025-2026";

export function PredictionsPage() {
  const [homeLeagueId, setHomeLeagueId] = useState<number | null>(null);
  const [homeTeamId, setHomeTeamId] = useState<number | null>(null);
  const [awayLeagueId, setAwayLeagueId] = useState<number | null>(null);
  const [awayTeamId, setAwayTeamId] = useState<number | null>(null);

  const [seasonLeagueId, setSeasonLeagueId] = useState<number | null>(null);

  const matchPredictionQuery = useQuery({
    queryKey: ["matchPrediction", homeTeamId, awayTeamId],
    queryFn: () => fetchMatchPrediction(homeTeamId!, awayTeamId!),
    enabled: homeTeamId !== null && awayTeamId !== null,
  });

  const leaguesQuery = useQuery({
    queryKey: ["leagues"],
    queryFn: fetchLeagues,
  });

  const leagues = leaguesQuery.data ?? [];
  const selectedSeasonLeagueId = seasonLeagueId ?? leagues[0]?.id ?? null;

  const seasonPredictionQuery = useQuery({
    queryKey: ["seasonPrediction", selectedSeasonLeagueId, season],
    queryFn: () => fetchSeasonPrediction(selectedSeasonLeagueId!, season),
    enabled: selectedSeasonLeagueId !== null,
  });

  const topScorerPredictionQuery = useQuery({
    queryKey: ["topScorerPrediction", selectedSeasonLeagueId, BASE_SEASON],
    queryFn: () => fetchTopScorerPrediction(selectedSeasonLeagueId!, BASE_SEASON),
    enabled: selectedSeasonLeagueId !== null,
  });

  return (
    <section>
      <h1>예측</h1>

      <h2>경기 승률 예측</h2>
      <TeamPicker
        label="홈팀"
        leagueId={homeLeagueId}
        onLeagueChange={setHomeLeagueId}
        teamId={homeTeamId}
        onTeamChange={setHomeTeamId}
      />
      <TeamPicker
        label="원정팀"
        leagueId={awayLeagueId}
        onLeagueChange={setAwayLeagueId}
        teamId={awayTeamId}
        onTeamChange={setAwayTeamId}
      />

      {matchPredictionQuery.isLoading && <LoadingState />}
      {matchPredictionQuery.error && <ErrorState error={matchPredictionQuery.error} />}
      {matchPredictionQuery.data && <MatchPredictionResult prediction={matchPredictionQuery.data} />}

      <h2>시즌 순위 예측 ({season})</h2>
      {leaguesQuery.isLoading && <LoadingState />}
      {leaguesQuery.error && <ErrorState error={leaguesQuery.error} />}
      {leagues.length > 0 && (
        <select
          value={selectedSeasonLeagueId ?? ""}
          onChange={(e) => setSeasonLeagueId(Number(e.target.value))}
        >
          {leagues.map((league) => (
            <option key={league.id} value={league.id}>
              {league.name}
            </option>
          ))}
        </select>
      )}

      {seasonPredictionQuery.isLoading && <LoadingState />}
      {seasonPredictionQuery.error && <ErrorState error={seasonPredictionQuery.error} />}
      {seasonPredictionQuery.data && <SeasonPredictionTable predictions={seasonPredictionQuery.data} />}

      <h2>선수 득점왕 예측 ({season})</h2>
      <p>{BASE_SEASON} 시즌 경기당 득점률을 바탕으로 추정한 값이에요.</p>
      {topScorerPredictionQuery.isLoading && <LoadingState />}
      {topScorerPredictionQuery.error && <ErrorState error={topScorerPredictionQuery.error} />}
      {topScorerPredictionQuery.data && (
        <TopScorerPredictionTable predictions={topScorerPredictionQuery.data} />
      )}
    </section>
  );
}
