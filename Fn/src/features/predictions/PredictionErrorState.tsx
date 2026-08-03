import { ApiError } from "../../api/client";
import { ErrorState } from "../../components/ErrorState";

export function PredictionErrorState({ error }: { error: Error }) {
  if (error instanceof ApiError && error.status === 404) {
    return <p>토너먼트 방식 대회는 총 경기 수가 팀마다 달라 이 예측을 제공하지 않아요.</p>;
  }
  return <ErrorState error={error} />;
}
