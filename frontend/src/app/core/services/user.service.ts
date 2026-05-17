import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Role, UserSummary } from '../models/user.model';

const API_URL = 'http://localhost:8080/api';

export interface RoleApplication {
  id: string;
  applicant: UserSummary;
  requestedRole: Role;
  status: 'PENDING' | 'APPROVED' | 'REJECTED';
  motivation: string;
  createdAt: string;
}

@Injectable({ providedIn: 'root' })
export class UserService {
  constructor(private http: HttpClient) {}

  leaderboard() {
    return this.http.get<UserSummary[]>(`${API_URL}/leaderboard`);
  }

  applyForRole(payload: { requestedRole: Role; motivation: string }) {
    return this.http.post<RoleApplication>(`${API_URL}/role-applications`, payload);
  }

  pendingApplications() {
    return this.http.get<RoleApplication[]>(`${API_URL}/admin/role-applications`);
  }

  approve(id: string) {
    return this.http.patch<RoleApplication>(`${API_URL}/admin/role-applications/${id}/approve`, {});
  }

  reject(id: string) {
    return this.http.patch<RoleApplication>(`${API_URL}/admin/role-applications/${id}/reject`, {});
  }
}
