import { apiFetch, ApiResponse } from './apiClient';

export interface TaskDto {
  id: number;
  weddingId: number;
  ceremonyId?: number;
  ceremonyName?: string;
  assignedUserId?: number;
  assignedUserName?: string;
  title: string;
  description?: string;
  priority: 'LOW' | 'MEDIUM' | 'HIGH';
  status: 'PENDING' | 'IN_PROGRESS' | 'COMPLETED';
  visibilityScope: 'BRIDE_PRIVATE' | 'GROOM_PRIVATE' | 'SHARED';
  dueDate?: string;
  estimatedBudget?: number;
  deletedAt?: string;
  createdAt: string;
  updatedAt: string;
}

export interface CreateTaskPayload {
  weddingId?: number;
  ceremonyId?: number;
  assignedUserId?: number;
  title: string;
  description?: string;
  priority?: 'LOW' | 'MEDIUM' | 'HIGH';
  status?: 'PENDING' | 'IN_PROGRESS' | 'COMPLETED';
  visibilityScope?: 'BRIDE_PRIVATE' | 'GROOM_PRIVATE' | 'SHARED';
  dueDate?: string;
  estimatedBudget?: number;
}

export interface UpdateTaskPayload {
  title?: string;
  description?: string;
  ceremonyId?: number;
  assignedUserId?: number;
  priority?: 'LOW' | 'MEDIUM' | 'HIGH';
  status?: 'PENDING' | 'IN_PROGRESS' | 'COMPLETED';
  visibilityScope?: 'BRIDE_PRIVATE' | 'GROOM_PRIVATE' | 'SHARED';
  dueDate?: string;
  estimatedBudget?: number;
}

export const taskApi = {
  getTasks: (): Promise<ApiResponse<TaskDto[]>> => {
    return apiFetch<TaskDto[]>('/tasks');
  },

  getTaskById: (id: number): Promise<ApiResponse<TaskDto>> => {
    return apiFetch<TaskDto>(`/tasks/${id}`);
  },

  createTask: (payload: CreateTaskPayload): Promise<ApiResponse<TaskDto>> => {
    return apiFetch<TaskDto>('/tasks', {
      method: 'POST',
      body: JSON.stringify(payload),
    });
  },

  updateTask: (id: number, payload: UpdateTaskPayload): Promise<ApiResponse<TaskDto>> => {
    return apiFetch<TaskDto>(`/tasks/${id}`, {
      method: 'PUT',
      body: JSON.stringify(payload),
    });
  },

  deleteTask: (id: number): Promise<ApiResponse<void>> => {
    return apiFetch<void>(`/tasks/${id}`, {
      method: 'DELETE',
    });
  },

  restoreTask: (id: number): Promise<ApiResponse<TaskDto>> => {
    return apiFetch<TaskDto>(`/tasks/${id}/restore`, {
      method: 'POST',
    });
  },

  getDeletedTasks: (): Promise<ApiResponse<TaskDto[]>> => {
    return apiFetch<TaskDto[]>('/tasks/deleted');
  },
};
