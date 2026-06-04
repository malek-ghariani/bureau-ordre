import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Planification } from '../models/planification.model';
import { ApiResponse } from '../models/ApiResponse.model';



@Injectable({ providedIn: 'root' })
export class PlanificationService {
  private api = 'http://localhost:8080/api/planifications';

  constructor(private http: HttpClient) {}

 getAll(): Observable<any[]> {
  return this.http.get<any[]>(this.api);
}

  getById(id: number): Observable<ApiResponse<any>> {
    return this.http.get<ApiResponse<any>>(`${this.api}/${id}`);
  }

  update(id: number, data: any): Observable<ApiResponse<any>> {
    return this.http.put<ApiResponse<any>>(`${this.api}/${id}`, data);
  }

  delete(id: number): Observable<ApiResponse<any>> {
    return this.http.delete<ApiResponse<any>>(`${this.api}/${id}`);
  }

  // ← plus d'id dans l'URL, le token JWT identifie l'agent
 getMesPlanifications(): Observable<any[]> {
  return this.http.get<any[]>(`${this.api}/mes-planifications`);
}

  repondre(id: number, data: { commentaireResultat: string, resultat: string }): Observable<ApiResponse<any>> {
    return this.http.put<ApiResponse<any>>(`${this.api}/${id}/repondre`, data);
  }

  changerStatut(id: number, statut: string): Observable<ApiResponse<any>> {
    return this.http.put<ApiResponse<any>>(`${this.api}/${id}/changer-statut?statut=${statut}`, {});
  }
}