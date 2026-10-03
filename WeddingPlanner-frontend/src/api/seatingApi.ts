import { apiFetch, ApiResponse } from './apiClient';

export interface GuestSeatingDto {
  seatingId: number;
  tableId: number;
  tableName: string;
  guestId: number;
  guestName: string;
  guestPhone?: string;
  side: 'BRIDE_SIDE' | 'GROOM_SIDE' | 'SHARED';
  category: string;
  status: string;
  plusOneAllowed: number;
  totalSeatsOccupied: number;
  assignedAt: string;
}

export interface SeatingTableDto {
  id: number;
  weddingId: number;
  ceremonyId?: number;
  ceremonyName?: string;
  tableName: string;
  capacity: number;
  occupiedSeats: number;
  isOverflowing: boolean;
  seatedGuests: GuestSeatingDto[];
  createdAt: string;
  updatedAt: string;
}

export interface UnassignedGuestDto {
  guestId: number;
  fullName: string;
  phone?: string;
  email?: string;
  side: 'BRIDE_SIDE' | 'GROOM_SIDE' | 'SHARED';
  category: string;
  status: string;
  plusOneAllowed: number;
  requiredSeats: number;
}

export interface SeatingOverviewDto {
  totalTables: number;
  totalCapacity: number;
  totalOccupiedSeats: number;
  totalSeatedGuestsCount: number;
  totalUnassignedGuestsCount: number;
  overflowConflictsCount: number;
  tables: SeatingTableDto[];
  unassignedGuests: UnassignedGuestDto[];
}

export interface CreateSeatingTablePayload {
  weddingId?: number;
  ceremonyId?: number;
  tableName: string;
  capacity: number;
}

export interface UpdateSeatingTablePayload {
  ceremonyId?: number;
  tableName: string;
  capacity: number;
}

export const seatingApi = {
  getSeatingOverview: async (weddingId?: number): Promise<SeatingOverviewDto> => {
    const url = weddingId ? `/api/v1/tables?weddingId=${weddingId}` : '/api/v1/tables';
    const res = await apiFetch<ApiResponse<SeatingOverviewDto>>(url);
    if (!res.success || !res.data) {
      throw new Error(res.message || 'Failed to fetch seating overview');
    }
    return res.data;
  },

  createTable: async (payload: CreateSeatingTablePayload): Promise<SeatingTableDto> => {
    const res = await apiFetch<ApiResponse<SeatingTableDto>>('/api/v1/tables', {
      method: 'POST',
      body: JSON.stringify(payload),
    });
    if (!res.success || !res.data) {
      throw new Error(res.message || 'Failed to create seating table');
    }
    return res.data;
  },

  updateTable: async (tableId: number, payload: UpdateSeatingTablePayload): Promise<SeatingTableDto> => {
    const res = await apiFetch<ApiResponse<SeatingTableDto>>(`/api/v1/tables/${tableId}`, {
      method: 'PUT',
      body: JSON.stringify(payload),
    });
    if (!res.success || !res.data) {
      throw new Error(res.message || 'Failed to update seating table');
    }
    return res.data;
  },

  deleteTable: async (tableId: number): Promise<void> => {
    const res = await apiFetch<ApiResponse<void>>(`/api/v1/tables/${tableId}`, {
      method: 'DELETE',
    });
    if (!res.success) {
      throw new Error(res.message || 'Failed to delete seating table');
    }
  },

  assignGuest: async (tableId: number, guestId: number): Promise<GuestSeatingDto> => {
    const res = await apiFetch<ApiResponse<GuestSeatingDto>>(`/api/v1/tables/${tableId}/assign`, {
      method: 'POST',
      body: JSON.stringify({ guestId }),
    });
    if (!res.success || !res.data) {
      throw new Error(res.message || 'Failed to assign guest to table');
    }
    return res.data;
  },

  unassignGuest: async (tableId: number, guestId: number): Promise<void> => {
    const res = await apiFetch<ApiResponse<void>>(`/api/v1/tables/${tableId}/unassign/${guestId}`, {
      method: 'DELETE',
    });
    if (!res.success) {
      throw new Error(res.message || 'Failed to unassign guest from table');
    }
  },
};
