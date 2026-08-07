import { useQuery } from "@tanstack/react-query";
import { fetchLiveMatches, fetchRecentMatches } from "../api/matches";

const REFRESH_INTERVAL_MS = 60_000;
const FALLBACK_LIMIT = 10;

export function LiveTicker() {
  const liveMatchesQuery = useQuery({
    queryKey: ["liveMatches"],
    queryFn: fetchLiveMatches,
    refetchInterval: REFRESH_INTERVAL_MS,
  });

  const isLive = (liveMatchesQuery.data?.length ?? 0) > 0;

  const recentMatchesQuery = useQuery({
    queryKey: ["recentMatches", FALLBACK_LIMIT],
    queryFn: () => fetchRecentMatches(FALLBACK_LIMIT),
    enabled: liveMatchesQuery.isSuccess && !isLive,
  });

  const matches = isLive ? liveMatchesQuery.data! : recentMatchesQuery.data ?? [];
  if (matches.length === 0) return null;

  function renderItems(keyPrefix: string) {
    return matches.map((match) => (
      <span key={`${keyPrefix}-${match.id}`} className="live-ticker-item">
        <span className="live-ticker-league">{match.leagueName}</span>
        {match.homeTeamName} <strong>{match.homeScore}-{match.awayScore}</strong> {match.awayTeamName}
      </span>
    ));
  }

  return (
    <div className="live-ticker">
      <span className={isLive ? "live-ticker-tag" : "live-ticker-tag live-ticker-tag-idle"}>
        {isLive ? "LIVE" : "RESULTS"}
      </span>
      <div className="live-ticker-track">
        <div className="live-ticker-content">
          {renderItems("a")}
          {renderItems("b")}
        </div>
      </div>
    </div>
  );
}
