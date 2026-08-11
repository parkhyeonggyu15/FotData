import { useState } from "react";
import { useQuery } from "@tanstack/react-query";
import { fetchLeagues } from "../../api/leagues";
import { fetchRecentMatches } from "../../api/matches";
import { fetchEloRankings } from "../../api/analysis";
import { currentSeason } from "../../lib/season";
import { LoadingState } from "../../components/LoadingState";
import { ErrorState } from "../../components/ErrorState";
import { MatchCard } from "../matches/MatchCard";
import { EloRankingTable } from "../rankings/EloRankingTable";
import { FeaturedMatchCard } from "./FeaturedMatchCard";

const RECENT_MATCHES_LIMIT = 6;
const ELO_TOP_N = 5;
const season = currentSeason();

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

  const topTeam = eloQuery.data?.[0];
  const featuredMatch = recentMatchesQuery.data?.[0];
  const otherMatches = recentMatchesQuery.data?.slice(1) ?? [];

  return (
    <section className="stack">
      

      <div className="stat-cards">
        <div className="stat-card">
          <span className="stat-card-label">동기화된 리그</span>
          <span className="stat-card-value">{leagues.length || "-"}</span>
          <span className="stat-card-sub">개 대회</span>
        </div>
        <div className="stat-card">
          <span className="stat-card-label">최근 표시 경기</span>
          <span className="stat-card-value">{recentMatchesQuery.data?.length ?? "-"}</span>
          <span className="stat-card-sub">건</span>
        </div>
        <div className="stat-card">
          <span className="stat-card-label">현재 ELO 1위</span>
          <span className="stat-card-value stat-card-value-text">{topTeam?.teamName ?? "-"}</span>
          <span className="stat-card-sub">{topTeam ? `${Math.round(topTeam.rating)} pt` : " "}</span>
        </div>
        <div className="stat-card">
          <span className="stat-card-label">기준 시즌</span>
          <span className="stat-card-value">{season}</span>
          <span className="stat-card-sub">현재 시즌</span>
        </div>
      </div>

      {featuredMatch && (
        <div className="card featured-match">
          <div className="section-head">
            <h2>주목 경기</h2>
          </div>
          <FeaturedMatchCard match={featuredMatch} />
        </div>
      )}

      <div className="home-split">
        <div className="card">
          <div className="section-head">
            <h2>ELO 순위 TOP {ELO_TOP_N}</h2>
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
          </div>
          {leaguesQuery.isLoading && <LoadingState />}
          {leaguesQuery.error && <ErrorState error={leaguesQuery.error} />}
          {eloQuery.isLoading && <LoadingState />}
          {eloQuery.error && <ErrorState error={eloQuery.error} />}
          {eloQuery.data && <EloRankingTable rankings={eloQuery.data.slice(0, ELO_TOP_N)} />}
        </div>

        <div className="card">
          <div className="section-head">
            <h2>최근 결과</h2>
          </div>
          {recentMatchesQuery.isLoading && <LoadingState />}
          {recentMatchesQuery.error && <ErrorState error={recentMatchesQuery.error} />}
          {otherMatches.length > 0 && (
            <ul className="match-list">
              {otherMatches.map((match) => (
                <li key={match.id}>
                  <MatchCard match={match} />
                </li>
              ))}
            </ul>
          )}
        </div>
      </div>
    </section>
  );
}
