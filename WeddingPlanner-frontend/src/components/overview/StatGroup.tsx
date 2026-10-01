import React from "react";
import { Link } from "react-router-dom";
import { ArrowUpRightIcon, BoxIcon } from "lucide-react";
interface StatGroupProps {
  icon: BoxIcon;
  title: string;
  to: string;
  stats: {
    label: string;
    value: string;
    note?: string;
    emphasis?: boolean;
  }[];
}
export function StatGroup({
  icon: Icon,
  title,
  to,
  stats
}: StatGroupProps) {
  return <div className="p-5">
      <div className="flex items-center justify-between">
        <p className="flex items-center gap-2 text-sm font-medium text-ink-700">
          <Icon className="h-4 w-4 text-gold-600" aria-hidden />
          {title}
        </p>
        <Link to={to} aria-label={`Open ${title}`} className="rounded p-1 text-ink-400 transition-colors duration-150 hover:bg-ivory-100 hover:text-ink">
          <ArrowUpRightIcon className="h-4 w-4" />
        </Link>
      </div>
      <dl className="mt-4 grid grid-cols-2 gap-4">
        {stats.map((s) => <div key={s.label}>
            <dt className="text-xs text-ink-500">{s.label}</dt>
            <dd className={`tnum mt-1 text-2xl font-semibold tracking-tight ${s.emphasis ? 'text-wine-700' : 'text-ink'}`}>{s.value}</dd>
            {s.note && <dd className="mt-0.5 text-xs text-ink-500">{s.note}</dd>}
          </div>)}
      </dl>
    </div>;
}