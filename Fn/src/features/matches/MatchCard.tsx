import type { MatchResponse } from "../../types/api";

const STATUS_LABEL: Record<MatchResponse["status"], string> = {
  SCHEDULED: "예정",
  LIVE: "진행중",
  FINISHED: "종료",
  POSTPONED: "연기",
  CANCELLED: "취소",
};

export function MatchCard({ match }: { match: MatchResponse }) {
  const score =
    match.homeScore !== null && match.awayScore !== null
      ? `${match.homeScore} : ${match.awayScore}`
      : "- : -";

  return (
    <article>
      <span>{new Date(match.matchDate).toLocaleString("ko-KR")}</span>
      <span>{STATUS_LABEL[match.status]}</span>
      <span>{match.homeTeamName}</span>
      <strong>{score}</strong>
      <span>{match.awayTeamName}</span>
    </article>
  );
}
