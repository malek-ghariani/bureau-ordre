// src/app/services/departement.service.ts
import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';

export interface DepartementDTO {
  code: string;
  nom: string;
  chefDepartement?: string;
  email?: string;
  telephone?: string;
}

export interface ApiResponse<T = any> {
  success: boolean;
  message: string;
  data?: T;
}

@Injectable({
  providedIn: 'root'
})
export class DepartementService {
  private baseUrl = 'http://localhost:8080/api/departements';

  constructor(private http: HttpClient) { }

  private getHeaders(): HttpHeaders {
    const token = localStorage.getItem('token');
    return new HttpHeaders({
      'Authorization': `Bearer ${token}`,
      'Content-Type': 'application/json'
    });
  }

  // GET /api/departements
  getAll(): Observable<ApiResponse<DepartementDTO[]>> {
    return this.http.get<ApiResponse<DepartementDTO[]>>(this.baseUrl, {
      headers: this.getHeaders()
    });
  }

  // GET /api/departements/{code}
  getById(code: string): Observable<ApiResponse<DepartementDTO>> {
    return this.http.get<ApiResponse<DepartementDTO>>(`${this.baseUrl}/${code}`, {
      headers: this.getHeaders()
    });
  }

  // POST /api/departements
  create(departement: DepartementDTO): Observable<ApiResponse<DepartementDTO>> {
    return this.http.post<ApiResponse<DepartementDTO>>(this.baseUrl, departement, {
      headers: this.getHeaders()
    });
  }

  // PUT /api/departements/{code}
  update(code: string, departement: DepartementDTO): Observable<ApiResponse<DepartementDTO>> {
    return this.http.put<ApiResponse<DepartementDTO>>(`${this.baseUrl}/${code}`, departement, {
      headers: this.getHeaders()
    });
  }

  // DELETE /api/departements/{code}
  delete(code: string): Observable<ApiResponse> {
    return this.http.delete<ApiResponse>(`${this.baseUrl}/${code}`, {
      headers: this.getHeaders()
    });
  }
}