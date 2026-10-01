import { BookingStatus, PaymentStatus, Priority, RsvpStatus, TaskStatus } from '../types/wedding';

export type Tone = 'neutral' | 'success' | 'warning' | 'danger' | 'wine' | 'gold' | 'bride' | 'groom';

export const toneClasses: Record<Tone, string> = {
  neutral: 'bg-ink-50 text-ink-600 border-ink-100',
  success: 'bg-success-50 text-success-700 border-success-100',
  warning: 'bg-warning-50 text-warning-700 border-warning-100',
  danger: 'bg-danger-50 text-danger-700 border-danger-100',
  wine: 'bg-wine-50 text-wine-700 border-wine-100',
  gold: 'bg-gold-50 text-gold-700 border-gold-200',
  bride: 'bg-bride-50 text-bride-700 border-bride-100',
  groom: 'bg-groom-50 text-groom-700 border-groom-100'
};

export const toneDot: Record<Tone, string> = {
  neutral: 'bg-ink-300',
  success: 'bg-success-600',
  warning: 'bg-warning-600',
  danger: 'bg-danger-600',
  wine: 'bg-wine-500',
  gold: 'bg-gold-500',
  bride: 'bg-bride-600',
  groom: 'bg-groom-600'
};

export const taskStatusMeta: Record<TaskStatus, {label: string;tone: Tone;}> = {
  'not-started': { label: 'Not Started', tone: 'neutral' },
  'in-progress': { label: 'In Progress', tone: 'gold' },
  completed: { label: 'Completed', tone: 'success' },
  overdue: { label: 'Overdue', tone: 'danger' }
};

export const priorityMeta: Record<Priority, {label: string;tone: Tone;}> = {
  high: { label: 'High', tone: 'wine' },
  medium: { label: 'Medium', tone: 'gold' },
  low: { label: 'Low', tone: 'neutral' }
};

export const rsvpMeta: Record<RsvpStatus, {label: string;tone: Tone;}> = {
  confirmed: { label: 'Confirmed', tone: 'success' },
  pending: { label: 'Pending', tone: 'warning' },
  declined: { label: 'Declined', tone: 'danger' }
};

export const paymentMeta: Record<PaymentStatus, {label: string;tone: Tone;}> = {
  paid: { label: 'Paid in full', tone: 'success' },
  deposit: { label: 'Deposit paid', tone: 'gold' },
  unpaid: { label: 'Unpaid', tone: 'warning' }
};

export const bookingMeta: Record<BookingStatus, {label: string;tone: Tone;}> = {
  booked: { label: 'Booked', tone: 'success' },
  pending: { label: 'Awaiting confirmation', tone: 'warning' },
  shortlisted: { label: 'Shortlisted', tone: 'neutral' }
};