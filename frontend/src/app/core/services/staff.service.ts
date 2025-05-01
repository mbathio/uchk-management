import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { ApiService } from './api.service';
import { Staff } from '../models/staff.model';

@Injectable({
  providedIn: 'root'
})
export class StaffService {
  private baseUrl = '/staff';

  constructor(private apiService: ApiService) { }

  getStaffById(id: number): Observable<Staff> {
    return this.apiService.get<Staff>(`${this.baseUrl}/${id}`);
  }

  getStaffByStaffId(staffId: string): Observable<Staff> {
    return this.apiService.get<Staff>(`${this.baseUrl}/staffId/${staffId}`);
  }

  getAllStaff(): Observable<Staff[]> {
    return this.apiService.get<Staff[]>(`${this.baseUrl}`);
  }

  getStaffByDepartment(department: string): Observable<Staff[]> {
    return this.apiService.get<Staff[]>(`${this.baseUrl}/department/${department}`);
  }

  getStaffByPosition(position: string): Observable<Staff[]> {
    return this.apiService.get<Staff[]>(`${this.baseUrl}/position/${position}`);
  }

  getTrainersByFormation(formationId: number): Observable<Staff[]> {
    return this.apiService.get<Staff[]>(`${this.baseUrl}/formation/${formationId}`);
  }

  createStaff(staff: Staff): Observable<Staff> {
    return this.apiService.post<Staff>(`${this.baseUrl}`, staff);
  }

  updateStaff(id: number, staff: Staff): Observable<Staff> {
    return this.apiService.put<Staff>(`${this.baseUrl}/${id}`, staff);
  }

  deleteStaff(id: number): Observable<void> {
    return this.apiService.delete<void>(`${this.baseUrl}/${id}`);
  }
}