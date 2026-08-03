import { useState } from "react";
import { Link } from "react-router-dom";
import { useQuery } from "@tanstack/react-query";
import { fetchLeagues } from "../../api/leagues";
import { fetchRecentMatches } from "../../api/matches";
import { fetchEloRankings } from "../../api/analysis";
import { LoadingState } from "../../components/LoadingState";
import { ErrorState } from "../../components/ErrorState";
import { MatchCard } from "../matches/MatchCard";
import { EloRankingTable } from "../rankings/EloRankingTable";

const RECENT_MATCHES_LIMIT = 5;
const ELO_TOP_N = 5;

export function HomePage() {
  const [leagueId, setLeagueId] = useState<number | null>(null);

  const leaguesQuery = useQuery({
    queryKey: ["leagues"],
    queryFn: fetchLeagues,
  });

  const recentMatchesQuery = useQuery({
    queryKey: ["recentMatches", RECENT_MATCHES_LIMIT],
    queryFn: () => fetchRecentMatches(RECENT_MATCHES_LIMIT),
  });

  const leagues = leaguesQuery.data ?? [];
  const selectedLeagueId = leagueId ?? leagues[0]?.id ?? null;

  const eloQuery = useQuery({
    queryKey: ["eloRankings", selectedLeagueId],
    queryFn: () => fetchEloRankings(selectedLeagueId!),
    enabled: selectedLeagueId !== null,
  });

  return (
    <section>
      <h1>FotData</h1>
      <p>해외축구 경기 결과와 팀 분석을 한눈에 확인하세요.</p>

      <nav>
        <Link to="/matches">경기 목록 보기</Link>
        <Link to="/rankings">순위표 보기</Link>
        <Link to="/h2h">상대전적 보기</Link>
        <Link to="/predictions">예측 보기</Link>
      </nav>

      <h2>최근 경기 결과</h2>
      {recentMatchesQuery.isLoading && <LoadingState />}
      {recentMatchesQuery.error && <ErrorState error={recentMatchesQuery.error} />}
      {recentMatchesQuery.data && (
        <ul>
          {recentMatchesQuery.data.map((match) => (
            <li key={match.id}>
              <MatchCard match={match} />
            </li>
          ))}
        </ul>
      )}

      <h2>ELO TOP {ELO_TOP_N}</h2>
      {leaguesQuery.isLoading && <LoadingState />}
      {leaguesQuery.error && <ErrorState error={leaguesQuery.error} />}
      {leagues.length > 0 && (
        <select
          value={selectedLeagueId ?? ""}
          onChange={(e) => setLeagueId(Number(e.target.value))}
        >
          {leagues.map((league) => (
            <option key={league.id} value={league.id}>
              {league.name}
            </option>
          ))}
        </select>
      )}
      {eloQuery.isLoading && <LoadingState />}
      {eloQuery.error && <ErrorState error={eloQuery.error} />}
      {eloQuery.data && <EloRankingTable rankings={eloQuery.data.slice(0, ELO_TOP_N)} />}
    </section>
  );
}
