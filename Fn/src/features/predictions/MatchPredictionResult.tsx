import type { MatchPredictionResponse } from "../../types/api";

function toPercent(value: number) {
  return `${Math.round(value * 100)}%`;
}

export function MatchPredictionResult({ prediction }: { prediction: MatchPredictionResponse }) {
  return (
    <dl className="prob-grid">
      <div className="prob-home">
        <dt>홈 승</dt>
        <dd>{toPercent(prediction.homeWinProbability)}</dd>
      </div>
      <div className="prob-draw">
        <dt>무승부</dt>
        <dd>{toPercent(prediction.drawProbability)}</dd>
      </div>
      <div className="prob-away">
        <dt>원정 승</dt>
        <dd>{toPercent(prediction.awayWinProbability)}</dd>
      </div>
    </dl>
  );
}
