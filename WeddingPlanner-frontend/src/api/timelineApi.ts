import { apiFetch, ApiResponse } from './apiClient';

export interface TimelineItemDto {
  id: number;
  weddingId: number;
  ceremonyId?: number;
  ceremonyName?: string;
  title: string;
  targetDate: string;
  targetTime?: string;
  completed: boolean;
}

export interface CreateTimelineItemPayload {
  weddingId?: number;
  ceremonyId?: number;
  title: string;
  targetDate: string;
  targetTime?: string;
  completed?: boolean;
}

export interface UpdateTimelineItemPayload {
  ceremonyId?: number;
  title: string;
  targetDate: string;
  targetTime?: string;
  completed?: boolean;
}

export const timelineApi = {
  getTimelineItems: async (weddingId?: number, ceremonyId?: number): Promise<TimelineItemDto[]> => {
    let url = '/api/v1/timelines';
    const params = new URLSearchParams();
    if (weddingId) params.append('weddingId', weddingId.toString());
    if (ceremonyId) params.append('ceremonyId', ceremonyId.toString());
    if (params.toString()) {
      url += `?${params.toString()}`;
    }

    const res = await apiFetch<ApiResponse<TimelineItemDto[]>>(url);
    if (!res.success || !res.data) {
      throw new Error(res.message || 'Failed to fetch timeline items');
    }
    return res.data;
  },

  createTimelineItem: async (payload: CreateTimelineItemPayload): Promise<TimelineItemDto> => {
    const res = await apiFetch<ApiResponse<TimelineItemDto>>('/api/v1/timelines', {
      method: 'POST',
      body: JSON.stringify(payload),
    });
    if (!res.success || !res.data) {
      throw new Error(res.message || 'Failed to create timeline item');
    }
    return res.data;
  },

  updateTimelineItem: async (id: number, payload: UpdateTimelineItemPayload): Promise<TimelineItemDto> => {
    const res = await apiFetch<ApiResponse<TimelineItemDto>>(`/api/v1/timelines/${id}`, {
      method: 'PUT',
      body: JSON.stringify(payload),
    });
    if (!res.success || !res.data) {
      throw new Error(res.message || 'Failed to update timeline item');
    }
    return res.data;
  },

  toggleCompletion: async (id: number): Promise<TimelineItemDto> => {
    const res = await apiFetch<ApiResponse<TimelineItemDto>>(`/api/v1/timelines/${id}/toggle`, {
      method: 'PATCH',
    });
    if (!res.success || !res.data) {
      throw new Error(res.message || 'Failed to toggle timeline completion');
    }
    return res.data;
  },

  deleteTimelineItem: async (id: number): Promise<void> => {
    const res = await apiFetch<ApiResponse<void>>(`/api/v1/timelines/${id}`, {
      method: 'DELETE',
    });
    if (!res.success) {
      throw new Error(res.message || 'Failed to delete timeline item');
    }
  },
};
