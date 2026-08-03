import { Link } from "react-router-dom";
import type { PlayerScorerResponse } from "../../types/api";

export function PlayerScorerTable({ scorers }: { scorers: PlayerScorerResponse[] }) {
  return (
    <table>
      <thead>
        <tr>
          <th>순위</th>
          <th>선수</th>
          <th>팀</th>
          <th>득점</th>
          <th>도움</th>
          <th>출전</th>
        </tr>
      </thead>
      <tbody>
        {scorers.map((scorer, index) => (
          <tr key={scorer.playerId}>
            <td>{index + 1}</td>
            <td>{scorer.playerName}</td>
            <td>
              <Link to={`/teams/${scorer.teamId}`}>{scorer.teamName}</Link>
            </td>
            <td>{scorer.goals}</td>
            <td>{scorer.assists}</td>
            <td>{scorer.playedMatches}</td>
          </tr>
        ))}
      </tbody>
    </table>
  );
}
