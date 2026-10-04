import { apiFetch, ApiResponse } from './apiClient';

export interface AuditLogDto {
  id: string;
  userId?: number;
  userEmail?: string;
  weddingId?: number;
  action: string;
  resource: string;
  details?: string;
  ipAddress?: string;
  timestamp: string;
}

export interface PageResponse<T> {
  content: T[];
  totalPages: number;
  totalElements: number;
  size: number;
  number: number;
}

export const auditLogApi = {
  getLogs: (page = 0, size = 20, action?: string): Promise<ApiResponse<PageResponse<AuditLogDto>>> => {
    const params = new URLSearchParams({ page: page.toString(), size: size.toString() });
    if (action) {
      params.append('action', action);
    }
    return apiFetch<PageResponse<AuditLogDto>>(`/audit-logs?${params.toString()}`);
  },
};
