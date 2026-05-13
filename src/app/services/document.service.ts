import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders, HttpEvent } from '@angular/common/http';
import { Observable, of, throwError } from 'rxjs';
import { catchError, map } from 'rxjs/operators';
import { PieceJointeDTO } from '../models/piece-jointe.model';

@Injectable({
  providedIn: 'root'
})
export class DocumentService {
  

  private baseUrl = 'http://localhost:8080/api';
  private baseEntrant = `${this.baseUrl}/courriers-entrants`;
  private baseSortant = `${this.baseUrl}/courriers-sortants`;
  private basePieceJointe = `${this.baseUrl}/pieces-jointes`;

  constructor(private http: HttpClient) {}

  
  getMesCourriersEntrants(): Observable<any> {
    return this.http.get(`${this.baseEntrant}/mes-courriers`, {
      headers: this.getAuthHeaders()
    }).pipe(catchError(this.handleError));
  }

  getEntrantById(id: number): Observable<any> {
    return this.http.get(`${this.baseEntrant}/${id}`, {
      headers: this.getAuthHeaders()
    }).pipe(catchError(this.handleError));
  }

  createEntrant(data: any): Observable<any> {
    return this.http.post(`${this.baseEntrant}`, data, {
      headers: this.getAuthHeaders()
    }).pipe(catchError(this.handleError));
  }

  updateEntrant(id: number, data: any): Observable<any> {
    return this.http.put(`${this.baseEntrant}/${id}`, data, {
      headers: this.getAuthHeaders()
    }).pipe(catchError(this.handleError));
  }

  deleteEntrant(id: number): Observable<any> {
    return this.http.delete(`${this.baseEntrant}/${id}`, {
      headers: this.getAuthHeaders()
    }).pipe(catchError(this.handleError));
  }

  // --------- COURRIERS SORTANTS ----------
  getMesCourriersSortants(): Observable<any> {
    return this.http.get(`${this.baseSortant}/mes-courriers`, {
      headers: this.getAuthHeaders()
    }).pipe(catchError(this.handleError));
  }

  getSortantById(id: number): Observable<any> {
    return this.http.get(`${this.baseSortant}/${id}`, {
      headers: this.getAuthHeaders()
    }).pipe(catchError(this.handleError));
  }

  createSortant(data: any): Observable<any> {
    return this.http.post(`${this.baseSortant}`, data, {
      headers: this.getAuthHeaders()
    }).pipe(catchError(this.handleError));
  }

  updateSortant(id: number, data: any): Observable<any> {
    return this.http.put(`${this.baseSortant}/${id}`, data, {
      headers: this.getAuthHeaders()
    }).pipe(catchError(this.handleError));
  }

  deleteSortant(id: number): Observable<any> {
    return this.http.delete(`${this.baseSortant}/${id}`, {
      headers: this.getAuthHeaders()
    }).pipe(catchError(this.handleError));
  }

  // --------- PIÈCES JOINTES ----------
  // ------------------- COURRIER ENTRANT -------------------
getPieceJointesEntrant(courrierId: number): Observable<PieceJointeDTO[]> {
  return this.http.get<any>(`${this.basePieceJointe}/courrier-entrant/${courrierId}`, {
    headers: this.getAuthHeaders()
  }).pipe(
    map(res => res.data || []), // <-- on prend data
    catchError(this.handleError)
  );
}

uploadPieceJointeEntrant(courrierId: number, file: File, description = ''): Observable<HttpEvent<any>> {
  const formData = new FormData();
  formData.append('file', file);
  if (description) formData.append('description', description);

  const headers = this.getAuthHeaders();
  headers.delete('Content-Type'); // 🔥 important

  return this.http.post(
    `${this.basePieceJointe}/upload/courrier-entrant/${courrierId}`,
    formData,
    {
      reportProgress: true,
      observe: 'events',
      headers
    }
  ).pipe(catchError(this.handleError));
}


// ------------------- COURRIER SORTANT -------------------
getPieceJointesSortant(courrierId: number): Observable<PieceJointeDTO[]> {
  return this.http.get<any>(`${this.basePieceJointe}/courrier-sortant/${courrierId}`, {
    headers: this.getAuthHeaders()
  }).pipe(
    map(res => res.data || []),
    catchError(this.handleError)
  );
}

uploadPieceJointeSortant(
  courrierId: number,
  file: File,
  description = ''
): Observable<HttpEvent<any>> {

  const formData = new FormData();
  formData.append('file', file);

  if (description) {
    formData.append('description', description);
  }

  // 🔥 IMPORTANT: ne pas forcer Content-Type
  const headers = this.getAuthHeaders();
  headers.delete('Content-Type');

  return this.http.post(
    `${this.basePieceJointe}/upload/courrier-sortant/${courrierId}`,
    formData,
    {
      reportProgress: true,
      observe: 'events',
      headers
    }
  ).pipe(
    catchError(this.handleError)
  );
}


// ------------------- GET BY ID -------------------
getPieceJointeById(id: number): Observable<any> {
  return this.http.get(`${this.basePieceJointe}/${id}`, {
    headers: this.getAuthHeaders()
  }).pipe(catchError(this.handleError));
}


// ------------------- DELETE -------------------
deletePieceJointe(id: number): Observable<any> {
  return this.http.delete(`${this.basePieceJointe}/${id}`, {
    headers: this.getAuthHeaders()
  }).pipe(catchError(this.handleError));
}


// ------------------- DOWNLOAD -------------------
downloadPieceJointe(id: number) {
  return this.http.get(
    `${this.basePieceJointe}/download/${id}`,
    {
      responseType: 'blob',
      headers: {
        Authorization: `Bearer ${localStorage.getItem('token')}`
      }
    }
  );
}

openPieceJointe(piece: PieceJointeDTO) {
  this.downloadPieceJointe(piece.id).subscribe(blob => {
    const url = window.URL.createObjectURL(blob);
    window.open(url); // ouvre dans un nouvel onglet
  }, err => {
    console.error("Erreur téléchargement fichier", err);
  });
}

  // --------- UTILITAIRES ----------
  private getAuthHeaders(): HttpHeaders {
    const token = localStorage.getItem('token') || '';
    return new HttpHeaders({ 'Authorization': `Bearer ${token}` });
  }

  private handleError(error: any) {
  console.error('Erreur API:', error);
  return throwError(() => error);
}

  // --------- ASSIGNATION DU COURRIER ----------
assignerCourrier(courrierId: number, employeId: number): Observable<any> {
  const url = `${this.baseEntrant}/${courrierId}/assigner/${employeId}`;
  return this.http.put(url, {}, { headers: this.getAuthHeaders() })
    .pipe(catchError(this.handleError));
}
envoyerCourrier(courrier: any): Observable<any> {
  const url = `${this.baseEntrant}`;
  return this.http.post(url, courrier, { headers: this.getAuthHeaders() })
    .pipe(catchError(this.handleError));
}
getEmployes(): Observable<{ success: boolean; message: string; data: any[] }> {
  const url = `${this.baseUrl}/employes`;
  return this.http.get<{ success: boolean; message: string; data: any[] }>(url, { headers: this.getAuthHeaders() })
    .pipe(catchError(this.handleError));
}


getMesCourriers() {
  return this.http.get<any[]>('http://localhost:8080/api/courriers/mes-courriers');
}
}
