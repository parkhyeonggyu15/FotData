import type { MatchPredictionResponse } from "../../types/api";

function toPercent(value: number) {
  return `${Math.round(value * 100)}%`;
}

export function MatchPredictionResult({ prediction }: { prediction: MatchPredictionResponse }) {
  return (
    <dl>
      <dt>홈 승</dt>
      <dd>{toPercent(prediction.homeWinProbability)}</dd>
      <dt>무승부</dt>
      <dd>{toPercent(prediction.drawProbability)}</dd>
      <dt>원정 승</dt>
      <dd>{toPercent(prediction.awayWinProbability)}</dd>
    </dl>
  );
}
