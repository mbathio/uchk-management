import { Formation } from "./formation.model";
import { User } from "./user.model";

// src/app/core/models/staff.model.ts
export interface Staff {
    id: number;
    user: User;
    staffId: string;
    firstName: string;
    lastName: string;
    position: string;
    department: string;
    contactInfo: string;
    formations: Formation[];
  }