export type Role = 'PLAYER' | 'ORGANIZER' | 'OBSERVER' | 'ADMIN';

export interface UserSummary {
  id: string;
  fullName: string;
  email: string;
  elo: number;
  organizerVerified: boolean;
  observerVerified: boolean;
  organizerLevel: number;
  roles: Role[];
}

export interface AuthResponse extends UserSummary {
  token: string;
}
