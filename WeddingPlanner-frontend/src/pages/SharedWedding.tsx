import React from 'react';
import { sharedAreas } from '../data/shared';
import { formatCompact, formatDate, relativeDue, daysFromToday } from '../utils/format';
import { taskStatusMeta } from '../utils/status';
import { iconFor } from '../utils/ui';
import { PageHeader } from '../components/ui/PageHeader';
import { ProgressBar } from '../components/ui/ProgressBar';
import { Badge } from '../components/ui/Badge';
import { PersonChip } from '../components/ui/PersonChip';

export function SharedWedding() {
  const totalBudget = sharedAreas.filter((a) => a.id !== 'a11').reduce((s, a) => s + a.budget, 0);
  const totalSpent = sharedAreas.filter((a) => a.id !== 'a11').reduce((s, a) => s + a.spent, 0);
  const avgProgress = Math.round(sharedAreas.reduce((s, a) => s + a.progress, 0) / sharedAreas.length);
  const needsAttention = sharedAreas.filter((a) => a.status !== 'completed' && daysFromToday(a.deadline) <= 21 && a.progress < 75);

  return (
    <div>
      <PageHeader
        title="Shared Wedding"
        description="Everything the couple plans together. Family supporters don’t see this area."
        scope="shared" />
      

      <section aria-label="Summary" className="mb-6 grid gap-px overflow-hidden rounded-lg border border-line bg-line shadow-card sm:grid-cols-3">
        <div className="bg-white p-5">
          <p className="text-xs text-ink-500">Overall progress</p>
          <p className="tnum mt-1 font-serif text-3xl font-semibold text-ink">{avgProgress}%</p>
          <ProgressBar value={avgProgress} className="mt-3" label="Overall shared progress" />
        </div>
        <div className="bg-white p-5">
          <p className="text-xs text-ink-500">Shared budget</p>
          <p className="tnum mt-1 text-2xl font-semibold text-ink">RWF {formatCompact(totalSpent)} <span className="text-base font-normal text-ink-500">/ {formatCompact(totalBudget)}</span></p>
          <p className="mt-2 text-xs text-ink-500">{Math.round(totalSpent / totalBudget * 100)}% spent across {sharedAreas.length - 1} areas</p>
        </div>
        <div className="bg-white p-5">
          <p className="text-xs text-ink-500">Needs attention</p>
          <p className="tnum mt-1 text-2xl font-semibold text-wine-700">{needsAttention.length} areas</p>
          <p className="mt-2 truncate text-xs text-ink-500">{needsAttention.map((a) => a.name).join(', ')}</p>
        </div>
      </section>

      <section aria-label="Planning areas" className="overflow-hidden rounded-lg border border-line bg-white shadow-card">
        <div className="hidden grid-cols-[minmax(0,1.7fr)_minmax(0,1fr)_140px_110px_110px_120px] gap-4 border-b border-line bg-ivory px-5 py-3 text-xs font-medium text-ink-500 lg:grid">
          <span>Area</span>
          <span>Progress</span>
          <span>Assigned to</span>
          <span>Deadline</span>
          <span className="text-right">Budget</span>
          <span>Status</span>
        </div>
        <ul className="divide-y divide-line">
          {sharedAreas.map((a) => {
            const Icon = iconFor(a.icon);
            const meta = taskStatusMeta[a.status];
            const soon = a.status !== 'completed' && daysFromToday(a.deadline) <= 14;
            return (
              <li key={a.id} className="grid gap-3 px-5 py-4 lg:grid-cols-[minmax(0,1.7fr)_minmax(0,1fr)_140px_110px_110px_120px] lg:items-center lg:gap-4">
                <div className="flex min-w-0 items-start gap-3">
                  <span className="flex h-9 w-9 shrink-0 items-center justify-center rounded-md border border-gold-200 bg-gold-50 text-gold-700">
                    <Icon className="h-4 w-4" aria-hidden />
                  </span>
                  <div className="min-w-0">
                    <p className="text-sm font-medium text-ink">{a.name}</p>
                    <p className="truncate text-xs text-ink-500">{a.nextStep}</p>
                  </div>
                  <span className="ml-auto lg:hidden">
                    <Badge tone={meta.tone} dot>{meta.label}</Badge>
                  </span>
                </div>
                <div className="flex items-center gap-3">
                  <ProgressBar value={a.progress} tone={a.progress === 100 ? 'success' : 'wine'} label={`${a.name} progress`} />
                  <span className="tnum w-9 shrink-0 text-right text-xs font-medium text-ink-700">{a.progress}%</span>
                </div>
                <div className="flex items-center justify-between gap-3 text-sm lg:contents">
                  <PersonChip role={a.assignee} />
                  <span className={`tnum text-xs lg:text-sm ${soon ? 'font-medium text-wine-700' : 'text-ink-600'}`} title={formatDate(a.deadline)}>
                    {a.status === 'completed' ? formatDate(a.deadline, 'd MMM') : relativeDue(a.deadline)}
                  </span>
                  <span className="tnum text-xs text-ink-700 lg:text-right lg:text-sm">{a.budget ? `RWF ${formatCompact(a.budget)}` : '—'}</span>
                </div>
                <span className="hidden lg:block">
                  <Badge tone={meta.tone} dot>{meta.label}</Badge>
                </span>
              </li>);

          })}
        </ul>
      </section>
    </div>);

}