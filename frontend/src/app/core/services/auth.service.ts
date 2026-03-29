import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

import { environment } from '../../../environments/environment';
import { LoginRequest } from '../models/login-request.model';
import { AuthResponse } from '../models/auth-response.model';
import { SsoRedirectResponse } from '../models/sso-redirect-response.model';

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private readonly authUrl = `${environment.apiUrl}/auth`;

  constructor(private readonly http: HttpClient) {}

  login(request: LoginRequest): Observable<AuthResponse> {
    return this.http.post<AuthResponse>(`${this.authUrl}/login`, request);
  }

  redirectToSso(): void {
    window.location.href = `${this.authUrl}/sso`;
  }

  startSso(): Observable<SsoRedirectResponse> {
  return this.http.get<SsoRedirectResponse>(`${this.authUrl}/sso`);
}

handleSsoCallback(code: string): Observable<AuthResponse> {
  return this.http.get<AuthResponse>(`${this.authUrl}/sso/callback`, {
    params: { code }
  });
}

  saveToken(token: string): void {
    localStorage.setItem('token', token);
  }

  getToken(): string | null {
    return localStorage.getItem('token');
  }

  clearSession(): void {
    localStorage.removeItem('token');
  }

  
}