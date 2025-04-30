// src/app/core/models/formation.model.ts
export interface Formation {
    id: number;
    name: string;
    type: string;
    level: string;
    startDate: string;
    endDate: string;
    description: string;
    fundingAmount: number;
    fundingType: string;
  }