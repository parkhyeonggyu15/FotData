import { useQuery } from "@tanstack/react-query";
import { fetchMatchPrediction } from "../../api/predictions";
import { fetchTeamStats } from "../../api/teamStats";
import { currentSeason } from "../../lib/season";
import { TeamCrest } from "../../components/TeamCrest";
import { LoadingState } from "../../components/LoadingState";
import { RecentFormBadges } from "../team/RecentFormBadges";
import type { MatchResponse } from "../../types/api";

const season = currentSeason();

function toPercent(value: number) {
  return `${Math.round(value * 100)}%`;
}

export function FeaturedMatchCard({ match }: { match: MatchResponse }) {
  const predictionQuery = useQuery({
    queryKey: ["matchPrediction", match.homeTeamId, match.awayTeamId],
    queryFn: () => fetchMatchPrediction(match.homeTeamId, match.awayTeamId),
  });

  const homeStatsQuery = useQuery({
    queryKey: ["teamStats", match.homeTeamId, season],
    queryFn: () => fetchTeamStats(match.homeTeamId, season),
    retry: false,
  });

  const awayStatsQuery = useQuery({
    queryKey: ["teamStats", match.awayTeamId, season],
    queryFn: () => fetchTeamStats(match.awayTeamId, season),
    retry: false,
  });

  const prediction = predictionQuery.data;
  const matchDate = new Date(match.matchDate).toLocaleString("ko-KR", {
    month: "long",
    day: "numeric",
    weekday: "short",
    hour: "2-digit",
    minute: "2-digit",
  });

  return (
    <div className="featured-card">
      <div className="featured-card-meta">
        <span>{match.leagueName}</span>
        <span>{matchDate}</span>
      </div>

      <div className="featured-card-teams">
        <div className="featured-card-team">
          <TeamCrest src={match.homeTeamCrestUrl} alt="" size={44} />
          <span className="featured-card-team-name">{match.homeTeamName}</span>
          {homeStatsQuery.data && <RecentFormBadges recentForm={homeStatsQuery.data.recentForm} />}
        </div>

        <div className="featured-card-center">
          {predictionQuery.isLoading && <LoadingState />}
          {prediction && (
            <div className="featured-card-odds">
              <div className="featured-card-odd">
                <span>홈승</span>
                <strong>{toPercent(prediction.homeWinProbability)}</strong>
              </div>
              <div className="featured-card-odd featured-card-odd-draw">
                <span>무승부</span>
                <strong>{toPercent(prediction.drawProbability)}</strong>
              </div>
              <div className="featured-card-odd">
                <span>원정승</span>
                <strong>{toPercent(prediction.awayWinProbability)}</strong>
              </div>
            </div>
          )}
        </div>

        <div className="featured-card-team featured-card-team-away">
          <TeamCrest src={match.awayTeamCrestUrl} alt="" size={44} />
          <span className="featured-card-team-name">{match.awayTeamName}</span>
          {awayStatsQuery.data && <RecentFormBadges recentForm={awayStatsQuery.data.recentForm} />}
        </div>
      </div>

      {prediction && (
        <p className="featured-card-comment">
          ELO 기준 {prediction.homeWinProbability >= prediction.awayWinProbability ? match.homeTeamName : match.awayTeamName}
          {" "}
          쪽이 근소하게 우세해요. 무승부 확률은 {toPercent(prediction.drawProbability)}예요.
        </p>
      )}
    </div>
  );
}
