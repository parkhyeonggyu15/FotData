import { useParams } from "react-router-dom";
import { useQuery } from "@tanstack/react-query";
import { fetchTeamStats } from "../../api/teamStats";
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

  if (statsQuery.isLoading) return <LoadingState />;
  if (statsQuery.error) return <ErrorState error={statsQuery.error} />;
  if (!statsQuery.data) return null;

  const stats = statsQuery.data;

  return (
    <section>
      <h1>{stats.teamName}</h1>
      <p>{stats.season} 시즌</p>

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
    </section>
  );
}
