// src/app/core/models/user.model.ts
export interface User {
  id: number;
  username: string;
  email: string;
  roles: string[];
  active: boolean;
  createdAt: string;
  updatedAt: string;
}