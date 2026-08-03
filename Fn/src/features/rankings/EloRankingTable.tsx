import { Link } from "react-router-dom";
import type { EloResponse } from "../../types/api";

export function EloRankingTable({ rankings }: { rankings: EloResponse[] }) {
  return (
    <table>
      <thead>
        <tr>
          <th>순위</th>
          <th>팀</th>
          <th>ELO</th>
        </tr>
      </thead>
      <tbody>
        {rankings.map((ranking, index) => (
          <tr key={ranking.teamId}>
            <td>{index + 1}</td>
            <td>
              <Link to={`/teams/${ranking.teamId}`}>{ranking.teamName}</Link>
            </td>
            <td>{Math.round(ranking.rating)}</td>
          </tr>
        ))}
      </tbody>
    </table>
  );
}
