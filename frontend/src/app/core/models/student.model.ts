import { Formation } from "./formation.model";
import { User } from "./user.model";

// src/app/core/models/student.model.ts
export interface Student {
    id: number;
    user: User;
    studentId: string;
    firstName: string;
    lastName: string;
    birthDate: string;
    currentFormation: Formation | null;
    promo: string;
    startYear: number;
    endYear: number;
  }