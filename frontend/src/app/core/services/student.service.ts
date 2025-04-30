// src/app/core/services/student.service.ts
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { ApiService } from './api.service';
import { Student } from '../models/student.model';

@Injectable({
  providedIn: 'root'
})
export class StudentService {
  constructor(private apiService: ApiService) {}

  getAllStudents(): Observable<Student[]> {
    return this.apiService.get<Student[]>('students');
  }

  getStudentById(id: number): Observable<Student> {
    return this.apiService.get<Student>(`students/${id}`);
  }

  createStudent(student: Partial<Student>): Observable<Student> {
    return this.apiService.post<Student>('students', student);
  }

  updateStudent(id: number, student: Partial<Student>): Observable<Student> {
    return this.apiService.put<Student>(`students/${id}`, student);
  }

  deleteStudent(id: number): Observable<void> {
    return this.apiService.delete<void>(`students/${id}`);
  }
}