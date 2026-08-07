import { useParams } from "react-router-dom";
import { useQuery } from "@tanstack/react-query";
import { fetchTeamStats } from "../../api/teamStats";
import { fetchTeamElo } from "../../api/elo";
import { ApiError } from "../../api/client";
import { currentSeason } from "../../lib/season";
import { LoadingState } from "../../components/LoadingState";
import { ErrorState } from "../../components/ErrorState";
import { TeamCrest } from "../../components/TeamCrest";
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
  const teamName = stats?.teamName ?? eloQuery.data?.teamName;
  const teamCrestUrl = stats?.teamCrestUrl ?? eloQuery.data?.teamCrestUrl ?? null;

  return (
    <section className="stack">
      <h1 className="team-name team-name-heading">
        <TeamCrest src={teamCrestUrl} alt="" size={36} />
        {teamName}
      </h1>

      <div className="card">
        <div className="section-head">
          <h2>{season} 시즌 성적</h2>
        </div>
        {statsQuery.isLoading && <LoadingState />}
        {statsQuery.error instanceof ApiError && statsQuery.error.status === 404 && (
          <p className="status-text">아직 {season} 시즌 경기 기록이 없어요.</p>
        )}
        {statsQuery.error && !(statsQuery.error instanceof ApiError && statsQuery.error.status === 404) && (
          <ErrorState error={statsQuery.error} />
        )}
        {stats && (
          <>
            <dl className="stat-grid">
              <div>
                <dt>경기</dt>
                <dd>{stats.played}</dd>
              </div>
              <div>
                <dt>승</dt>
                <dd>{stats.win}</dd>
              </div>
              <div>
                <dt>무</dt>
                <dd>{stats.draw}</dd>
              </div>
              <div>
                <dt>패</dt>
                <dd>{stats.lose}</dd>
              </div>
              <div>
                <dt>득점</dt>
                <dd>{stats.goalsFor}</dd>
              </div>
              <div>
                <dt>실점</dt>
                <dd>{stats.goalsAgainst}</dd>
              </div>
              <div>
                <dt>홈 득실차</dt>
                <dd>{stats.homeGoalDifference}</dd>
              </div>
              <div>
                <dt>원정 득실차</dt>
                <dd>{stats.awayGoalDifference}</dd>
              </div>
            </dl>

            <h2 style={{ marginTop: "1.25rem" }}>최근 폼</h2>
            <RecentFormBadges recentForm={stats.recentForm} />
          </>
        )}
      </div>

      <div className="card">
        <div className="section-head">
          <h2>ELO 레이팅</h2>
        </div>
        {eloQuery.isLoading && <LoadingState />}
        {eloQuery.error && <ErrorState error={eloQuery.error} />}
        {eloQuery.data && <p className="elo-value">{Math.round(eloQuery.data.rating)}</p>}
      </div>
    </section>
  );
}
