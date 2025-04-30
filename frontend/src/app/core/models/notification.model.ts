import { User } from "./user.model";

// src/app/core/models/notification.model.ts
export interface Notification {
  id: number;
  user: User;
  message: string;
  type: string;
  read: boolean;
  createdAt: string;
}