import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { ApiService } from '../api.service';
import { Student } from '../models/student.model';

@Injectable({
  providedIn: 'root'
})
export class StudentService {
  private baseUrl = '/students';

  constructor(private http: HttpClient, private apiService: ApiService) { }

  getStudentById(id: number): Observable<Student> {
    return this.apiService.get<Student>(`${this.baseUrl}/${id}`);
  }

  getStudentByStudentId(studentId: string): Observable<Student> {
    return this.apiService.get<Student>(`${this.baseUrl}/studentId/${studentId}`);
  }

  getCurrentStudent(): Observable<Student> {
    return this.apiService.get<Student>(`${this.baseUrl}/me`);
  }

  getAllStudents(): Observable<Student[]> {
    return this.apiService.get<Student[]>(`${this.baseUrl}`);
  }

  getStudentsByFormation(formationId: number): Observable<Student[]> {
    return this.apiService.get<Student[]>(`${this.baseUrl}/formation/${formationId}`);
  }

  getStudentsByPromo(promo: string): Observable<Student[]> {
    return this.apiService.get<Student[]>(`${this.baseUrl}/promo/${promo}`);
  }

  createStudent(student: Student): Observable<Student> {
    return this.apiService.post<Student>(`${this.baseUrl}`, student);
  }

  updateStudent(id: number, student: Student): Observable<Student> {
    return this.apiService.put<Student>(`${this.baseUrl}/${id}`, student);
  }

  deleteStudent(id: number): Observable<void> {
    return this.apiService.delete<void>(`${this.baseUrl}/${id}`);
  }
}