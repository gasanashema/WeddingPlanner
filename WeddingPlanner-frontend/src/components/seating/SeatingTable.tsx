import React from 'react';
import { SeatingTable as SeatingTableType } from '../../types/wedding';

interface SeatingTableProps {
  table: SeatingTableType;
  used: number;
  selected: boolean;
  onSelect: () => void;
}

export function SeatingTable({ table, used, selected, onSelect }: SeatingTableProps) {
  const full = used >= table.capacity;
  const seats = Array.from({ length: table.capacity }, (_, i) => {
    const angle = i / table.capacity * Math.PI * 2 - Math.PI / 2;
    return { x: 50 + 42 * Math.cos(angle), y: 50 + 42 * Math.sin(angle), filled: i < used };
  });

  return (
    <button
      type="button"
      onClick={onSelect}
      aria-pressed={selected}
      aria-label={`${table.name}, ${table.note}, ${used} of ${table.capacity} seats filled`}
      className={`flex flex-col items-center rounded-lg border p-3 transition-colors duration-150 ${
      selected ? 'border-wine-600 bg-wine-50' : 'border-transparent hover:border-line hover:bg-white'}`
      }>
      
      <div className="relative h-28 w-28">
        {seats.map((s, i) =>
        <span
          key={i}
          className={`absolute h-3.5 w-3.5 -translate-x-1/2 -translate-y-1/2 rounded-full border ${
          s.filled ? 'border-wine-700 bg-wine-700' : 'border-line-strong bg-white'}`
          }
          style={{ left: `${s.x}%`, top: `${s.y}%` }}
          aria-hidden />

        )}
        <span
          className={`absolute left-1/2 top-1/2 flex h-16 w-16 -translate-x-1/2 -translate-y-1/2 flex-col items-center justify-center rounded-full border ${
          table.id === 1 ? 'border-gold-300 bg-gold-50' : 'border-line bg-surface'}`
          }>
          
          <span className="font-serif text-lg font-semibold leading-none text-ink">{table.id === 1 ? 'H' : table.id}</span>
          <span className={`tnum mt-1 text-[10px] ${full ? 'font-medium text-wine-700' : 'text-ink-500'}`}>
            {used}/{table.capacity}
          </span>
        </span>
      </div>
      <span className="mt-1 text-xs font-medium text-ink">{table.name}</span>
      <span className="text-[11px] text-ink-500">{table.note}</span>
    </button>);

}