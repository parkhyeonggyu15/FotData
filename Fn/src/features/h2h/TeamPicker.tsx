import { useEffect } from "react";
import { useQuery } from "@tanstack/react-query";
import { fetchLeagues, fetchTeamsByLeague } from "../../api/leagues";
import { LoadingState } from "../../components/LoadingState";
import { ErrorState } from "../../components/ErrorState";

interface TeamPickerProps {
  label: string;
  leagueId: number | null;
  onLeagueChange: (leagueId: number) => void;
  teamId: number | null;
  onTeamChange: (teamId: number) => void;
}

export function TeamPicker({ label, leagueId, onLeagueChange, teamId, onTeamChange }: TeamPickerProps) {
  const leaguesQuery = useQuery({ queryKey: ["leagues"], queryFn: fetchLeagues });
  const leagues = leaguesQuery.data ?? [];

  const firstLeagueId = leagues[0]?.id;
  useEffect(() => {
    if (leagueId === null && firstLeagueId !== undefined) {
      onLeagueChange(firstLeagueId);
    }
  }, [leagueId, firstLeagueId, onLeagueChange]);

  const teamsQuery = useQuery({
    queryKey: ["teams", leagueId],
    queryFn: () => fetchTeamsByLeague(leagueId!),
    enabled: leagueId !== null,
  });

  if (leaguesQuery.isLoading) return <LoadingState />;
  if (leaguesQuery.error) return <ErrorState error={leaguesQuery.error} />;

  const teams = teamsQuery.data ?? [];

  return (
    <fieldset>
      <legend>{label}</legend>

      <select value={leagueId ?? ""} onChange={(e) => onLeagueChange(Number(e.target.value))}>
        {leagues.map((league) => (
          <option key={league.id} value={league.id}>
            {league.name}
          </option>
        ))}
      </select>

      <select value={teamId ?? ""} onChange={(e) => onTeamChange(Number(e.target.value))}>
        <option value="">팀 선택</option>
        {teams.map((team) => (
          <option key={team.id} value={team.id}>
            {team.name}
          </option>
        ))}
      </select>
    </fieldset>
  );
}
