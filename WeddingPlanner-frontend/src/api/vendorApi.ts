import { apiFetch, ApiResponse } from './apiClient';

export type VendorCategory =
  | 'VENUE'
  | 'CATERING'
  | 'DECORATION'
  | 'PHOTOGRAPHY'
  | 'BEAUTY'
  | 'TRANSPORT'
  | 'MUSIC'
  | 'ATTIRE'
  | 'OTHER';

export type BookingStatus =
  | 'INQUIRY'
  | 'CONTACTED'
  | 'BOOKED'
  | 'COMPLETED'
  | 'CANCELLED';

export type VendorPaymentStatus = 'UNPAID' | 'DEPOSIT_PAID' | 'PAID_IN_FULL';

export interface VendorDto {
  id: number;
  weddingId: number;
  name: string;
  category: VendorCategory;
  contactPhone?: string;
  contactEmail?: string;
  costRwf: number;
  bookingStatus: BookingStatus;
  paymentStatus: VendorPaymentStatus;
  notes?: string;
}

export interface VendorSummaryDto {
  totalVendors: number;
  bookedVendorsCount: number;
  totalVendorCostRwf: number;
  totalPaidCostRwf: number;
  vendors: VendorDto[];
}

export interface CreateVendorPayload {
  weddingId?: number;
  name: string;
  category: VendorCategory;
  contactPhone?: string;
  contactEmail?: string;
  costRwf?: number;
  bookingStatus?: BookingStatus;
  paymentStatus?: VendorPaymentStatus;
  notes?: string;
}

export interface UpdateVendorPayload {
  name: string;
  category: VendorCategory;
  contactPhone?: string;
  contactEmail?: string;
  costRwf?: number;
  bookingStatus?: BookingStatus;
  paymentStatus?: VendorPaymentStatus;
  notes?: string;
}

export const vendorApi = {
  getVendors: async (weddingId?: number): Promise<VendorSummaryDto> => {
    const url = weddingId ? `/api/v1/vendors?weddingId=${weddingId}` : '/api/v1/vendors';
    const res = await apiFetch<ApiResponse<VendorSummaryDto>>(url);
    if (!res.success || !res.data) {
      throw new Error(res.message || 'Failed to fetch vendor summary');
    }
    return res.data;
  },

  createVendor: async (payload: CreateVendorPayload): Promise<VendorDto> => {
    const res = await apiFetch<ApiResponse<VendorDto>>('/api/v1/vendors', {
      method: 'POST',
      body: JSON.stringify(payload),
    });
    if (!res.success || !res.data) {
      throw new Error(res.message || 'Failed to create vendor');
    }
    return res.data;
  },

  updateVendor: async (id: number, payload: UpdateVendorPayload): Promise<VendorDto> => {
    const res = await apiFetch<ApiResponse<VendorDto>>(`/api/v1/vendors/${id}`, {
      method: 'PUT',
      body: JSON.stringify(payload),
    });
    if (!res.success || !res.data) {
      throw new Error(res.message || 'Failed to update vendor');
    }
    return res.data;
  },

  deleteVendor: async (id: number): Promise<void> => {
    const res = await apiFetch<ApiResponse<void>>(`/api/v1/vendors/${id}`, {
      method: 'DELETE',
    });
    if (!res.success) {
      throw new Error(res.message || 'Failed to delete vendor');
    }
  },
};
