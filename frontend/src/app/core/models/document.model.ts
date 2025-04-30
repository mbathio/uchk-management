// src/app/core/models/document.model.ts
export interface Document {
  id: number;
  title: string;
  description: string;
  filePath: string;
  fileName: string;
  type: string;
  visibilityLevel: string;
  creator: User;
  createdAt: string;
  updatedAt: string;
  formations: Formation[];
}
