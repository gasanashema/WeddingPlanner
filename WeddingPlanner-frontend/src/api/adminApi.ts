import { apiFetch, ApiResponse } from './apiClient';
import { PageResponse } from './auditLogApi';

export interface AdminUserDto {
  id: number;
  email: string;
  fullName: string;
  phoneNumber?: string;
  role: string;
  createdAt: string;
}

export interface AdminWeddingDto {
  id: number;
  title: string;
  partnerCode: string;
  familyCode: string;
  memberCount: number;
  createdAt: string;
}

export interface UpdateUserRolePayload {
  role: string;
}

export const adminApi = {
  getUsers: (page = 0, size = 20): Promise<ApiResponse<PageResponse<AdminUserDto>>> => {
    return apiFetch<PageResponse<AdminUserDto>>(`/admin/users?page=${page}&size=${size}`);
  },

  updateUserRole: (userId: number, role: string): Promise<ApiResponse<AdminUserDto>> => {
    return apiFetch<AdminUserDto>(`/admin/users/${userId}/role`, {
      method: 'PUT',
      body: JSON.stringify({ role }),
    });
  },

  getWeddings: (page = 0, size = 20): Promise<ApiResponse<PageResponse<AdminWeddingDto>>> => {
    return apiFetch<PageResponse<AdminWeddingDto>>(`/admin/weddings?page=${page}&size=${size}`);
  },

  deleteUser: (userId: number): Promise<ApiResponse<void>> => {
    return apiFetch<void>(`/admin/users/${userId}`, {
      method: 'DELETE',
    });
  },
};
