import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Transmission } from '../models/transmission.model';
import { ApiResponse } from '../models/ApiResponse.model';

@Injectable({ providedIn: 'root' })
export class TransmissionService {
  private api = 'http://localhost:8080/api/transmissions';

  constructor(private http: HttpClient) {}

  envoyer(data: any): Observable<ApiResponse<any>> {
    return this.http.post<ApiResponse<any>>(`${this.api}/envoyer`, data);
  }

  getRecus(): Observable<ApiResponse<any[]>> {
    return this.http.get<ApiResponse<any[]>>(`${this.api}/recus`);
  }

  marquerLu(id: number): Observable<ApiResponse<any>> {
    return this.http.put<ApiResponse<any>>(`${this.api}/${id}/lu`, {});
  }

  getByCourrierEntrant(id: number): Observable<ApiResponse<any[]>> {
    return this.http.get<ApiResponse<any[]>>(`${this.api}/by-courrier-entrant/${id}`);
  }

  getByCourrierSortant(id: number): Observable<ApiResponse<any[]>> {
    return this.http.get<ApiResponse<any[]>>(`${this.api}/by-courrier-sortant/${id}`);
  }
}