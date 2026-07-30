import { Link } from "react-router-dom";
import type { RankingResponse } from "../../types/api";

interface RankingTableProps {
  rankings: RankingResponse[];
  metricLabel: string;
  metric: "goalsFor" | "goalsAgainst";
}

export function RankingTable({ rankings, metricLabel, metric }: RankingTableProps) {
  return (
    <table>
      <thead>
        <tr>
          <th>순위</th>
          <th>팀</th>
          <th>{metricLabel}</th>
        </tr>
      </thead>
      <tbody>
        {rankings.map((ranking, index) => (
          <tr key={ranking.teamId}>
            <td>{index + 1}</td>
            <td>
              <Link to={`/teams/${ranking.teamId}`}>{ranking.teamName}</Link>
            </td>
            <td>{ranking[metric]}</td>
          </tr>
        ))}
      </tbody>
    </table>
  );
}
