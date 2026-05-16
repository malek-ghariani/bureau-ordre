import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Planification } from '../models/planification.model';

const API = 'http://localhost:8080/api/planifications';

@Injectable({
  providedIn: 'root'
})
export class PlanificationService {

  constructor(private http: HttpClient) {}

  // GET ALL
  getAll(): Observable<Planification[]> {
    return this.http.get<Planification[]>(API);
  }

  // GET BY ID
  getById(id: number): Observable<Planification> {
    return this.http.get<Planification>(`${API}/${id}`);
  }

  // CREATE
  create(data: Planification): Observable<Planification> {
    return this.http.post<Planification>(API, data);
  }

  // UPDATE
  update(id: number, data: Planification): Observable<Planification> {
    return this.http.put<Planification>(`${API}/${id}`, data);
  }

  // DELETE
  delete(id: number): Observable<void> {
    return this.http.delete<void>(`${API}/${id}`);
  }

  // EMPLOYÉ : récupérer les planifications par destinataire
  getByDestinataire(id: number): Observable<any> {
  return this.http.get(`${API}/employe/${id}`);
}

  // REPONDRE
  repondre(id: number, resultat: string, message: string) {
    const body = { resultat, message };
    return this.http.put(`${API}/${id}/repondre`, body);
  }
  // L'URL pointe vers PieceJointeController, pas PlanificationController
telechargerPieceJointe(id: number): Observable<Blob> {
  return this.http.get(`http://localhost:8080/api/pieces-jointes/download/${id}`, {
    responseType: 'blob',
    headers: { Authorization: `Bearer ${localStorage.getItem('token')}` }
  });
}
}