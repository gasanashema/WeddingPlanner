import { apiFetch, ApiResponse } from './apiClient';

export interface UserDto {
  id: number;
  firstName: string;
  lastName: string;
  email: string;
  phoneNumber?: string;
  role: string;
  enabled: boolean;
  mustChangePassword?: boolean;
}

export interface AuthResponseData {
  token: string;
  user: UserDto;
}

export interface RegisterPayload {
  firstName: string;
  lastName: string;
  email: string;
  password?: string;
  phoneNumber?: string;
  role?: string;
  partnerFirstName?: string;
  partnerLastName?: string;
  partnerPhone?: string;
  partnerEmail?: string;
}

export interface LoginPayload {
  email: string;
  password?: string;
}

export interface ChangePasswordPayload {
  currentPassword?: string;
  newPassword?: string;
}

export const authApi = {
  register: (payload: RegisterPayload): Promise<ApiResponse<AuthResponseData>> => {
    return apiFetch<AuthResponseData>('/auth/register', {
      method: 'POST',
      body: JSON.stringify(payload),
    });
  },

  login: (payload: LoginPayload): Promise<ApiResponse<AuthResponseData>> => {
    return apiFetch<AuthResponseData>('/auth/login', {
      method: 'POST',
      body: JSON.stringify(payload),
    });
  },

  getMe: (): Promise<ApiResponse<UserDto>> => {
    return apiFetch<UserDto>('/auth/me');
  },

  changePassword: (payload: ChangePasswordPayload): Promise<ApiResponse<void>> => {
    return apiFetch<void>('/auth/change-password', {
      method: 'POST',
      body: JSON.stringify(payload),
    });
  },
};

