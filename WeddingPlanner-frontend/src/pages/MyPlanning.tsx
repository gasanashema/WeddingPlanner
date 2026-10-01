import React, { useState } from 'react';
import { Link } from 'react-router-dom';
import { CheckIcon, HouseIcon, LockIcon, PlusIcon } from 'lucide-react';
import { Role, Scope } from '../types/wedding';
import { useRole } from '../contexts/RoleContext';
import { useQuickAdd } from '../contexts/QuickAddContext';
import { useWeddingData } from '../contexts/WeddingDataContext';
import { useVisibleData } from '../hooks/useVisibleData';
import { people, roles } from '../data/wedding';
import { privateScopeOf, scopeAudience, sideScopeOf } from '../utils/permissions';
import { formatRWF } from '../utils/format';
import { Card } from '../components/ui/Card';
import { Button } from '../components/ui/Button';
import { Tabs } from '../components/ui/Tabs';
import { ProgressBar } from '../components/ui/ProgressBar';
import { Avatar } from '../components/ui/Avatar';
import { EmptyState } from '../components/ui/EmptyState';
import { VisibilityNote } from '../components/ui/VisibilityNote';
import { TaskCheckRow } from '../components/tasks/TaskCheckRow';

import { useScreenInit } from '../useScreenInit.js';

type Area = 'private' | 'side';

export function MyPlanning() {
  const { role, person, side, isSupport } = useRole();
  const { tasks, expenses, budgetLines } = useVisibleData();
  const { homeItems } = useWeddingData();
  const { openTask } = useQuickAdd();
  const screenInit = useScreenInit();
  const [area, setArea] = useState<Area>(isSupport ? 'side' : screenInit?.area as Area | undefined ?? 'private');

  const scope: Scope = area === 'private' && !isSupport ? privateScopeOf(side) : sideScopeOf(side);
  const scopeTasks = tasks.filter((t) => t.scope === scope).sort((a, b) => Number(a.status === 'completed') - Number(b.status === 'completed') || a.due.localeCompare(b.due));
  const done = scopeTasks.filter((t) => t.status === 'completed').length;
  const planned = budgetLines.filter((l) => l.scope === scope).reduce((s, l) => s + l.planned, 0);
  const spent = expenses.filter((e) => e.scope === scope).reduce((s, e) => s + e.amount, 0);
  const sideItems = homeItems.filter((h) => h.side === side);
  const sideLabel = side === 'bride' ? 'Bride Side' : 'Groom Side';
  const supporter = people[side === 'bride' ? 'bride-support' : 'groom-support'];
  const audience = scopeAudience[scope];

  return (
    <div className="space-y-6">
      <section
        aria-label="Planning side"
        className={`relative overflow-hidden rounded-lg border bg-white p-6 shadow-card lg:p-8 ${side === 'bride' ? 'border-bride-200' : 'border-groom-200'}`}>
        
        <span className={`absolute inset-y-0 left-0 w-1 ${side === 'bride' ? 'bg-bride-600' : 'bg-groom-600'}`} aria-hidden />
        <div className="flex flex-col gap-5 md:flex-row md:items-center md:justify-between">
          <div>
            <p className={`text-sm font-medium ${side === 'bride' ? 'text-bride-700' : 'text-groom-700'}`}>You are working in</p>
            <h1 className="mt-1 font-serif text-[32px] font-semibold leading-tight text-ink">{sideLabel}</h1>
            <p className="mt-1 text-sm text-ink-500">
              Planning as {person.name} · {person.roleLabel}
            </p>
          </div>
          <div className="flex items-center gap-3">
            <div className="flex -space-x-2">
              <Avatar role={side} size="md" />
              <Avatar role={side === 'bride' ? 'bride-support' : 'groom-support'} size="md" />
            </div>
            <div className="text-sm">
              <p className="font-medium text-ink">{people[side].firstName} & {supporter.firstName}</p>
              <p className="text-xs text-ink-500">{supporter.relation}</p>
            </div>
          </div>
        </div>
      </section>

      {!isSupport &&
      <Tabs<Area>
        id="my-planning"
        value={area}
        onChange={setArea}
        items={[
        { value: 'private', label: 'Private · only you', count: tasks.filter((t) => t.scope === privateScopeOf(side)).length },
        { value: 'side', label: `${side === 'bride' ? 'Bride' : 'Groom'}-side · you & ${supporter.firstName}`, count: tasks.filter((t) => t.scope === sideScopeOf(side)).length }]
        } />

      }

      <div className="flex flex-col gap-3 sm:flex-row sm:items-center sm:justify-between">
        <VisibilityNote scope={scope} />
        <Button icon={PlusIcon} onClick={() => openTask(scope)}>
          Add to {area === 'private' && !isSupport ? 'private list' : `${side}-side list`}
        </Button>
      </div>

      <div className="grid gap-6 lg:grid-cols-3">
        <Card title="Checklist" description={`${done} of ${scopeTasks.length} done`} className="lg:col-span-2" bodyClassName="px-5">
          {scopeTasks.length ?
          <ul className="divide-y divide-line">
              {scopeTasks.map((t) =>
            <TaskCheckRow key={t.id} task={t} showScope={false} />
            )}
            </ul> :

          <EmptyState
            icon={CheckIcon}
            title="Nothing here yet"
            description="Add the first item to this list. Only the people shown above will see it."
            action={<Button icon={PlusIcon} onClick={() => openTask(scope)}>Add item</Button>} />

          }
        </Card>

        <div className="space-y-6">
          <Card title="Who can see this">
            <ul className="space-y-2.5">
              {roles.map((r: Role) => {
                const allowed = audience.includes(r);
                return (
                  <li key={r} className={`flex items-center gap-3 ${allowed ? '' : 'opacity-70'}`}>
                    <Avatar role={r} size="xs" />
                    <div className="min-w-0 flex-1">
                      <p className="truncate text-sm text-ink">
                        {people[r].firstName}
                        {r === role && ' (you)'}
                      </p>
                      <p className="truncate text-[11px] text-ink-500">{people[r].roleLabel}</p>
                    </div>
                    {allowed ?
                    <span className="flex items-center gap-1 text-xs font-medium text-success-700">
                        <CheckIcon className="h-3.5 w-3.5" aria-hidden /> Can see
                      </span> :

                    <span className="flex items-center gap-1 text-xs text-ink-500">
                        <LockIcon className="h-3.5 w-3.5" aria-hidden /> Hidden
                      </span>
                    }
                  </li>);

              })}
            </ul>
          </Card>

          <Card title="Budget for this list">
            <div className="flex items-baseline justify-between">
              <p className="tnum text-xl font-semibold text-ink">{formatRWF(spent)}</p>
              <p className="tnum text-xs text-ink-500">of {formatRWF(planned)}</p>
            </div>
            <ProgressBar value={planned ? spent / planned * 100 : 0} size="md" className="mt-3" label="Budget spent" tone={spent > planned ? 'danger' : 'wine'} />
            <p className="mt-2 text-xs text-ink-500">{planned - spent >= 0 ? `${formatRWF(planned - spent)} left` : `${formatRWF(spent - planned)} over budget`}</p>
          </Card>

          {area === 'side' &&
          <Link
            to="/home-preparation"
            className="flex items-center gap-3 rounded-lg border border-line bg-white p-4 shadow-card transition-colors duration-150 hover:bg-ivory">
            
              <span className="flex h-10 w-10 items-center justify-center rounded-md bg-gold-50 text-gold-700">
                <HouseIcon className="h-5 w-5" aria-hidden />
              </span>
              <span className="min-w-0 flex-1">
                <span className="block text-sm font-medium text-ink">Home preparation</span>
                <span className="block text-xs text-ink-500">
                  {sideItems.filter((h) => h.completed).length} of {sideItems.length} items ready
                </span>
              </span>
            </Link>
          }
        </div>
      </div>
    </div>);

}