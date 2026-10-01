import { addDays, differenceInCalendarDays, format, parseISO } from 'date-fns';
import { TODAY } from '../data/wedding';

export const formatRWF = (n: number) => `RWF ${Math.round(n).toLocaleString('en-US')}`;

export function formatCompact(n: number): string {
  if (Math.abs(n) >= 1_000_000) {
    const v = n / 1_000_000;
    return `${Number.isInteger(v) ? v : v.toFixed(1)}M`;
  }
  if (Math.abs(n) >= 1000) return `${Math.round(n / 1000)}K`;
  return `${n}`;
}

export const formatDate = (iso: string, pattern = 'd MMM yyyy') => format(parseISO(iso), pattern);

export const daysFromToday = (iso: string) => differenceInCalendarDays(parseISO(iso), parseISO(TODAY));

export const addDaysISO = (iso: string, n: number) => format(addDays(parseISO(iso), n), 'yyyy-MM-dd');

export function relativeDue(iso: string): string {
  const d = daysFromToday(iso);
  if (d === 0) return 'Today';
  if (d === 1) return 'Tomorrow';
  if (d < 0) return `${Math.abs(d)}d overdue`;
  if (d <= 14) return `In ${d} days`;
  return formatDate(iso, 'd MMM');
}