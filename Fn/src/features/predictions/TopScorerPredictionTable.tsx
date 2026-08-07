import { Link } from "react-router-dom";
import { TeamCrest } from "../../components/TeamCrest";
import type { PlayerGoalPredictionResponse } from "../../types/api";

export function TopScorerPredictionTable({ predictions }: { predictions: PlayerGoalPredictionResponse[] }) {
  return (
    <div className="table-wrap">
      <table>
        <thead>
          <tr>
            <th>예상 순위</th>
            <th>선수</th>
            <th>팀</th>
            <th>예상 득점</th>
          </tr>
        </thead>
        <tbody>
          {predictions.map((prediction, index) => (
            <tr key={prediction.playerId}>
              <td>{index + 1}</td>
              <td>{prediction.playerName}</td>
              <td>
                <Link to={`/teams/${prediction.teamId}`} className="team-name">
                  <TeamCrest src={prediction.teamCrestUrl} alt="" />
                  {prediction.teamName}
                </Link>
              </td>
              <td>{prediction.predictedGoals.toFixed(1)}</td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );
}
