import React from 'react';
import { motion } from 'framer-motion';
import { EASE } from '../../utils/ui';

export interface TabItem<T extends string> {
  value: T;
  label: string;
  count?: number;
}

interface TabsProps<T extends string> {
  id: string;
  items: TabItem<T>[];
  value: T;
  onChange: (v: T) => void;
  className?: string;
}

export function Tabs<T extends string>({ id, items, value, onChange, className = '' }: TabsProps<T>) {
  return (
    <div role="tablist" className={`scrollbar-none flex gap-6 overflow-x-auto border-b border-line ${className}`}>
      {items.map((it) => {
        const active = it.value === value;
        return (
          <button
            key={it.value}
            type="button"
            role="tab"
            aria-selected={active}
            onClick={() => onChange(it.value)}
            className={`relative flex shrink-0 items-center whitespace-nowrap pb-3 pt-1 text-sm font-medium transition-colors duration-150 ${
            active ? 'text-ink' : 'text-ink-500 hover:text-ink'}`
            }>
            
            {it.label}
            {it.count !== undefined &&
            <span
              className={`tnum ml-2 rounded px-1.5 py-0.5 text-[11px] ${active ? 'bg-wine-50 text-wine-700' : 'bg-ivory-100 text-ink-500'}`}>
              
                {it.count}
              </span>
            }
            {active &&
            <motion.span
              layoutId={`tab-underline-${id}`}
              className="absolute inset-x-0 -bottom-px h-0.5 bg-wine-700"
              transition={{ duration: 0.2, ease: EASE }} />

            }
          </button>);

      })}
    </div>);

}