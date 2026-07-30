const SEASON_START_MONTH = 8;

export function currentSeason(date = new Date()): string {
  const month = date.getMonth() + 1;
  const startYear = month >= SEASON_START_MONTH ? date.getFullYear() : date.getFullYear() - 1;
  return `${startYear}-${startYear + 1}`;
}
