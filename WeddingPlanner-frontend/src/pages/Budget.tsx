import React, { useState } from 'react';
import { PlusIcon } from 'lucide-react';
import { Scope } from '../types/wedding';
import { useRole } from '../contexts/RoleContext';
import { useQuickAdd } from '../contexts/QuickAddContext';
import { useVisibleData } from '../hooks/useVisibleData';
import { people, wedding } from '../data/wedding';
import { aggregateByCategory } from '../utils/budget';
import { daysFromToday, formatDate, formatRWF } from '../utils/format';
import { scopeMeta } from '../utils/permissions';
import { iconFor } from '../utils/ui';
import { PageHeader } from '../components/ui/PageHeader';
import { Button } from '../components/ui/Button';
import { Card } from '../components/ui/Card';
import { Tabs } from '../components/ui/Tabs';
import { ProgressBar } from '../components/ui/ProgressBar';
import { ScopeBadge } from '../components/ui/ScopeBadge';
import { BudgetChart } from '../components/budget/BudgetChart';

type Filter = 'all' | Scope;

export function Budget() {
  const { visibleScopes, isSupport, side } = useRole();
  const { budgetLines, expenses } = useVisibleData();
  const { openExpense } = useQuickAdd();
  const [filter, setFilter] = useState<Filter>('all');

  const lines = filter === 'all' ? budgetLines : budgetLines.filter((l) => l.scope === filter);
  const exp = filter === 'all' ? expenses : expenses.filter((e) => e.scope === filter);
  const planned = lines.reduce((s, l) => s + l.planned, 0);
  const spent = exp.reduce((s, e) => s + e.amount, 0);
  const remaining = planned - spent;
  const pct = planned ? Math.round(spent / planned * 100) : 0;
  const rows = aggregateByCategory(lines, exp);
  const recent = [...exp].sort((a, b) => b.date.localeCompare(a.date)).slice(0, 7);

  return (
    <div>
      <PageHeader
        title="Budget & Expenses"
        description={isSupport ? `The ${side}-side budget you share with ${people[side].firstName}.` : 'Your private, family-side and shared budgets in one place.'}
        actions={
        <Button icon={PlusIcon} onClick={openExpense}>
            Add expense
          </Button>
        } />
      

      {visibleScopes.length > 1 &&
      <Tabs<Filter>
        id="budget-scope"
        className="mb-6"
        value={filter}
        onChange={setFilter}
        items={[{ value: 'all', label: 'All budgets' }, ...visibleScopes.map((s) => ({ value: s as Filter, label: scopeMeta[s].label }))]} />

      }

      <section aria-label="Budget totals" className="mb-6 rounded-lg border border-line bg-white p-6 shadow-card">
        <div className="grid gap-6 md:grid-cols-[1.4fr_1fr_1fr] md:divide-x md:divide-line">
          <div>
            <p className="text-xs text-ink-500">Total planned budget</p>
            <p className="tnum mt-1 text-3xl font-semibold tracking-tight text-ink sm:text-4xl">{formatRWF(planned)}</p>
          </div>
          <div className="md:pl-6">
            <p className="text-xs text-ink-500">Total spent</p>
            <p className="tnum mt-1 text-2xl font-semibold text-wine-700">{formatRWF(spent)}</p>
            <p className="mt-0.5 text-xs text-ink-500">{exp.length} payments</p>
          </div>
          <div className="md:pl-6">
            <p className="text-xs text-ink-500">Remaining</p>
            <p className={`tnum mt-1 text-2xl font-semibold ${remaining < 0 ? 'text-danger-700' : 'text-success-700'}`}>{formatRWF(remaining)}</p>
            <p className="mt-0.5 text-xs text-ink-500">{daysFromToday(wedding.date)} days until the wedding</p>
          </div>
        </div>
        <ProgressBar value={pct} size="md" className="mt-6" label="Budget spent" />
        <p className="mt-2 text-xs text-ink-500">{pct}% of the planned budget has been spent</p>
      </section>

      <div className="mb-6 grid gap-6 lg:grid-cols-5">
        <Card
          title="Planned vs actual"
          className="lg:col-span-3"
          action={
          <div className="flex items-center gap-4 text-xs text-ink-500">
              <span className="flex items-center gap-1.5"><span className="h-2.5 w-2.5 rounded-sm bg-gold-200" aria-hidden />Planned</span>
              <span className="flex items-center gap-1.5"><span className="h-2.5 w-2.5 rounded-sm bg-wine-700" aria-hidden />Actual</span>
            </div>
          }>
          
          <BudgetChart data={rows} />
        </Card>

        <Card title="Recent expenses" className="lg:col-span-2" bodyClassName="px-5">
          <ul className="divide-y divide-line">
            {recent.map((e) => {
              const Icon = iconFor(e.category);
              return (
                <li key={e.id} className="flex items-center gap-3 py-3">
                  <span className="flex h-8 w-8 shrink-0 items-center justify-center rounded-md bg-ivory-100 text-ink-600">
                    <Icon className="h-4 w-4" aria-hidden />
                  </span>
                  <div className="min-w-0 flex-1">
                    <p className="truncate text-sm text-ink">{e.title}</p>
                    <div className="mt-0.5 flex flex-wrap items-center gap-x-2 gap-y-1 text-xs text-ink-500">
                      <span>{formatDate(e.date, 'd MMM')}</span>
                      <span>· {people[e.paidBy].firstName} · {e.method}</span>
                      <ScopeBadge scope={e.scope} />
                    </div>
                  </div>
                  <p className="tnum shrink-0 text-sm font-medium text-ink">{formatRWF(e.amount)}</p>
                </li>);

            })}
          </ul>
        </Card>
      </div>

      <Card title="Category breakdown" bodyClassName="p-0">
        <div className="hidden grid-cols-[minmax(0,1.3fr)_minmax(0,1.5fr)_130px_130px_130px] gap-4 border-b border-line bg-ivory px-5 py-3 text-xs font-medium text-ink-500 md:grid">
          <span>Category</span>
          <span>Used</span>
          <span className="text-right">Planned</span>
          <span className="text-right">Spent</span>
          <span className="text-right">Remaining</span>
        </div>
        <ul className="divide-y divide-line">
          {rows.map((r) => {
            const Icon = iconFor(r.category);
            const used = r.planned ? r.spent / r.planned * 100 : 0;
            const left = r.planned - r.spent;
            return (
              <li key={r.category} className="grid grid-cols-2 gap-x-4 gap-y-2 px-5 py-3.5 md:grid-cols-[minmax(0,1.3fr)_minmax(0,1.5fr)_130px_130px_130px] md:items-center">
                <span className="flex items-center gap-2.5 text-sm font-medium text-ink">
                  <Icon className="h-4 w-4 text-gold-600" aria-hidden />
                  {r.category}
                </span>
                <span className="flex items-center gap-3 md:order-none">
                  <ProgressBar value={used} tone={used > 100 ? 'danger' : used > 85 ? 'warning' : 'wine'} label={`${r.category} used`} />
                  <span className="tnum w-10 text-right text-xs text-ink-500">{Math.round(used)}%</span>
                </span>
                <span className="tnum text-xs text-ink-500 md:text-right md:text-sm md:text-ink-700"><span className="md:hidden">Planned </span>{formatRWF(r.planned)}</span>
                <span className="tnum text-right text-xs text-ink-500 md:text-sm md:text-ink-700"><span className="md:hidden">Spent </span>{formatRWF(r.spent)}</span>
                <span className={`tnum col-span-2 text-xs font-medium md:col-span-1 md:text-right md:text-sm ${left < 0 ? 'text-danger-700' : 'text-ink'}`}>
                  <span className="font-normal text-ink-500 md:hidden">Remaining </span>
                  {formatRWF(left)}
                </span>
              </li>);

          })}
        </ul>
      </Card>
    </div>);

}