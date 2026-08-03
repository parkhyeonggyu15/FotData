import { useState } from "react";
import { useQuery } from "@tanstack/react-query";
import { fetchLeagues } from "../../api/leagues";
import { fetchEloRankings, fetchPlayerScorers, fetchTopConceders, fetchTopScorers } from "../../api/analysis";
import { currentSeason } from "../../lib/season";
import { LoadingState } from "../../components/LoadingState";
import { ErrorState } from "../../components/ErrorState";
import { RankingTable } from "./RankingTable";
import { EloRankingTable } from "./EloRankingTable";
import { PlayerScorerTable } from "./PlayerScorerTable";

const SEASON_OPTIONS = ["2026-2027", "2025-2026"];

export function RankingsPage() {
  const [leagueId, setLeagueId] = useState<number | null>(null);
  const [season, setSeason] = useState(currentSeason());

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

  const eloQuery = useQuery({
    queryKey: ["eloRankings", selectedLeagueId],
    queryFn: () => fetchEloRankings(selectedLeagueId!),
    enabled: selectedLeagueId !== null,
  });

  const playerScorersQuery = useQuery({
    queryKey: ["playerScorers", selectedLeagueId, season],
    queryFn: () => fetchPlayerScorers(selectedLeagueId!, season),
    enabled: selectedLeagueId !== null,
  });

  if (leaguesQuery.isLoading) return <LoadingState />;
  if (leaguesQuery.error) return <ErrorState error={leaguesQuery.error} />;

  return (
    <section>
      <h1>순위표</h1>

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

      <label htmlFor="season-select">시즌</label>
      <select id="season-select" value={season} onChange={(e) => setSeason(e.target.value)}>
        {SEASON_OPTIONS.map((option) => (
          <option key={option} value={option}>
            {option}
          </option>
        ))}
      </select>

      <h2>팀 득점 순위 ({season})</h2>
      {scorersQuery.isLoading && <LoadingState />}
      {scorersQuery.error && <ErrorState error={scorersQuery.error} />}
      {scorersQuery.data && <RankingTable rankings={scorersQuery.data} metricLabel="득점" metric="goalsFor" />}

      <h2>팀 실점 순위 ({season})</h2>
      {concedersQuery.isLoading && <LoadingState />}
      {concedersQuery.error && <ErrorState error={concedersQuery.error} />}
      {concedersQuery.data && (
        <RankingTable rankings={concedersQuery.data} metricLabel="실점" metric="goalsAgainst" />
      )}

      <h2>선수 득점 순위 ({season})</h2>
      {playerScorersQuery.isLoading && <LoadingState />}
      {playerScorersQuery.error && <ErrorState error={playerScorersQuery.error} />}
      {playerScorersQuery.data && <PlayerScorerTable scorers={playerScorersQuery.data} />}

      <h2>ELO 순위 (현재)</h2>
      <p>ELO는 시즌 구분 없이 누적되는 지표라 선택한 시즌과 무관하게 최신 값을 보여줘요.</p>
      {eloQuery.isLoading && <LoadingState />}
      {eloQuery.error && <ErrorState error={eloQuery.error} />}
      {eloQuery.data && <EloRankingTable rankings={eloQuery.data} />}
    </section>
  );
}
