import { apiFetch, ApiResponse } from './apiClient';
import { Expense, BudgetCategory, Scope } from '../types/wedding';

export interface ExpenseDto {
  id: number;
  weddingId: number;
  ceremonyId?: number;
  ceremonyName?: string;
  budgetCategoryId?: number;
  budgetCategoryName?: string;
  title: string;
  amountRwf: number;
  paidByUserId?: number;
  paidByUserName?: string;
  dateSpent: string;
  visibilityScope: 'BRIDE_PRIVATE' | 'GROOM_PRIVATE' | 'SHARED';
  receiptNotes?: string;
  deletedAt?: string;
  createdAt: string;
  updatedAt: string;
}

export interface BudgetCategoryDto {
  id: number;
  name: string;
  allocatedAmountRwf: number;
  spentAmountRwf: number;
  visibilityScope: 'BRIDE_PRIVATE' | 'GROOM_PRIVATE' | 'SHARED';
}

export interface BudgetSummaryDto {
  weddingId: number;
  totalPlannedRwf: number;
  totalSpentRwf: number;
  remainingRwf: number;
  totalContributionsRwf: number;
  categories: BudgetCategoryDto[];
  expenses: ExpenseDto[];
}

export interface CreateExpensePayload {
  weddingId?: number;
  ceremonyId?: number;
  budgetCategoryId?: number;
  title: string;
  amountRwf: number;
  paidByUserId?: number;
  dateSpent: string;
  visibilityScope?: 'BRIDE_PRIVATE' | 'GROOM_PRIVATE' | 'SHARED';
  receiptNotes?: string;
}

export interface UpdateExpensePayload {
  ceremonyId?: number;
  budgetCategoryId?: number;
  title?: string;
  amountRwf?: number;
  paidByUserId?: number;
  dateSpent?: string;
  visibilityScope?: 'BRIDE_PRIVATE' | 'GROOM_PRIVATE' | 'SHARED';
  receiptNotes?: string;
}

export interface CreateBudgetCategoryPayload {
  weddingId?: number;
  name: string;
  allocatedAmountRwf: number;
  visibilityScope?: 'BRIDE_PRIVATE' | 'GROOM_PRIVATE' | 'SHARED';
}

export interface MomoWebhookPayload {
  weddingId: number;
  contributorName: string;
  contributorPhone: string;
  amountRwf: number;
  momoTransactionId: string;
  status?: 'PENDING' | 'COMPLETED' | 'FAILED';
  signature?: string;
}

export function mapExpenseDtoToExpense(dto: ExpenseDto): Expense {
  const scope: Scope =
    dto.visibilityScope === 'BRIDE_PRIVATE'
      ? 'private-bride'
      : dto.visibilityScope === 'GROOM_PRIVATE'
      ? 'private-groom'
      : 'shared';

  const catName = dto.budgetCategoryName || '';
  let category: BudgetCategory = 'Other';
  if (catName.includes('Venue')) category = 'Venue';
  else if (catName.includes('Food') || catName.includes('Catering')) category = 'Food';
  else if (catName.includes('Decor')) category = 'Decoration';
  else if (catName.includes('Attire') || catName.includes('Clothing')) category = 'Clothing';
  else if (catName.includes('Photo')) category = 'Photography';
  else if (catName.includes('Home')) category = 'Home Preparation';

  return {
    id: String(dto.id),
    title: dto.title,
    category,
    scope,
    amount: dto.amountRwf,
    date: dto.dateSpent || '2027-08-01',
    paidBy: 'bride',
    method: 'MoMo',
  };
}

export const budgetApi = {
  getBudgetSummary: (): Promise<ApiResponse<BudgetSummaryDto>> => {
    return apiFetch<BudgetSummaryDto>('/budget');
  },

  createCategory: (payload: CreateBudgetCategoryPayload): Promise<ApiResponse<BudgetCategoryDto>> => {
    return apiFetch<BudgetCategoryDto>('/budget/categories', {
      method: 'POST',
      body: JSON.stringify(payload),
    });
  },

  getExpenses: (): Promise<ApiResponse<ExpenseDto[]>> => {
    return apiFetch<ExpenseDto[]>('/expenses');
  },

  getExpenseById: (id: number): Promise<ApiResponse<ExpenseDto>> => {
    return apiFetch<ExpenseDto>(`/expenses/${id}`);
  },

  createExpense: (payload: CreateExpensePayload): Promise<ApiResponse<ExpenseDto>> => {
    return apiFetch<ExpenseDto>('/expenses', {
      method: 'POST',
      body: JSON.stringify(payload),
    });
  },

  updateExpense: (id: number, payload: UpdateExpensePayload): Promise<ApiResponse<ExpenseDto>> => {
    return apiFetch<ExpenseDto>(`/expenses/${id}`, {
      method: 'PUT',
      body: JSON.stringify(payload),
    });
  },

  deleteExpense: (id: number): Promise<ApiResponse<void>> => {
    return apiFetch<void>(`/expenses/${id}`, {
      method: 'DELETE',
    });
  },

  restoreExpense: (id: number): Promise<ApiResponse<ExpenseDto>> => {
    return apiFetch<ExpenseDto>(`/expenses/${id}/restore`, {
      method: 'POST',
    });
  },

  getDeletedExpenses: (): Promise<ApiResponse<ExpenseDto[]>> => {
    return apiFetch<ExpenseDto[]>('/expenses/deleted');
  },

  submitMomoContribution: (payload: MomoWebhookPayload): Promise<ApiResponse<unknown>> => {
    return apiFetch<unknown>('/public/contributions/webhook', {
      method: 'POST',
      body: JSON.stringify(payload),
    });
  },
};
