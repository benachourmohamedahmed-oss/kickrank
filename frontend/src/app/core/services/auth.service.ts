import { Injectable, computed, signal } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Router } from '@angular/router';
import { tap } from 'rxjs';
import { AuthResponse } from '../models/user.model';

const API_URL = 'http://localhost:8080/api';
const STORAGE_KEY = 'kickrank.session';

@Injectable({ providedIn: 'root' })
export class AuthService {
  private readonly session = signal<AuthResponse | null>(this.readSession());
  readonly user = computed(() => this.session());
  readonly isAuthenticated = computed(() => !!this.session()?.token);

  constructor(private http: HttpClient, private router: Router) {}

  login(payload: { email: string; password: string }) {
    return this.http.post<AuthResponse>(`${API_URL}/auth/login`, payload).pipe(tap((response) => this.setSession(response)));
  }

  register(payload: { fullName: string; email: string; password: string }) {
    return this.http.post<AuthResponse>(`${API_URL}/auth/register`, payload).pipe(tap((response) => this.setSession(response)));
  }

  logout(): void {
    localStorage.removeItem(STORAGE_KEY);
    this.session.set(null);
    void this.router.navigateByUrl('/login');
  }

  token(): string | null {
    return this.session()?.token ?? null;
  }

  hasRole(role: string): boolean {
    return this.session()?.roles.includes(role as never) ?? false;
  }

  private setSession(response: AuthResponse): void {
    localStorage.setItem(STORAGE_KEY, JSON.stringify(response));
    this.session.set(response);
  }

  private readSession(): AuthResponse | null {
    const raw = localStorage.getItem(STORAGE_KEY);
    return raw ? JSON.parse(raw) as AuthResponse : null;
  }
}
