import React from 'react';
import { Link, useNavigate } from 'react-router-dom';
import {
  ArmchairIcon,
  ArrowRightIcon,
  ListChecksIcon,
  LockIcon,
  ReceiptIcon,
  SendIcon,
  UserPlusIcon,
  UsersIcon,
  WalletIcon } from
'lucide-react';
import { useRole } from '../contexts/RoleContext';
import { useQuickAdd } from '../contexts/QuickAddContext';
import { useVisibleData } from '../hooks/useVisibleData';
import { useGuestStats } from '../hooks/useGuestStats';
import { milestones } from '../data/timeline';
import { wedding } from '../data/wedding';
import { aggregateByCategory } from '../utils/budget';
import { daysFromToday, formatCompact, formatDate, formatRWF } from '../utils/format';
import { iconFor } from '../utils/ui';
import { Card } from '../components/ui/Card';
import { ProgressBar } from '../components/ui/ProgressBar';
import { ScopeBadge } from '../components/ui/ScopeBadge';
import { Avatar } from '../components/ui/Avatar';
import { StatGroup } from '../components/overview/StatGroup';
import { TaskCheckRow } from '../components/tasks/TaskCheckRow';

export function Overview() {
  const { person, isSupport, side, canSee } = useRole();
  const { tasks, expenses, budgetLines, planned, spent, completedCount, progress } = useVisibleData();
  const guests = useGuestStats();
  const { openTask, openExpense, openGuest } = useQuickAdd();
  const navigate = useNavigate();

  const openTasks = tasks.filter((t) => t.status !== 'completed').sort((a, b) => a.due.localeCompare(b.due));
  const overdue = openTasks.filter((t) => t.status === 'overdue').length;
  const upcomingMilestones = milestones.
  filter((m) => canSee(m.scope) && daysFromToday(m.date) >= 0).
  sort((a, b) => a.date.localeCompare(b.date)).
  slice(0, 4);
  const next = upcomingMilestones[0];
  const recent = [...expenses].sort((a, b) => b.date.localeCompare(a.date)).slice(0, 5);
  const topCategories = aggregateByCategory(budgetLines, expenses).slice(0, 5);
  const days = daysFromToday(wedding.date);
  const spentPct = planned ? Math.round(spent / planned * 100) : 0;

  const quickActions = [
  { label: 'Add a task', icon: ListChecksIcon, onClick: () => openTask() },
  { label: 'Log an expense', icon: ReceiptIcon, onClick: openExpense },
  { label: 'Add a guest', icon: UserPlusIcon, onClick: openGuest },
  ...(isSupport ?
  [] :
  [
  { label: 'Send invitations', icon: SendIcon, onClick: () => navigate('/invitations') },
  { label: 'Arrange seating', icon: ArmchairIcon, onClick: () => navigate('/seating') }])];



  const rsvpSegments = [
  { label: 'Confirmed', value: guests.confirmed, cls: 'bg-success-600' },
  { label: 'Pending', value: guests.pending, cls: 'bg-gold-300' },
  { label: 'Declined', value: guests.declined, cls: 'bg-danger-600' }];


  return (
    <div className="space-y-6">
      <div className="flex flex-col gap-1">
        <h1 className="font-serif text-[28px] font-semibold text-ink sm:text-[32px]">Muraho, {person.firstName}</h1>
        <p className="text-sm text-ink-500">
          {isSupport ?
          <span className="inline-flex flex-wrap items-center gap-1.5">
              <LockIcon className="h-3.5 w-3.5" aria-hidden />
              You’re seeing {side === 'bride' ? 'bride' : 'groom'}-side planning only. The couple’s private and shared plans stay hidden.
            </span> :

          'Here’s where the wedding stands today.'
          }
        </p>
      </div>

      <div className="grid gap-6 lg:grid-cols-3">
        <section aria-label="Planning progress" className="rounded-lg bg-wine-800 p-6 text-ivory lg:col-span-2 lg:p-8">
          <div className="flex flex-col gap-8 md:flex-row md:items-end md:justify-between">
            <div>
              <p className="text-sm text-wine-200">{isSupport ? `${side === 'bride' ? 'Bride' : 'Groom'}-side progress` : 'Wedding planning'}</p>
              <p className="tnum mt-2 font-serif text-6xl font-semibold leading-none text-gold-300">{progress}%</p>
              <p className="mt-2 text-sm text-wine-100">completed · {completedCount} of {tasks.length} tasks done</p>
            </div>
            <dl className="grid grid-cols-3 gap-6 md:gap-8">
              <div>
                <dt className="text-xs text-wine-200">Days to go</dt>
                <dd className="tnum mt-1 text-2xl font-semibold">{days}</dd>
              </div>
              <div>
                <dt className="text-xs text-wine-200">Open tasks</dt>
                <dd className="tnum mt-1 text-2xl font-semibold">{openTasks.length}</dd>
              </div>
              <div>
                <dt className="text-xs text-wine-200">Overdue</dt>
                <dd className={`tnum mt-1 text-2xl font-semibold ${overdue ? 'text-gold-300' : ''}`}>{overdue}</dd>
              </div>
            </dl>
          </div>
          <div className="mt-8 h-2 overflow-hidden rounded-full bg-white/15" role="progressbar" aria-valuenow={progress} aria-valuemin={0} aria-valuemax={100} aria-label="Planning progress">
            <div className="h-full rounded-full bg-gold-300" style={{ width: `${progress}%` }} />
          </div>
          {next &&
          <p className="mt-4 text-sm text-wine-100">
              Next up: <span className="font-medium text-ivory">{next.title}</span> · {formatDate(next.date, 'd MMMM')}
            </p>
          }
        </section>

        <Card title="Quick actions" bodyClassName="p-2">
          <ul>
            {quickActions.map((a) =>
            <li key={a.label}>
                <button
                type="button"
                onClick={a.onClick}
                className="group flex w-full items-center gap-3 rounded-md px-3 py-2.5 text-left text-sm text-ink transition-colors duration-150 hover:bg-ivory">
                
                  <span className="flex h-8 w-8 items-center justify-center rounded-md bg-wine-50 text-wine-700">
                    <a.icon className="h-4 w-4" aria-hidden />
                  </span>
                  <span className="flex-1">{a.label}</span>
                  <ArrowRightIcon className="h-4 w-4 text-ink-300 transition-transform duration-150 group-hover:translate-x-0.5 group-hover:text-ink-500" aria-hidden />
                </button>
              </li>
            )}
          </ul>
        </Card>
      </div>

      <section aria-label="Key numbers" className="grid divide-y divide-line rounded-lg border border-line bg-white shadow-card md:grid-cols-3 md:divide-x md:divide-y-0">
        <StatGroup
          icon={ListChecksIcon}
          title="Tasks"
          to="/tasks"
          stats={[
          { label: 'Completed', value: `${completedCount}` },
          { label: 'Remaining', value: `${openTasks.length}`, note: overdue ? `${overdue} overdue` : 'None overdue' }]
          } />
        
        <StatGroup
          icon={WalletIcon}
          title="Budget"
          to="/budget"
          stats={[
          { label: 'Total budget', value: `RWF ${formatCompact(planned)}` },
          { label: 'Amount spent', value: `RWF ${formatCompact(spent)}`, note: `${spentPct}% of budget`, emphasis: true }]
          } />
        
        <StatGroup
          icon={UsersIcon}
          title="Guests"
          to="/guests"
          stats={[
          { label: 'Confirmed', value: `${guests.confirmed}` },
          { label: 'Pending', value: `${guests.pending}`, note: `of ${guests.total} invited` }]
          } />
        
      </section>

      <div className="grid gap-6 lg:grid-cols-3">
        <Card
          title="Upcoming tasks"
          description="Sorted by due date"
          className="lg:col-span-2"
          bodyClassName="px-5"
          action={
          <Link to="/tasks" className="text-sm font-medium text-wine-700 hover:text-wine-800">
              View all
            </Link>
          }>
          
          <ul className="divide-y divide-line">
            {openTasks.slice(0, 5).map((t) =>
            <TaskCheckRow key={t.id} task={t} />
            )}
          </ul>
        </Card>

        <Card title="Upcoming deadlines" action={<Link to="/timeline" className="text-sm font-medium text-wine-700 hover:text-wine-800">Timeline</Link>}>
          <ol className="space-y-4">
            {upcomingMilestones.map((m) =>
            <li key={m.id} className="flex gap-3">
                <div className="w-11 shrink-0 rounded-md border border-line bg-ivory py-1.5 text-center">
                  <p className="tnum font-serif text-lg font-semibold leading-none text-ink">{formatDate(m.date, 'd')}</p>
                  <p className="mt-0.5 text-[10px] font-medium uppercase text-ink-500">{formatDate(m.date, 'MMM')}</p>
                </div>
                <div className="min-w-0">
                  <p className="text-sm font-medium text-ink">{m.title}</p>
                  <p className="mt-0.5 text-xs text-ink-500">In {daysFromToday(m.date)} days · {m.kind}</p>
                </div>
              </li>
            )}
          </ol>
        </Card>
      </div>

      <div className="grid gap-6 lg:grid-cols-3">
        <Card title="Budget summary" action={<Link to="/budget" className="text-sm font-medium text-wine-700 hover:text-wine-800">Details</Link>}>
          <div className="flex items-baseline justify-between">
            <p className="tnum text-xl font-semibold text-ink">{formatRWF(planned - spent)}</p>
            <p className="text-xs text-ink-500">remaining</p>
          </div>
          <ProgressBar value={spentPct} size="md" className="mt-3" label="Budget spent" />
          <ul className="mt-5 space-y-3">
            {topCategories.map((c) => {
              const Icon = iconFor(c.category);
              return (
                <li key={c.category}>
                  <div className="flex items-center justify-between text-sm">
                    <span className="flex items-center gap-2 text-ink-700">
                      <Icon className="h-3.5 w-3.5 text-ink-400" aria-hidden />
                      {c.category}
                    </span>
                    <span className="tnum text-xs text-ink-500">
                      {formatCompact(c.spent)} / {formatCompact(c.planned)}
                    </span>
                  </div>
                  <ProgressBar value={c.planned ? c.spent / c.planned * 100 : 0} tone="gold" className="mt-1.5" label={`${c.category} spent`} />
                </li>);

            })}
          </ul>
        </Card>

        <Card title="Guests & RSVP" description={isSupport ? `${side === 'bride' ? 'Bride' : 'Groom'}-side & mutual guests` : 'Both families'} action={<Link to="/guests" className="text-sm font-medium text-wine-700 hover:text-wine-800">Guest list</Link>}>
          <p className="tnum text-4xl font-semibold text-ink">{guests.total}</p>
          <p className="text-xs text-ink-500">guests invited</p>
          <div className="mt-5 flex h-2.5 overflow-hidden rounded-full bg-ivory-200">
            {rsvpSegments.map((s) =>
            <div key={s.label} className={s.cls} style={{ width: `${s.value / guests.total * 100}%` }} />
            )}
          </div>
          <dl className="mt-5 space-y-2.5">
            {rsvpSegments.map((s) =>
            <div key={s.label} className="flex items-center justify-between text-sm">
                <dt className="flex items-center gap-2 text-ink-700">
                  <span className={`h-2 w-2 rounded-full ${s.cls}`} aria-hidden />
                  {s.label}
                </dt>
                <dd className="tnum font-medium text-ink">
                  {s.value} <span className="font-normal text-ink-500">· {Math.round(s.value / guests.total * 100)}%</span>
                </dd>
              </div>
            )}
          </dl>
          <p className="mt-5 border-t border-line pt-4 text-xs text-ink-500">RSVP deadline: {formatDate(wedding.rsvpBy, 'd MMMM')}</p>
        </Card>

        <Card title="Recent expenses" bodyClassName="px-5" action={<Link to="/budget" className="text-sm font-medium text-wine-700 hover:text-wine-800">All</Link>}>
          <ul className="divide-y divide-line">
            {recent.map((e) =>
            <li key={e.id} className="flex items-center gap-3 py-3">
                <Avatar role={e.paidBy} size="xs" />
                <div className="min-w-0 flex-1">
                  <p className="truncate text-sm text-ink">{e.title}</p>
                  <div className="mt-0.5 flex items-center gap-2 text-xs text-ink-500">
                    <span>{formatDate(e.date, 'd MMM')}</span>
                    <ScopeBadge scope={e.scope} />
                  </div>
                </div>
                <p className="tnum shrink-0 text-sm font-medium text-ink">{formatCompact(e.amount)}</p>
              </li>
            )}
          </ul>
        </Card>
      </div>
    </div>);

}