import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders, HttpEvent } from '@angular/common/http';
import { Observable, of, throwError } from 'rxjs';
import { catchError, map } from 'rxjs/operators';
import { PieceJointeDTO } from '../models/piece-jointe.model';
import { ApiResponse } from '../models/ApiResponse.model';

@Injectable({ providedIn: 'root' })
export class DocumentService {
  private baseUrl = 'http://localhost:8080/api';
  private baseEntrant = `${this.baseUrl}/courriers-entrants`;
  private baseSortant = `${this.baseUrl}/courriers-sortants`;
  private basePieceJointe = `${this.baseUrl}/pieces-jointes`;

  constructor(private http: HttpClient) {}

  // --------- COURRIERS ENTRANTS ----------
  getAllEntrants(): Observable<ApiResponse<any[]>> {
    return this.http.get<ApiResponse<any[]>>(this.baseEntrant);
  }

  getEntrantById(id: number): Observable<ApiResponse<any>> {
    return this.http.get<ApiResponse<any>>(`${this.baseEntrant}/${id}`);
  }

  createEntrant(data: any): Observable<ApiResponse<any>> {
    return this.http.post<ApiResponse<any>>(this.baseEntrant, data);
  }

  updateEntrant(id: number, data: any): Observable<ApiResponse<any>> {
    return this.http.put<ApiResponse<any>>(`${this.baseEntrant}/${id}`, data);
  }

  deleteEntrant(id: number): Observable<ApiResponse<any>> {
    return this.http.delete<ApiResponse<any>>(`${this.baseEntrant}/${id}`);
  }

  archiverEntrant(id: number): Observable<ApiResponse<any>> {
    return this.http.put<ApiResponse<any>>(`${this.baseEntrant}/${id}/archiver`, {});
  }

  getArchivesEntrant(): Observable<ApiResponse<any>> {
    return this.http.get<ApiResponse<any>>(`${this.baseEntrant}/archives`);
  }

  changerStatutEntrant(id: number, statut: string): Observable<ApiResponse<any>> {
    return this.http.put<ApiResponse<any>>(`${this.baseEntrant}/${id}/statut?statut=${statut}`, {});
  }

  // --------- COURRIERS SORTANTS ----------
  getAllSortants(): Observable<ApiResponse<any[]>> {
    return this.http.get<ApiResponse<any[]>>(this.baseSortant);
  }

  getSortantById(id: number): Observable<ApiResponse<any>> {
    return this.http.get<ApiResponse<any>>(`${this.baseSortant}/${id}`);
  }

  createSortant(data: any): Observable<ApiResponse<any>> {
    return this.http.post<ApiResponse<any>>(this.baseSortant, data);
  }

  updateSortant(id: number, data: any): Observable<ApiResponse<any>> {
    return this.http.put<ApiResponse<any>>(`${this.baseSortant}/${id}`, data);
  }

  deleteSortant(id: number): Observable<ApiResponse<any>> {
    return this.http.delete<ApiResponse<any>>(`${this.baseSortant}/${id}`);
  }

  archiverSortant(id: number): Observable<ApiResponse<any>> {
    return this.http.put<ApiResponse<any>>(`${this.baseSortant}/${id}/archiver`, {});
  }

  getArchivesSortant(): Observable<ApiResponse<any>> {
    return this.http.get<ApiResponse<any>>(`${this.baseSortant}/archives`);
  }

  changerStatutSortant(id: number, statut: string): Observable<ApiResponse<any>> {
    return this.http.put<ApiResponse<any>>(`${this.baseSortant}/${id}/statut?statut=${statut}`, {});
  }

  // --------- PIÈCES JOINTES ----------
  getPieceJointesEntrant(courrierId: number): Observable<any[]> {
    return this.http.get<ApiResponse<any[]>>(
      `${this.basePieceJointe}/courrier-entrant/${courrierId}`
    ).pipe(map(res => res.data || []));
  }

  getPieceJointesSortant(courrierId: number): Observable<any[]> {
    return this.http.get<ApiResponse<any[]>>(
      `${this.basePieceJointe}/courrier-sortant/${courrierId}`
    ).pipe(map(res => res.data || []));
  }

  uploadPieceJointeEntrant(courrierId: number, files: File[], description = ''): Observable<any> {
    const formData = new FormData();
    files.forEach(f => formData.append('files', f));  // ← files plural
    if (description) formData.append('description', description);
    return this.http.post(`${this.basePieceJointe}/upload/courrier-entrant/${courrierId}`, formData);
  }

  uploadPieceJointeSortant(courrierId: number, files: File[], description = ''): Observable<any> {
    const formData = new FormData();
    files.forEach(f => formData.append('files', f));  // ← files plural
    if (description) formData.append('description', description);
    return this.http.post(`${this.basePieceJointe}/upload/courrier-sortant/${courrierId}`, formData);
  }

  deletePieceJointe(id: number): Observable<any> {
    return this.http.delete(`${this.basePieceJointe}/${id}`);
  }

  downloadPieceJointe(id: number): Observable<Blob> {
    return this.http.get(`${this.basePieceJointe}/download/${id}`, {
      responseType: 'blob'
    });
  }

  openPieceJointe(id: number) {
    this.downloadPieceJointe(id).subscribe(blob => {
      const url = window.URL.createObjectURL(blob);
      window.open(url);
    });
  }
}