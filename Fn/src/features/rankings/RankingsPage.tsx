import { useState } from "react";
import { useQuery } from "@tanstack/react-query";
import { fetchLeagues } from "../../api/leagues";
import { fetchEloRankings, fetchPlayerScorers, fetchTopConceders, fetchTopScorers } from "../../api/analysis";
import { currentSeason } from "../../lib/season";
import { LoadingState } from "../../components/LoadingState";
import { ErrorState } from "../../components/ErrorState";
import { LeagueTabs } from "../../components/LeagueTabs";
import { RankingTable } from "./RankingTable";
import { EloRankingTable } from "./EloRankingTable";
import { PlayerScorerTable } from "./PlayerScorerTable";

const SEASON_OPTIONS = ["2026-2027", "2025-2026"];
const UCL_QUALIFY_RANK = 4;

type Tab = "goals" | "conceded" | "players" | "elo";

const TABS: { key: Tab; label: string }[] = [
  { key: "goals", label: "팀 득점 순위" },
  { key: "conceded", label: "팀 실점 순위" },
  { key: "players", label: "선수 득점 순위" },
  { key: "elo", label: "ELO 순위" },
];

export function RankingsPage() {
  const [leagueId, setLeagueId] = useState<number | null>(null);
  const [season, setSeason] = useState(currentSeason());
  const [tab, setTab] = useState<Tab>("goals");

  const leaguesQuery = useQuery({
    queryKey: ["leagues"],
    queryFn: fetchLeagues,
  });

  const leagues = leaguesQuery.data ?? [];
  const selectedLeagueId = leagueId ?? leagues[0]?.id ?? null;

  const scorersQuery = useQuery({
    queryKey: ["topScorers", selectedLeagueId, season],
    queryFn: () => fetchTopScorers(selectedLeagueId!, season),
    enabled: selectedLeagueId !== null && tab === "goals",
  });

  const concedersQuery = useQuery({
    queryKey: ["topConceders", selectedLeagueId, season],
    queryFn: () => fetchTopConceders(selectedLeagueId!, season),
    enabled: selectedLeagueId !== null && tab === "conceded",
  });

  const eloQuery = useQuery({
    queryKey: ["eloRankings", selectedLeagueId],
    queryFn: () => fetchEloRankings(selectedLeagueId!),
    enabled: selectedLeagueId !== null && tab === "elo",
  });

  const playerScorersQuery = useQuery({
    queryKey: ["playerScorers", selectedLeagueId, season],
    queryFn: () => fetchPlayerScorers(selectedLeagueId!, season),
    enabled: selectedLeagueId !== null && tab === "players",
  });

  if (leaguesQuery.isLoading) return <LoadingState />;
  if (leaguesQuery.error) return <ErrorState error={leaguesQuery.error} />;

  return (
    <section className="stack">
      <h1>순위표</h1>

      <LeagueTabs leagues={leagues} selectedLeagueId={selectedLeagueId} onSelect={setLeagueId} />

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
      </div>

      <div className="card">
        <div className="sub-tabs">
          {TABS.map((t) => (
            <button
              key={t.key}
              type="button"
              className={t.key === tab ? "sub-tab active" : "sub-tab"}
              onClick={() => setTab(t.key)}
            >
              {t.label}
            </button>
          ))}
        </div>

        {tab === "goals" && (
          <>
            {scorersQuery.isLoading && <LoadingState />}
            {scorersQuery.error && <ErrorState error={scorersQuery.error} />}
            {scorersQuery.data && (
              <>
                <p className="section-hint">굵은 왼쪽 라인은 상위 {UCL_QUALIFY_RANK}위, 챔피언스리그 진출권 예상 구간이에요.</p>
                <RankingTable
                  rankings={scorersQuery.data}
                  metricLabel="득점"
                  metric="goalsFor"
                  highlightTopN={UCL_QUALIFY_RANK}
                />
              </>
            )}
          </>
        )}

        {tab === "conceded" && (
          <>
            {concedersQuery.isLoading && <LoadingState />}
            {concedersQuery.error && <ErrorState error={concedersQuery.error} />}
            {concedersQuery.data && (
              <RankingTable rankings={concedersQuery.data} metricLabel="실점" metric="goalsAgainst" />
            )}
          </>
        )}

        {tab === "players" && (
          <>
            {playerScorersQuery.isLoading && <LoadingState />}
            {playerScorersQuery.error && <ErrorState error={playerScorersQuery.error} />}
            {playerScorersQuery.data && <PlayerScorerTable scorers={playerScorersQuery.data} />}
          </>
        )}

        {tab === "elo" && (
          <>
            <p className="section-hint">ELO는 시즌 구분 없이 누적되는 지표라 최신 값을 보여줘요.</p>
            {eloQuery.isLoading && <LoadingState />}
            {eloQuery.error && <ErrorState error={eloQuery.error} />}
            {eloQuery.data && <EloRankingTable rankings={eloQuery.data} />}
          </>
        )}
      </div>
    </section>
  );
}
