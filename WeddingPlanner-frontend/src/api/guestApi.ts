import { apiFetch, ApiResponse } from './apiClient';
import { Guest, GuestSide, RsvpStatus } from '../types/wedding';

export interface GuestDto {
  id: number;
  weddingId: number;
  fullName: string;
  phone?: string;
  email?: string;
  side: 'BRIDE_SIDE' | 'GROOM_SIDE' | 'SHARED';
  category: 'VIP' | 'FAMILY' | 'FRIEND';
  plusOneAllowed: number;
  status: 'PENDING' | 'CONFIRMED' | 'ATTENDING' | 'DECLINED';
  invitationToken?: string;
  shareableUrl?: string;
  createdAt: string;
  updatedAt: string;
}

export interface CreateGuestPayload {
  weddingId?: number;
  fullName: string;
  phone?: string;
  email?: string;
  side?: 'BRIDE_SIDE' | 'GROOM_SIDE' | 'SHARED';
  category?: 'VIP' | 'FAMILY' | 'FRIEND';
  plusOneAllowed?: number;
  status?: 'PENDING' | 'CONFIRMED' | 'ATTENDING' | 'DECLINED';
}

export interface UpdateGuestPayload {
  fullName?: string;
  phone?: string;
  email?: string;
  side?: 'BRIDE_SIDE' | 'GROOM_SIDE' | 'SHARED';
  category?: 'VIP' | 'FAMILY' | 'FRIEND';
  plusOneAllowed?: number;
  status?: 'PENDING' | 'CONFIRMED' | 'ATTENDING' | 'DECLINED';
}

export interface PublicInvitationDetailsDto {
  token: string;
  weddingTitle: string;
  guestName: string;
  plusOneAllowed: number;
  status: 'PENDING' | 'CONFIRMED' | 'ATTENDING' | 'DECLINED';
  personalMessage?: string;
  plusOneNames?: string;
  dietaryPreferences?: string;
  respondedAt?: string;
  ceremonies?: Array<{
    id: number;
    name: string;
    ceremonyType: string;
    ceremonyDate?: string;
    startTime?: string;
    venueLocation?: string;
    description?: string;
  }>;
}

export interface SubmitRsvpPayload {
  status: 'PENDING' | 'CONFIRMED' | 'ATTENDING' | 'DECLINED';
  plusOneNames?: string;
  dietaryPreferences?: string;
}

export function mapGuestDtoToGuest(dto: GuestDto): Guest {
  const side: GuestSide =
    dto.side === 'BRIDE_SIDE' ? 'bride' : dto.side === 'GROOM_SIDE' ? 'groom' : 'both';
  const rsvp: RsvpStatus =
    dto.status === 'CONFIRMED' || dto.status === 'ATTENDING'
      ? 'confirmed'
      : dto.status === 'DECLINED'
      ? 'declined'
      : 'pending';

  return {
    id: String(dto.id),
    name: dto.fullName,
    side,
    group: dto.category.charAt(0) + dto.category.slice(1).toLowerCase(),
    phone: dto.phone || '',
    rsvp,
    table: null,
    plusOnes: dto.plusOneAllowed || 0,
    invitationOpened: !!dto.invitationToken,
  };
}

export const guestApi = {
  getGuests: (): Promise<ApiResponse<GuestDto[]>> => {
    return apiFetch<GuestDto[]>('/guests');
  },

  getGuestById: (id: number): Promise<ApiResponse<GuestDto>> => {
    return apiFetch<GuestDto>(`/guests/${id}`);
  },

  createGuest: (payload: CreateGuestPayload): Promise<ApiResponse<GuestDto>> => {
    return apiFetch<GuestDto>('/guests', {
      method: 'POST',
      body: JSON.stringify(payload),
    });
  },

  updateGuest: (id: number, payload: UpdateGuestPayload): Promise<ApiResponse<GuestDto>> => {
    return apiFetch<GuestDto>(`/guests/${id}`, {
      method: 'PUT',
      body: JSON.stringify(payload),
    });
  },

  deleteGuest: (id: number): Promise<ApiResponse<void>> => {
    return apiFetch<void>(`/guests/${id}`, {
      method: 'DELETE',
    });
  },

  getPublicInvitation: (token: string): Promise<ApiResponse<PublicInvitationDetailsDto>> => {
    return apiFetch<PublicInvitationDetailsDto>(`/public/invitations/${token}`);
  },

  submitPublicRsvp: (
    token: string,
    payload: SubmitRsvpPayload
  ): Promise<ApiResponse<PublicInvitationDetailsDto>> => {
    return apiFetch<PublicInvitationDetailsDto>(`/public/invitations/${token}/rsvp`, {
      method: 'POST',
      body: JSON.stringify(payload),
    });
  },
};
