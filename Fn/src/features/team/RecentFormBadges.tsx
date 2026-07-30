const RESULT_LABEL: Record<string, string> = {
  W: "승",
  D: "무",
  L: "패",
};

export function RecentFormBadges({ recentForm }: { recentForm: string }) {
  if (!recentForm) return <p>최근 경기 기록이 없어요.</p>;

  return (
    <ul>
      {recentForm.split("").map((result, index) => (
        <li key={index}>{RESULT_LABEL[result] ?? result}</li>
      ))}
    </ul>
  );
}
