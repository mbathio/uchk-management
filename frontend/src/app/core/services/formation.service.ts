import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { ApiService } from './api.service';
import { Formation } from '../models/formation.model';

@Injectable({
  providedIn: 'root'
})
export class FormationService {
  private baseUrl = '/formations';

  constructor(private apiService: ApiService) { }

  getFormationById(id: number): Observable<Formation> {
    return this.apiService.get<Formation>(`${this.baseUrl}/${id}`);
  }

  getAllFormations(): Observable<Formation[]> {
    return this.apiService.get<Formation[]>(`${this.baseUrl}`);
  }

  getFormationsByType(type: string): Observable<Formation[]> {
    return this.apiService.get<Formation[]>(`${this.baseUrl}/type/${type}`);
  }

  getFormationsByLevel(level: string): Observable<Formation[]> {
    return this.apiService.get<Formation[]>(`${this.baseUrl}/level/${level}`);
  }

  createFormation(formation: Formation): Observable<Formation> {
    return this.apiService.post<Formation>(`${this.baseUrl}`, formation);
  }

  updateFormation(id: number, formation: Formation): Observable<Formation> {
    return this.apiService.put<Formation>(`${this.baseUrl}/${id}`, formation);
  }

  deleteFormation(id: number): Observable<void> {
    return this.apiService.delete<void>(`${this.baseUrl}/${id}`);
  }
}