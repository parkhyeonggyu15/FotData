import { useState } from "react";
import { useQuery } from "@tanstack/react-query";
import { fetchLeagues } from "../../api/leagues";
import { fetchTopConceders, fetchTopScorers } from "../../api/analysis";
import { currentSeason } from "../../lib/season";
import { LoadingState } from "../../components/LoadingState";
import { ErrorState } from "../../components/ErrorState";
import { RankingTable } from "./RankingTable";

const season = currentSeason();

export function RankingsPage() {
  const [leagueId, setLeagueId] = useState<number | null>(null);

  const leaguesQuery = useQuery({
    queryKey: ["leagues"],
    queryFn: fetchLeagues,
  });

  const leagues = leaguesQuery.data ?? [];
  const selectedLeagueId = leagueId ?? leagues[0]?.id ?? null;

  const scorersQuery = useQuery({
    queryKey: ["topScorers", selectedLeagueId, season],
    queryFn: () => fetchTopScorers(selectedLeagueId!, season),
    enabled: selectedLeagueId !== null,
  });

  const concedersQuery = useQuery({
    queryKey: ["topConceders", selectedLeagueId, season],
    queryFn: () => fetchTopConceders(selectedLeagueId!, season),
    enabled: selectedLeagueId !== null,
  });

  if (leaguesQuery.isLoading) return <LoadingState />;
  if (leaguesQuery.error) return <ErrorState error={leaguesQuery.error} />;

  return (
    <section>
      <h1>순위표 ({season})</h1>

      <label htmlFor="league-select">리그</label>
      <select
        id="league-select"
        value={selectedLeagueId ?? ""}
        onChange={(e) => setLeagueId(Number(e.target.value))}
      >
        {leagues.map((league) => (
          <option key={league.id} value={league.id}>
            {league.name}
          </option>
        ))}
      </select>

      <h2>득점 순위</h2>
      {scorersQuery.isLoading && <LoadingState />}
      {scorersQuery.error && <ErrorState error={scorersQuery.error} />}
      {scorersQuery.data && <RankingTable rankings={scorersQuery.data} metricLabel="득점" metric="goalsFor" />}

      <h2>실점 순위</h2>
      {concedersQuery.isLoading && <LoadingState />}
      {concedersQuery.error && <ErrorState error={concedersQuery.error} />}
      {concedersQuery.data && (
        <RankingTable rankings={concedersQuery.data} metricLabel="실점" metric="goalsAgainst" />
      )}
    </section>
  );
}
