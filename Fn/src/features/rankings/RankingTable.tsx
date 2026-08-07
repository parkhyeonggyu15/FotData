import { Link } from "react-router-dom";
import { TeamCrest } from "../../components/TeamCrest";
import type { RankingResponse } from "../../types/api";

interface RankingTableProps {
  rankings: RankingResponse[];
  metricLabel: string;
  metric: "goalsFor" | "goalsAgainst";
  highlightTopN?: number;
}

export function RankingTable({ rankings, metricLabel, metric, highlightTopN }: RankingTableProps) {
  return (
    <div className="table-wrap">
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
            <tr key={ranking.teamId} className={highlightTopN && index < highlightTopN ? "qualifies-ucl" : undefined}>
              <td>{index + 1}</td>
              <td>
                <Link to={`/teams/${ranking.teamId}`} className="team-name">
                  <TeamCrest src={ranking.teamCrestUrl} alt="" />
                  {ranking.teamName}
                </Link>
              </td>
              <td>{ranking[metric]}</td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );
}
