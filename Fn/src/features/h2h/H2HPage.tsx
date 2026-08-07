import { useState } from "react";
import { useQuery } from "@tanstack/react-query";
import { fetchHeadToHead } from "../../api/analysis";
import { LoadingState } from "../../components/LoadingState";
import { ErrorState } from "../../components/ErrorState";
import { MatchCard } from "../matches/MatchCard";
import { TeamPicker } from "./TeamPicker";

export function H2HPage() {
  const [leagueAId, setLeagueAId] = useState<number | null>(null);
  const [teamAId, setTeamAId] = useState<number | null>(null);
  const [leagueBId, setLeagueBId] = useState<number | null>(null);
  const [teamBId, setTeamBId] = useState<number | null>(null);

  const h2hQuery = useQuery({
    queryKey: ["h2h", teamAId, teamBId],
    queryFn: () => fetchHeadToHead(teamAId!, teamBId!),
    enabled: teamAId !== null && teamBId !== null,
  });

  return (
    <section className="stack">
      <h1>상대전적</h1>

      <div className="controls">
        <TeamPicker
          label="팀 A"
          leagueId={leagueAId}
          onLeagueChange={setLeagueAId}
          teamId={teamAId}
          onTeamChange={setTeamAId}
        />
        <TeamPicker
          label="팀 B"
          leagueId={leagueBId}
          onLeagueChange={setLeagueBId}
          teamId={teamBId}
          onTeamChange={setTeamBId}
        />
      </div>

      {h2hQuery.isLoading && <LoadingState />}
      {h2hQuery.error && <ErrorState error={h2hQuery.error} />}
      {h2hQuery.data && (
        <div className="card">
          <p className="h2h-summary">
            {h2hQuery.data.teamAWins}승 {h2hQuery.data.draws}무 {h2hQuery.data.teamBWins}패
          </p>
          <h2 style={{ marginTop: "1rem", marginBottom: "0.75rem" }}>최근 맞대결</h2>
          <ul className="match-list">
            {h2hQuery.data.recentMatches.map((match) => (
              <li key={match.id}>
                <MatchCard match={match} />
              </li>
            ))}
          </ul>
        </div>
      )}
    </section>
  );
}
