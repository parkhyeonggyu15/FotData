const RESULT_LABEL: Record<string, string> = {
  W: "승",
  D: "무",
  L: "패",
};

const RESULT_CLASS: Record<string, string> = {
  W: "result-win",
  D: "result-draw",
  L: "result-lose",
};

export function RecentFormBadges({ recentForm }: { recentForm: string }) {
  if (!recentForm) return <p>최근 경기 기록이 없어요.</p>;

  return (
    <ul className="form-badges">
      {recentForm.split("").map((result, index) => (
        <li key={index} className={`form-badge ${RESULT_CLASS[result] ?? ""}`}>
          {RESULT_LABEL[result] ?? result}
        </li>
      ))}
    </ul>
  );
}
