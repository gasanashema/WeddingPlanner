import React from 'react';
import { Bar, BarChart, CartesianGrid, ResponsiveContainer, Tooltip, XAxis, YAxis } from 'recharts';
import { CategoryTotal, shortCategory } from '../../utils/budget';
import { formatCompact } from '../../utils/format';
import { BudgetTooltip } from './BudgetTooltip';

export function BudgetChart({ data }: {data: CategoryTotal[];}) {
  const chartData = data.map((d) => ({ ...d, short: shortCategory(d.category) }));
  return (
    <div className="h-72 w-full">
      <ResponsiveContainer width="100%" height="100%">
        <BarChart data={chartData} barGap={3} barCategoryGap="24%" margin={{ top: 8, right: 4, left: -8, bottom: 0 }}>
          <CartesianGrid vertical={false} stroke="#E9E2D8" />
          <XAxis dataKey="short" tick={{ fontSize: 11, fill: '#6B625D' }} axisLine={false} tickLine={false} interval={0} />
          <YAxis tickFormatter={(v: number) => formatCompact(v)} tick={{ fontSize: 11, fill: '#6B625D' }} axisLine={false} tickLine={false} width={48} />
          <Tooltip content={<BudgetTooltip />} cursor={{ fill: '#F4EFE7' }} />
          <Bar dataKey="planned" fill="#EADBB6" radius={[3, 3, 0, 0]} maxBarSize={22} />
          <Bar dataKey="spent" fill="#5E2331" radius={[3, 3, 0, 0]} maxBarSize={22} />
        </BarChart>
      </ResponsiveContainer>
    </div>);

}