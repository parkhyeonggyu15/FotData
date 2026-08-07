import { TeamCrest } from "../../components/TeamCrest";
import type { MatchResponse } from "../../types/api";

const STATUS_LABEL: Record<MatchResponse["status"], string> = {
  SCHEDULED: "예정",
  LIVE: "진행중",
  FINISHED: "종료",
  POSTPONED: "연기",
  CANCELLED: "취소",
};

const STATUS_CLASS: Record<MatchResponse["status"], string> = {
  SCHEDULED: "status-scheduled",
  LIVE: "status-live",
  FINISHED: "status-finished",
  POSTPONED: "status-scheduled",
  CANCELLED: "status-scheduled",
};

export function MatchCard({ match }: { match: MatchResponse }) {
  const score =
    match.homeScore !== null && match.awayScore !== null
      ? `${match.homeScore} : ${match.awayScore}`
      : "- : -";

  return (
    <article className="match-card">
      <span className="match-date">{new Date(match.matchDate).toLocaleString("ko-KR")}</span>
      <span className={`match-status ${STATUS_CLASS[match.status]}`}>{STATUS_LABEL[match.status]}</span>
      <span className="team-home team-name">
        {match.homeTeamName}
        <TeamCrest src={match.homeTeamCrestUrl} alt="" />
      </span>
      <strong className="score">{score}</strong>
      <span className="team-away team-name">
        <TeamCrest src={match.awayTeamCrestUrl} alt="" />
        {match.awayTeamName}
      </span>
    </article>
  );
}
