import { UserSummary } from './user.model';

export type MatchType = 'CASUAL' | 'RANKED';
export type MatchFormat = 'FIVE_V_FIVE' | 'SEVEN_V_SEVEN' | 'ELEVEN_V_ELEVEN';
export type MatchStatus = 'WAITING' | 'TEAMS_GENERATED' | 'READY' | 'ONGOING' | 'FINISHED';
export type TeamCode = 'A' | 'B';

export interface Participation {
  id: string;
  player: UserSummary;
  teamCode: TeamCode | null;
  goals: number;
  assists: number;
  observerRating: number | null;
}

export interface Match {
  id: string;
  title: string;
  location: string;
  scheduledAt: string;
  type: MatchType;
  format: MatchFormat;
  status: MatchStatus;
  organizer: UserSummary;
  observer: UserSummary | null;
  teamAScore: number | null;
  teamBScore: number | null;
  validated: boolean;
  capacity: number;
  participants: Participation[];
}

export interface CreateMatchPayload {
  title: string;
  location: string;
  scheduledAt: string;
  type: MatchType;
  format: MatchFormat;
  observerId?: string | null;
}
