import type { LeagueResponse } from "../types/api";

interface LeagueTabsProps {
  leagues: LeagueResponse[];
  selectedLeagueId: number | null;
  onSelect: (leagueId: number) => void;
}

export function LeagueTabs({ leagues, selectedLeagueId, onSelect }: LeagueTabsProps) {
  return (
    <div className="league-tabs">
      {leagues.map((league) => (
        <button
          key={league.id}
          type="button"
          className={league.id === selectedLeagueId ? "league-tab active" : "league-tab"}
          onClick={() => onSelect(league.id)}
        >
          {league.name}
        </button>
      ))}
    </div>
  );
}
