import { useState } from "react";
import { useQuery } from "@tanstack/react-query";
import { fetchLeagues } from "../../api/leagues";
import { fetchMatches } from "../../api/matches";
import { currentSeason } from "../../lib/season";
import { LoadingState } from "../../components/LoadingState";
import { ErrorState } from "../../components/ErrorState";
import { LeagueTabs } from "../../components/LeagueTabs";
import { MatchCard } from "./MatchCard";

const SEASON_OPTIONS = ["2026-2027", "2025-2026"];

export function MatchListPage() {
  const [leagueId, setLeagueId] = useState<number | null>(null);
  const [season, setSeason] = useState(currentSeason());
  const [matchday, setMatchday] = useState(1);

  const leaguesQuery = useQuery({
    queryKey: ["leagues"],
    queryFn: fetchLeagues,
  });

  const leagues = leaguesQuery.data ?? [];
  const selectedLeagueId = leagueId ?? leagues[0]?.id ?? null;

  const matchesQuery = useQuery({
    queryKey: ["matches", selectedLeagueId, season, matchday],
    queryFn: () => fetchMatches(selectedLeagueId!, season, matchday),
    enabled: selectedLeagueId !== null,
  });

  if (leaguesQuery.isLoading) return <LoadingState />;
  if (leaguesQuery.error) return <ErrorState error={leaguesQuery.error} />;

  return (
    <section className="stack">
      <h1>경기 목록</h1>

      <LeagueTabs leagues={leagues} selectedLeagueId={selectedLeagueId} onSelect={setLeagueId} />

      <div className="card">
        <div className="controls">
          <div className="field">
            <label htmlFor="season-select">시즌</label>
            <select id="season-select" value={season} onChange={(e) => setSeason(e.target.value)}>
              {SEASON_OPTIONS.map((option) => (
                <option key={option} value={option}>
                  {option}
                </option>
              ))}
            </select>
          </div>

          <div className="field">
            <label htmlFor="matchday-input">라운드</label>
            <input
              id="matchday-input"
              type="number"
              min={1}
              value={matchday}
              onChange={(e) => setMatchday(Number(e.target.value))}
            />
          </div>
        </div>

        {matchesQuery.isLoading && <LoadingState />}
        {matchesQuery.error && <ErrorState error={matchesQuery.error} />}
        {matchesQuery.data && matchesQuery.data.length === 0 && (
          <p className="status-text">해당 라운드에 경기가 없어요.</p>
        )}
        {matchesQuery.data && matchesQuery.data.length > 0 && (
          <ul className="match-list">
            {matchesQuery.data.map((match) => (
              <li key={match.id}>
                <MatchCard match={match} />
              </li>
            ))}
          </ul>
        )}
      </div>
    </section>
  );
}
