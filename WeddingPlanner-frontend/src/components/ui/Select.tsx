import React from 'react';
import { ChevronDownIcon } from 'lucide-react';
import { inputClass } from '../../utils/ui';

interface SelectProps<T extends string> {
  id?: string;
  value: T;
  onChange: (v: T) => void;
  options: {value: T;label: string;}[];
  ariaLabel?: string;
  className?: string;
  disabled?: boolean;
}

export function Select<T extends string>({ id, value, onChange, options, ariaLabel, className = '', disabled }: SelectProps<T>) {
  return (
    <div className={`relative ${className}`}>
      <select
        id={id}
        aria-label={ariaLabel}
        value={value}
        disabled={disabled}
        onChange={(e) => onChange(e.target.value as T)}
        className={`${inputClass} cursor-pointer appearance-none pr-9 disabled:cursor-not-allowed disabled:bg-ivory-100 disabled:text-ink-500`}>
        
        {options.map((o) =>
        <option key={o.value} value={o.value}>
            {o.label}
          </option>
        )}
      </select>
      <ChevronDownIcon className="pointer-events-none absolute right-3 top-1/2 h-4 w-4 -translate-y-1/2 text-ink-400" aria-hidden />
    </div>);

}