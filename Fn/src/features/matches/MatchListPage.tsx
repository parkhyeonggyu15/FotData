import { useState } from "react";
import { useQuery } from "@tanstack/react-query";
import { fetchLeagues } from "../../api/leagues";
import { fetchMatches } from "../../api/matches";
import { currentSeason } from "../../lib/season";
import { LoadingState } from "../../components/LoadingState";
import { ErrorState } from "../../components/ErrorState";
import { MatchCard } from "./MatchCard";

const season = currentSeason();

export function MatchListPage() {
  const [leagueId, setLeagueId] = useState<number | null>(null);
  const [matchday, setMatchday] = useState(1);

  const leaguesQuery = useQuery({
    queryKey: ["leagues"],
    queryFn: fetchLeagues,
  });

  const matchesQuery = useQuery({
    queryKey: ["matches", leagueId, season, matchday],
    queryFn: () => fetchMatches(leagueId!, season, matchday),
    enabled: leagueId !== null,
  });

  if (leaguesQuery.isLoading) return <LoadingState />;
  if (leaguesQuery.error) return <ErrorState error={leaguesQuery.error} />;

  const leagues = leaguesQuery.data ?? [];
  const selectedLeagueId = leagueId ?? leagues[0]?.id ?? null;

  return (
    <section>
      <h1>경기 목록</h1>

      <div>
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

        <label htmlFor="matchday-input">라운드</label>
        <input
          id="matchday-input"
          type="number"
          min={1}
          value={matchday}
          onChange={(e) => setMatchday(Number(e.target.value))}
        />
      </div>

      {matchesQuery.isLoading && <LoadingState />}
      {matchesQuery.error && <ErrorState error={matchesQuery.error} />}
      {matchesQuery.data && (
        <ul>
          {matchesQuery.data.map((match) => (
            <li key={match.id}>
              <MatchCard match={match} />
            </li>
          ))}
        </ul>
      )}
    </section>
  );
}
