import { Link } from "react-router-dom";
import { TeamCrest } from "../../components/TeamCrest";
import type { EloResponse } from "../../types/api";

export function EloRankingTable({ rankings }: { rankings: EloResponse[] }) {
  return (
    <div className="table-wrap">
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
                <Link to={`/teams/${ranking.teamId}`} className="team-name">
                  <TeamCrest src={ranking.teamCrestUrl} alt="" />
                  {ranking.teamName}
                </Link>
              </td>
              <td>{Math.round(ranking.rating)}</td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );
}
