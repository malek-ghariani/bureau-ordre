// src/app/services/auth.service.ts
import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { tap } from 'rxjs/operators';

interface LoginResponse {
  token: string;
  type: string;
  email: string;
  role: string;
  nom: string;
  matricule: string;
  id?: number;
}

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  private apiURL = 'http://localhost:8080/api/auth/login';

  constructor(private http: HttpClient) {}

  // 🔹 Login et stockage token + infos utilisateur
  login(credentials: { email: string; password: string }): Observable<LoginResponse> {
    return this.http.post<LoginResponse>(this.apiURL, credentials).pipe(
      tap(res => {
        if (res.token) {
          this.saveToken(res.token);
          this.saveRole(res.role);
          this.saveEmail(res.email);
          this.saveNom(res.nom);
          this.saveMatricule(res.matricule);
        }
      })
    );
  }

  // ---------------------------
  // 🔐 Gestion Token
  // ---------------------------

  saveToken(token: string) {
    localStorage.setItem('token', token);
  }

  getToken(): string | null {
    return localStorage.getItem('token');
  }

  // ---------------------------
  // 👤 Gestion Profil
  // ---------------------------

  saveRole(role: string) {
    localStorage.setItem('role', role);
  }
  getRole(): string | null {
    return localStorage.getItem('role');
  }

  saveEmail(email: string) {
    localStorage.setItem('email', email);
  }
  getEmail(): string | null {
    return localStorage.getItem('email');
  }

  saveNom(nom: string) {
    localStorage.setItem('nom', nom);
  }
  getNom(): string | null {
    return localStorage.getItem('nom');
  }

  saveMatricule(matricule: string) {
    localStorage.setItem('matricule', matricule);
  }
  getMatricule(): string | null {
    return localStorage.getItem('matricule');
  }

  // ---------------------------
  // 🔓 Logout
  // ---------------------------

  logout() {
    localStorage.removeItem('token');
    localStorage.removeItem('role');
    localStorage.removeItem('email');
    localStorage.removeItem('nom');
    localStorage.removeItem('matricule');
  }

  // ---------------------------
  // 🛡️ Vérifications
  // ---------------------------

  isLoggedIn(): boolean {
    return !!this.getToken();
  }

  hasRole(role: string): boolean {
    return this.getRole() === role;
  }

  isAdmin(): boolean {
    return this.hasRole('ADMIN');
  }

  saveId(id: number) {
  localStorage.setItem('id', id.toString());
}

getId(): number {
  return Number(localStorage.getItem('id'));
}
}
