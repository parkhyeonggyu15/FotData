import { ApiError } from "../api/client";

export function ErrorState({ error }: { error: Error }) {
  if (error instanceof ApiError && error.status === 404) {
    return <p role="alert">데이터를 찾을 수 없어요.</p>;
  }
  if (error instanceof ApiError && error.status === 400) {
    return <p role="alert">잘못된 요청이에요.</p>;
  }
  return <p role="alert">문제가 발생했어요. 잠시 후 다시 시도해주세요.</p>;
}
