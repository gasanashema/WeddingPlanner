import React from 'react';
import { formatRWF } from '../../utils/format';

interface TooltipPayload {
  dataKey: string;
  value: number;
  payload: {category: string;};
}

interface BudgetTooltipProps {
  active?: boolean;
  payload?: TooltipPayload[];
}

export function BudgetTooltip({ active, payload }: BudgetTooltipProps) {
  if (!active || !payload?.length) return null;
  const planned = payload.find((p) => p.dataKey === 'planned')?.value ?? 0;
  const spent = payload.find((p) => p.dataKey === 'spent')?.value ?? 0;
  return (
    <div className="rounded-md border border-line bg-white px-3 py-2.5 text-xs shadow-pop">
      <p className="mb-1.5 font-semibold text-ink">{payload[0].payload.category}</p>
      <p className="tnum flex justify-between gap-6 text-ink-500">
        Planned <span className="text-ink">{formatRWF(planned)}</span>
      </p>
      <p className="tnum flex justify-between gap-6 text-ink-500">
        Actual <span className="text-ink">{formatRWF(spent)}</span>
      </p>
    </div>);

}