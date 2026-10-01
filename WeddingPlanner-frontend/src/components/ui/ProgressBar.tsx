import React from 'react';

type Tone = 'wine' | 'gold' | 'success' | 'warning' | 'danger';

const fills: Record<Tone, string> = {
  wine: 'bg-wine-700',
  gold: 'bg-gold-500',
  success: 'bg-success-600',
  warning: 'bg-warning-600',
  danger: 'bg-danger-600'
};

interface ProgressBarProps {
  value: number;
  tone?: Tone;
  size?: 'sm' | 'md';
  label?: string;
  className?: string;
}

export function ProgressBar({ value, tone = 'wine', size = 'sm', label, className = '' }: ProgressBarProps) {
  const v = Math.max(0, Math.min(100, value));
  return (
    <div
      role="progressbar"
      aria-valuenow={Math.round(v)}
      aria-valuemin={0}
      aria-valuemax={100}
      aria-label={label}
      className={`w-full overflow-hidden rounded-full bg-ivory-200 ${size === 'sm' ? 'h-1.5' : 'h-2.5'} ${className}`}>
      
      <div className={`h-full rounded-full ${fills[tone]} transition-[width] duration-300 ease-out`} style={{ width: `${v}%` }} />
    </div>);

}