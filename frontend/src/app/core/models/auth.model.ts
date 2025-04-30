import { User } from "./user.model";

// src/app/core/models/auth.model.ts
export interface LoginRequest {
    username: string;
    password: string;
  }
  
  export interface LoginResponse {
    token: string;
    user: User;
  }