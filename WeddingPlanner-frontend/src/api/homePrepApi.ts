import { apiFetch, ApiResponse } from './apiClient';
import { HomeItem } from '../types/wedding';

export interface HomePrepDto {
  id: number;
  weddingId: number;
  category: 'APPLIANCES' | 'FURNITURE' | 'KITCHENWARE' | 'BEDDING' | 'RENT_UTILITIES';
  itemName: string;
  side: 'BRIDE_SIDE' | 'GROOM_SIDE' | 'SHARED';
  assignedUserId?: number;
  assignedUserName?: string;
  budgetRwf: number;
  dueDate?: string;
  isCompleted: boolean;
  notes?: string;
  createdAt: string;
  updatedAt: string;
}

export interface CreateHomePrepPayload {
  weddingId?: number;
  category: 'APPLIANCES' | 'FURNITURE' | 'KITCHENWARE' | 'BEDDING' | 'RENT_UTILITIES';
  itemName: string;
  side?: 'BRIDE_SIDE' | 'GROOM_SIDE' | 'SHARED';
  assignedUserId?: number;
  budgetRwf?: number;
  dueDate?: string;
  isCompleted?: boolean;
  notes?: string;
}

export interface UpdateHomePrepPayload {
  category?: 'APPLIANCES' | 'FURNITURE' | 'KITCHENWARE' | 'BEDDING' | 'RENT_UTILITIES';
  itemName?: string;
  side?: 'BRIDE_SIDE' | 'GROOM_SIDE' | 'SHARED';
  assignedUserId?: number;
  budgetRwf?: number;
  dueDate?: string;
  isCompleted?: boolean;
  notes?: string;
}

export function mapDtoToHomeItem(dto: HomePrepDto): HomeItem {
  const side: 'bride' | 'groom' = dto.side === 'BRIDE_SIDE' ? 'bride' : 'groom';
  const categoryMap: Record<string, string> = {
    APPLIANCES: 'Appliances',
    FURNITURE: 'Furniture',
    KITCHENWARE: 'Kitchenware',
    BEDDING: 'Bedding',
    RENT_UTILITIES: 'Rent & Utilities',
  };
  return {
    id: String(dto.id),
    name: dto.itemName,
    category: categoryMap[dto.category] || dto.category,
    side,
    quantity: 1,
    budget: dto.budgetRwf || 0,
    deadline: dto.dueDate || '2027-08-01',
    assignee: side,
    completed: dto.isCompleted,
    notes: dto.notes || '',
  };
}

export function mapCategoryToBackend(cat: string): 'APPLIANCES' | 'FURNITURE' | 'KITCHENWARE' | 'BEDDING' | 'RENT_UTILITIES' {
  const upper = cat.toUpperCase();
  if (upper.includes('APPLIANCE')) return 'APPLIANCES';
  if (upper.includes('FURNITURE')) return 'FURNITURE';
  if (upper.includes('KITCHEN')) return 'KITCHENWARE';
  if (upper.includes('BED')) return 'BEDDING';
  if (upper.includes('RENT') || upper.includes('UTILITY')) return 'RENT_UTILITIES';
  return 'APPLIANCES';
}

export const homePrepApi = {
  getHomePreps: (): Promise<ApiResponse<HomePrepDto[]>> => {
    return apiFetch<HomePrepDto[]>('/home-prep');
  },

  getHomePrepById: (id: number): Promise<ApiResponse<HomePrepDto>> => {
    return apiFetch<HomePrepDto>(`/home-prep/${id}`);
  },

  createHomePrep: (payload: CreateHomePrepPayload): Promise<ApiResponse<HomePrepDto>> => {
    return apiFetch<HomePrepDto>('/home-prep', {
      method: 'POST',
      body: JSON.stringify(payload),
    });
  },

  updateHomePrep: (id: number, payload: UpdateHomePrepPayload): Promise<ApiResponse<HomePrepDto>> => {
    return apiFetch<HomePrepDto>(`/home-prep/${id}`, {
      method: 'PUT',
      body: JSON.stringify(payload),
    });
  },

  deleteHomePrep: (id: number): Promise<ApiResponse<void>> => {
    return apiFetch<void>(`/home-prep/${id}`, {
      method: 'DELETE',
    });
  },
};
