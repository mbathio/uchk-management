import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { ApiService } from './api.service';
import { Notification } from '../models/notification.model';

@Injectable({
  providedIn: 'root'
})
export class NotificationService {
  private baseUrl = '/notifications';

  constructor(private apiService: ApiService) { }

  getNotificationById(id: number): Observable<Notification> {
    return this.apiService.get<Notification>(`${this.baseUrl}/${id}`);
  }

  getAllNotifications(): Observable<Notification[]> {
    return this.apiService.get<Notification[]>(`${this.baseUrl}`);
  }

  getUnreadNotifications(): Observable<Notification[]> {
    return this.apiService.get<Notification[]>(`${this.baseUrl}/unread`);
  }

  getUnreadCount(): Observable<number> {
    return this.apiService.get<number>(`${this.baseUrl}/unread/count`);
  }

  markAsRead(id: number): Observable<Notification> {
    return this.apiService.put<Notification>(`${this.baseUrl}/${id}/read`, {});
  }

  markAllAsRead(): Observable<void> {
    return this.apiService.put<void>(`${this.baseUrl}/read-all`, {});
  }

  deleteNotification(id: number): Observable<void> {
    return this.apiService.delete<void>(`${this.baseUrl}/${id}`);
  }
}