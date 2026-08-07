import { Link } from "react-router-dom";
import { TeamCrest } from "../../components/TeamCrest";
import type { SeasonPredictionResponse } from "../../types/api";

function toPercent(value: number) {
  return `${Math.round(value * 100)}%`;
}

export function SeasonPredictionTable({ predictions }: { predictions: SeasonPredictionResponse[] }) {
  return (
    <div className="table-wrap">
      <table>
        <thead>
          <tr>
            <th>예상 순위</th>
            <th>팀</th>
            <th>평균 순위</th>
            <th>우승 확률</th>
            <th>강등 확률</th>
          </tr>
        </thead>
        <tbody>
          {predictions.map((prediction, index) => (
            <tr key={prediction.teamId}>
              <td>{index + 1}</td>
              <td>
                <Link to={`/teams/${prediction.teamId}`} className="team-name">
                  <TeamCrest src={prediction.teamCrestUrl} alt="" />
                  {prediction.teamName}
                </Link>
              </td>
              <td>{prediction.averageRank.toFixed(1)}</td>
              <td>{toPercent(prediction.titleProbability)}</td>
              <td>{toPercent(prediction.relegationProbability)}</td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );
}
