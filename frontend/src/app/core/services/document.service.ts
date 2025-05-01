import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { ApiService } from './api.service';
import { Document } from '../models/document.model';

@Injectable({
  providedIn: 'root'
})
export class DocumentService {
  private baseUrl = '/documents';

  constructor(private apiService: ApiService) { }

  getDocumentById(id: number): Observable<Document> {
    return this.apiService.get<Document>(`${this.baseUrl}/${id}`);
  }

  getAllDocuments(): Observable<Document[]> {
    return this.apiService.get<Document[]>(`${this.baseUrl}`);
  }

  getDocumentsByType(type: string): Observable<Document[]> {
    return this.apiService.get<Document[]>(`${this.baseUrl}/type/${type}`);
  }

  getDocumentTypes(): Observable<string[]> {
    return this.apiService.get<string[]>(`${this.baseUrl}/types`);
  }

  uploadDocument(formData: FormData): Observable<Document> {
    return this.apiService.post<Document>(`${this.baseUrl}/upload`, formData);
  }

  updateDocument(id: number, document: Document): Observable<Document> {
    return this.apiService.put<Document>(`${this.baseUrl}/${id}`, document);
  }

  deleteDocument(id: number): Observable<void> {
    return this.apiService.delete<void>(`${this.baseUrl}/${id}`);
  }
}