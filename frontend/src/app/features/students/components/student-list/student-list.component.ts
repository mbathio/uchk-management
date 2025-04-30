// src/app/features/students/components/student-list/student-list.component.ts
import { Component, OnInit } from '@angular/core';
import { Observable } from 'rxjs';
import { Student } from '../../../../core/models/student.model';
import { StudentService } from '../../../../core/services/student.service';

@Component({
  selector: 'app-student-list',
  templateUrl: './student-list.component.html',
  styleUrls: ['./student-list.component.scss']
})
export class StudentListComponent implements OnInit {
  students$!: Observable<Student[]>;
  displayedColumns: string[] = ['studentId', 'firstName', 'lastName', 'promo', 'currentFormation', 'actions'];

  constructor(private studentService: StudentService) {}

  ngOnInit(): void {
    this.loadStudents();
  }

  loadStudents(): void {
    this.students$ = this.studentService.getAllStudents();
  }

  deleteStudent(id: number): void {
    if (confirm('Êtes-vous sûr de vouloir supprimer cet étudiant ?')) {
      this.studentService.deleteStudent(id).subscribe(() => {
        this.loadStudents();
      });
    }
  }
}