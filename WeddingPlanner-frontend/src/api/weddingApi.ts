import { apiFetch, ApiResponse } from './apiClient';

export interface WeddingMemberDto {
  id: number;
  userId: number;
  userName: string;
  userEmail: string;
  role: string;
  side: 'BRIDE_SIDE' | 'GROOM_SIDE' | 'SHARED';
  joinedAt: string;
}

export interface CeremonyDto {
  id: number;
  weddingId: number;
  ceremonyType: 'TRADITIONAL' | 'CIVIL' | 'RELIGIOUS' | 'RECEPTION' | 'GENERAL';
  name: string;
  ceremonyDate?: string;
  startTime?: string;
  venueLocation?: string;
  description?: string;
}

export interface WeddingDto {
  id: number;
  title: string;
  targetBudget?: number;
  partnerCode: string;
  familyCode: string;
  brideId?: number;
  brideName?: string;
  groomId?: number;
  groomName?: string;
  members?: WeddingMemberDto[];
  ceremonies?: CeremonyDto[];
  createdAt: string;
  updatedAt: string;
}

export interface CreateWeddingPayload {
  title: string;
  targetBudget?: number;
}

export interface JoinWeddingPayload {
  joinCode: string;
  role?: string;
}

export interface CreateCeremonyPayload {
  weddingId: number;
  ceremonyType: string;
  name: string;
  ceremonyDate?: string;
  startTime?: string;
  venueLocation?: string;
  description?: string;
}

export const weddingApi = {
  createWedding: (payload: CreateWeddingPayload): Promise<ApiResponse<WeddingDto>> => {
    return apiFetch<WeddingDto>('/weddings', {
      method: 'POST',
      body: JSON.stringify(payload),
    });
  },

  joinWedding: (payload: JoinWeddingPayload): Promise<ApiResponse<WeddingDto>> => {
    return apiFetch<WeddingDto>('/weddings/join', {
      method: 'POST',
      body: JSON.stringify(payload),
    });
  },

  getMyWedding: (): Promise<ApiResponse<WeddingDto>> => {
    return apiFetch<WeddingDto>('/weddings/me');
  },

  getWeddingById: (id: number): Promise<ApiResponse<WeddingDto>> => {
    return apiFetch<WeddingDto>(`/weddings/${id}`);
  },

  getCeremonies: (weddingId?: number): Promise<ApiResponse<CeremonyDto[]>> => {
    const query = weddingId ? `?weddingId=${weddingId}` : '';
    return apiFetch<CeremonyDto[]>(`/ceremonies${query}`);
  },

  createCeremony: (payload: CreateCeremonyPayload): Promise<ApiResponse<CeremonyDto>> => {
    return apiFetch<CeremonyDto>('/ceremonies', {
      method: 'POST',
      body: JSON.stringify(payload),
    });
  },
};
