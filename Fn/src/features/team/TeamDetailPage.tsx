import { useParams } from "react-router-dom";
import { useQuery } from "@tanstack/react-query";
import { fetchTeamStats } from "../../api/teamStats";
import { fetchTeamElo } from "../../api/elo";
import { ApiError } from "../../api/client";
import { currentSeason } from "../../lib/season";
import { LoadingState } from "../../components/LoadingState";
import { ErrorState } from "../../components/ErrorState";
import { RecentFormBadges } from "./RecentFormBadges";

const season = currentSeason();

export function TeamDetailPage() {
  const { teamId } = useParams<{ teamId: string }>();

  const statsQuery = useQuery({
    queryKey: ["teamStats", teamId, season],
    queryFn: () => fetchTeamStats(Number(teamId), season),
  });

  const eloQuery = useQuery({
    queryKey: ["teamElo", teamId],
    queryFn: () => fetchTeamElo(Number(teamId)),
  });

  const stats = statsQuery.data;

  return (
    <section>
      <h1>{stats?.teamName ?? eloQuery.data?.teamName}</h1>

      <h2>{season} 시즌 성적</h2>
      {statsQuery.isLoading && <LoadingState />}
      {statsQuery.error instanceof ApiError && statsQuery.error.status === 404 && (
        <p>아직 {season} 시즌 경기 기록이 없어요.</p>
      )}
      {statsQuery.error && !(statsQuery.error instanceof ApiError && statsQuery.error.status === 404) && (
        <ErrorState error={statsQuery.error} />
      )}
      {stats && (
        <>
          <dl>
            <dt>경기</dt>
            <dd>{stats.played}</dd>
            <dt>승</dt>
            <dd>{stats.win}</dd>
            <dt>무</dt>
            <dd>{stats.draw}</dd>
            <dt>패</dt>
            <dd>{stats.lose}</dd>
            <dt>득점</dt>
            <dd>{stats.goalsFor}</dd>
            <dt>실점</dt>
            <dd>{stats.goalsAgainst}</dd>
            <dt>홈 득실차</dt>
            <dd>{stats.homeGoalDifference}</dd>
            <dt>원정 득실차</dt>
            <dd>{stats.awayGoalDifference}</dd>
          </dl>

          <h2>최근 폼</h2>
          <RecentFormBadges recentForm={stats.recentForm} />
        </>
      )}

      <h2>ELO 레이팅</h2>
      {eloQuery.isLoading && <LoadingState />}
      {eloQuery.error && <ErrorState error={eloQuery.error} />}
      {eloQuery.data && <p>{Math.round(eloQuery.data.rating)}</p>}
    </section>
  );
}
