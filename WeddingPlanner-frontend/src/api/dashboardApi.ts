import { apiFetch, ApiResponse } from './apiClient';

export interface DashboardSummaryDto {
  weddingId: number;
  weddingTitle: string;
  totalTasks: number;
  completedTasks: number;
  inProgressTasks: number;
  pendingTasks: number;
  totalBudgetPlanned: number;
  totalBudgetSpent: number;
  totalBudgetRemaining: number;
  totalGuests: number;
  confirmedGuests: number;
  pendingGuests: number;
  declinedGuests: number;
  totalHomePrepItems: number;
  completedHomePrepItems: number;
  totalVendors: number;
  confirmedVendors: number;
  totalTimelines: number;
}

export const dashboardApi = {
  getSummary: (): Promise<ApiResponse<DashboardSummaryDto>> => {
    return apiFetch<DashboardSummaryDto>('/dashboard/summary');
  },
};
