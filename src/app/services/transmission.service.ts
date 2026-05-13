import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Transmission } from '../models/transmission.model';
import { ApiResponse } from '../models/ApiResponse.model';

@Injectable({
  providedIn: 'root'
})
export class TransmissionService {

  private api = 'http://localhost:8080/api/transmissions';

  constructor(private http: HttpClient) {}

  envoyer(data: any): Observable<ApiResponse<Transmission>> {
    return this.http.post<ApiResponse<Transmission>>(`${this.api}/envoyer`, data);
  }

  getRecus(): Observable<ApiResponse<Transmission[]>> {
    return this.http.get<ApiResponse<Transmission[]>>(`${this.api}/recus`);
  }

  getEnvoyes(): Observable<ApiResponse<Transmission[]>> {
    return this.http.get<ApiResponse<Transmission[]>>(`${this.api}/envoyes`);
  }

  marquerLu(id: number): Observable<ApiResponse<Transmission>> {
    return this.http.put<ApiResponse<Transmission>>(`${this.api}/${id}/lu`, {});
  }

  repondre(id: number, resultat: string, reponse: string): Observable<ApiResponse<Transmission>> {
    const params = new HttpParams()
      .set('resultat', resultat)
      .set('reponse', reponse);

    return this.http.put<ApiResponse<Transmission>>(`${this.api}/${id}/repondre`, null, { params });
  }

  getByCourrierEntrant(id: number): Observable<ApiResponse<Transmission[]>> {
    return this.http.get<ApiResponse<Transmission[]>>(`${this.api}/by-courrier-entrant/${id}`);
  }

  getByCourrierSortant(id: number): Observable<ApiResponse<Transmission[]>> {
    return this.http.get<ApiResponse<Transmission[]>>(`${this.api}/by-courrier-sortant/${id}`);
  }
}