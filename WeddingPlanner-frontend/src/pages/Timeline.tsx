import React from 'react';
import { CheckIcon, MapPinIcon } from 'lucide-react';
import { useRole } from '../contexts/RoleContext';
import { milestones, weddingDaySchedule } from '../data/timeline';
import { daysFromToday, formatDate } from '../utils/format';
import { PageHeader } from '../components/ui/PageHeader';
import { Card } from '../components/ui/Card';
import { ScopeBadge } from '../components/ui/ScopeBadge';
import { Badge } from '../components/ui/Badge';

export function Timeline() {
  const { canSee, isSupport } = useRole();
  const items = milestones.filter((m) => canSee(m.scope) || isSupport && m.scope === 'shared').sort((a, b) => a.date.localeCompare(b.date));
  const nextId = items.find((m) => daysFromToday(m.date) >= 0)?.id;
  const doneCount = items.filter((m) => daysFromToday(m.date) < 0).length;

  return (
    <div>
      <PageHeader title="Timeline" description="Key milestones from the first family introduction to the wedding day." />

      <div className="grid gap-6 xl:grid-cols-[minmax(0,1fr)_380px]">
        <Card title="Milestones" description={`${doneCount} of ${items.length} behind you`}>
          <ol className="relative">
            {items.map((m, i) => {
              const d = daysFromToday(m.date);
              const done = d < 0;
              const isNext = m.id === nextId;
              const last = i === items.length - 1;
              return (
                <li key={m.id} className="relative grid grid-cols-[64px_24px_minmax(0,1fr)] gap-x-3 pb-7 last:pb-0 sm:grid-cols-[88px_24px_minmax(0,1fr)] sm:gap-x-4">
                  <div className="pt-0.5 text-right">
                    <p className={`tnum text-sm font-medium ${done ? 'text-ink-500' : 'text-ink'}`}>{formatDate(m.date, 'd MMM')}</p>
                    <p className="text-[11px] text-ink-500">{formatDate(m.date, 'yyyy')}</p>
                  </div>
                  <div className="relative flex justify-center">
                    {!last && <span className={`absolute top-6 h-[calc(100%-4px)] w-px ${done ? 'bg-wine-300' : 'bg-line-strong'}`} aria-hidden />}
                    <span
                      className={`relative z-10 mt-0.5 flex h-5 w-5 items-center justify-center rounded-full border-2 ${
                      done ? 'border-wine-700 bg-wine-700 text-white' : isNext ? 'border-gold-500 bg-gold-50' : 'border-line-strong bg-white'}`
                      }>
                      
                      {done && <CheckIcon className="h-3 w-3" strokeWidth={3} aria-hidden />}
                      {isNext && <span className="h-1.5 w-1.5 rounded-full bg-gold-500" aria-hidden />}
                    </span>
                  </div>
                  <div className={`min-w-0 ${isNext ? '-mt-2 rounded-md border border-gold-200 bg-gold-50/60 p-3' : ''}`}>
                    <div className="flex flex-wrap items-center gap-2">
                      <h3 className={`text-sm font-semibold ${done ? 'text-ink-600' : 'text-ink'} ${m.id === 'm13' ? 'font-serif text-lg' : ''}`}>{m.title}</h3>
                      {isNext && <Badge tone="gold">Next · in {d} days</Badge>}
                    </div>
                    <p className="mt-1 text-sm text-ink-500">{m.description}</p>
                    <div className="mt-2 flex flex-wrap items-center gap-2 text-xs text-ink-500">
                      <span>{m.kind}</span>
                      {m.location &&
                      <span className="inline-flex items-center gap-1">
                          <MapPinIcon className="h-3 w-3" aria-hidden />
                          {m.location}
                        </span>
                      }
                      <ScopeBadge scope={m.scope} />
                    </div>
                  </div>
                </li>);

            })}
          </ol>
        </Card>

        <Card title="Wedding day" description={formatDate('2027-08-24', 'EEEE, d MMMM yyyy')} className="self-start" bodyClassName="px-5 py-2">
          <ol className="divide-y divide-line">
            {weddingDaySchedule.map((s) =>
            <li key={s.time} className="grid grid-cols-[52px_minmax(0,1fr)] gap-3 py-3">
                <span className="tnum pt-0.5 text-sm font-semibold text-wine-700">{s.time}</span>
                <div className="min-w-0">
                  <p className="text-sm font-medium text-ink">{s.title}</p>
                  <p className="text-xs text-ink-500">
                    {s.location} · {s.owner}
                  </p>
                </div>
              </li>
            )}
          </ol>
        </Card>
      </div>
    </div>);

}