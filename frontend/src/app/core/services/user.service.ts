import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { ApiService } from './api.service';
import { User } from '../models/user.model';

@Injectable({
  providedIn: 'root'
})
export class UserService {
  private baseUrl = '/users';

  constructor(private apiService: ApiService) { }

  getUserById(id: number): Observable<User> {
    return this.apiService.get<User>(`${this.baseUrl}/${id}`);
  }

  getUserByUsername(username: string): Observable<User> {
    return this.apiService.get<User>(`${this.baseUrl}/username/${username}`);
  }

  getAllUsers(): Observable<User[]> {
    return this.apiService.get<User[]>(`${this.baseUrl}`);
  }

  getUsersByRole(role: string): Observable<User[]> {
    return this.apiService.get<User[]>(`${this.baseUrl}/role/${role}`);
  }

  createUser(user: any): Observable<User> {
    return this.apiService.post<User>(`${this.baseUrl}`, user);
  }

  updateUser(id: number, user: any): Observable<User> {
    return this.apiService.put<User>(`${this.baseUrl}/${id}`, user);
  }

  deleteUser(id: number): Observable<void> {
    return this.apiService.delete<void>(`${this.baseUrl}/${id}`);
  }
}