import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { CreateMatchPayload, Match } from '../models/match.model';

const API_URL = 'http://localhost:8080/api';

@Injectable({ providedIn: 'root' })
export class MatchService {
  constructor(private http: HttpClient) {}

  findAll() {
    return this.http.get<Match[]>(`${API_URL}/matches`);
  }

  create(payload: CreateMatchPayload) {
    return this.http.post<Match>(`${API_URL}/matches`, payload);
  }

  join(matchId: string) {
    return this.http.post<Match>(`${API_URL}/matches/${matchId}/join`, {});
  }

  generateTeams(matchId: string) {
    return this.http.post<Match>(`${API_URL}/matches/${matchId}/generate-teams`, {});
  }

  validate(matchId: string, payload: unknown) {
    return this.http.post<Match>(`${API_URL}/matches/${matchId}/validate`, payload);
  }
}
