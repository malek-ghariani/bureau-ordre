// src/app/services/employe.service.ts
import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

export interface EmployeDTO {
  id?: number;
  matricule: string;
  nom: string;
  email: string;
  password: string;
  telephone?: string;
  poste?: string;
  departementCode?: string;
  nomDepartement?: string;
  role?: string;
  enabled?: boolean;
}

export interface ApiResponse<T = any> {
  success: boolean;
  message: string;
  data?: T;
}

@Injectable({
  providedIn: 'root'
})
export class EmployeService {
  private baseUrl = 'http://localhost:8080/api/employes'; // adapte si nécessaire

  constructor(private http: HttpClient) { }

  // GET /api/employes
  getAll(): Observable<ApiResponse<EmployeDTO[]>> {
    return this.http.get<ApiResponse<EmployeDTO[]>>(this.baseUrl);
  }

  // GET /api/employes/{id}
  getById(id: number): Observable<ApiResponse<EmployeDTO>> {
    return this.http.get<ApiResponse<EmployeDTO>>(`${this.baseUrl}/${id}`);
  }

  // POST /api/employes
  create(employe: EmployeDTO): Observable<ApiResponse<EmployeDTO>> {
    return this.http.post<ApiResponse<EmployeDTO>>(this.baseUrl, employe);
  }

  // PUT /api/employes/{id}
  update(id: number, employe: EmployeDTO): Observable<ApiResponse<EmployeDTO>> {
    return this.http.put<ApiResponse<EmployeDTO>>(`${this.baseUrl}/${id}`, employe);
  }

  // DELETE /api/employes/{id}
  delete(id: number): Observable<ApiResponse> {
    return this.http.delete<ApiResponse>(`${this.baseUrl}/${id}`);
  }

  // GET /api/employes/departement/{code}
  getByDepartement(code: string): Observable<ApiResponse<EmployeDTO[]>> {
    return this.http.get<ApiResponse<EmployeDTO[]>>(`${this.baseUrl}/departement/${code}`);
  }
}
