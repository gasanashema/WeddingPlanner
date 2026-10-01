import React from 'react';

interface SegmentedProps<T extends string> {
  options: {value: T;label: string;}[];
  value: T;
  onChange: (v: T) => void;
  ariaLabel: string;
  className?: string;
}

export function Segmented<T extends string>({ options, value, onChange, ariaLabel, className = '' }: SegmentedProps<T>) {
  return (
    <div role="radiogroup" aria-label={ariaLabel} className={`inline-flex rounded-md border border-line-strong bg-white p-0.5 ${className}`}>
      {options.map((o) => {
        const active = o.value === value;
        return (
          <button
            key={o.value}
            type="button"
            role="radio"
            aria-checked={active}
            onClick={() => onChange(o.value)}
            className={`h-8 whitespace-nowrap rounded-[5px] px-3 text-[13px] font-medium transition-colors duration-150 ${
            active ? 'bg-wine-700 text-white' : 'text-ink-600 hover:bg-ivory-100 hover:text-ink'}`
            }>
            
            {o.label}
          </button>);

      })}
    </div>);

}