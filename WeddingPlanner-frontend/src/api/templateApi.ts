import { apiFetch, ApiResponse } from './apiClient';
import { HomePrepDto } from './homePrepApi';

export interface TemplateItemDto {
  id: number;
  title: string;
  itemType: string;
  defaultSide: 'BRIDE_SIDE' | 'GROOM_SIDE' | 'SHARED';
  estimatedBudget: number;
}

export interface TemplateDto {
  id: number;
  name: string;
  category: 'WEDDING_TASK' | 'HOME_PREP';
  description: string;
  isActive: boolean;
  items: TemplateItemDto[];
}

export interface ApplyTemplatePayload {
  weddingId?: number;
  side?: 'BRIDE_SIDE' | 'GROOM_SIDE' | 'SHARED';
}

export const templateApi = {
  getAllTemplates: (): Promise<ApiResponse<TemplateDto[]>> => {
    return apiFetch<TemplateDto[]>('/templates');
  },

  getTemplateById: (id: number): Promise<ApiResponse<TemplateDto>> => {
    return apiFetch<TemplateDto>(`/templates/${id}`);
  },

  applyTemplate: (id: number, payload?: ApplyTemplatePayload): Promise<ApiResponse<HomePrepDto[]>> => {
    return apiFetch<HomePrepDto[]>(`/templates/${id}/apply`, {
      method: 'POST',
      body: payload ? JSON.stringify(payload) : undefined,
    });
  },
};
