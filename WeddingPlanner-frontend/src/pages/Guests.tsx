import React, { useState } from 'react';
import { SearchIcon, UserPlusIcon, UsersIcon, ChevronRightIcon } from 'lucide-react';
import { GuestSide, RsvpStatus } from '../types/wedding';
import { useRole } from '../contexts/RoleContext';
import { useQuickAdd } from '../contexts/QuickAddContext';
import { useGuestStats } from '../hooks/useGuestStats';
import { seatingTables } from '../data/guests';
import { rsvpMeta } from '../utils/status';
import { inputClass } from '../utils/ui';
import { PageHeader } from '../components/ui/PageHeader';
import { Button } from '../components/ui/Button';
import { Badge } from '../components/ui/Badge';
import { Segmented } from '../components/ui/Segmented';
import { Select } from '../components/ui/Select';
import { EmptyState } from '../components/ui/EmptyState';
import { GuestDrawer } from '../components/guests/GuestDrawer';

type SideFilter = 'all' | GuestSide;
const sideTone = { bride: 'bride', groom: 'groom', both: 'wine' } as const;
const sideShort = { bride: 'Bride', groom: 'Groom', both: 'Both' };
const cols = 'md:grid-cols-[minmax(0,1.6fr)_90px_minmax(0,1fr)_120px_minmax(0,1fr)_24px]';

export function Guests() {
  const { isSupport, side } = useRole();
  const { openGuest } = useQuickAdd();
  const stats = useGuestStats();
  const [query, setQuery] = useState('');
  const [sideFilter, setSideFilter] = useState<SideFilter>('all');
  const [rsvp, setRsvp] = useState<'all' | RsvpStatus>('all');
  const [selected, setSelected] = useState<string | null>(null);

  const list = stats.list.
  filter((g) => sideFilter === 'all' ? true : g.side === sideFilter).
  filter((g) => rsvp === 'all' ? true : g.rsvp === rsvp).
  filter((g) => g.name.toLowerCase().includes(query.toLowerCase()) || g.phone.includes(query));

  const tableName = (id: number | null) => id ? seatingTables.find((t) => t.id === id)?.name ?? `Table ${id}` : null;

  const summary = [
  { label: 'Total guests', value: stats.total, tone: 'text-ink' },
  { label: 'Confirmed', value: stats.confirmed, tone: 'text-success-700' },
  { label: 'Pending', value: stats.pending, tone: 'text-warning-700' },
  { label: 'Declined', value: stats.declined, tone: 'text-danger-700' }];


  return (
    <div>
      <PageHeader
        title="Guests & RSVP"
        description={isSupport ? `Guests from the ${side}'s family and friends, plus mutual guests.` : 'Both families’ guest lists with live RSVP responses.'}
        actions={<Button icon={UserPlusIcon} onClick={openGuest}>Add guest</Button>} />
      

      <section aria-label="RSVP summary" className="mb-6 grid grid-cols-2 gap-px overflow-hidden rounded-lg border border-line bg-line shadow-card md:grid-cols-4">
        {summary.map((s) =>
        <div key={s.label} className="bg-white p-5">
            <p className="text-xs text-ink-500">{s.label}</p>
            <p className={`tnum mt-1 text-2xl font-semibold ${s.tone}`}>{s.value}</p>
            {s.label !== 'Total guests' && <p className="tnum mt-0.5 text-xs text-ink-500">{Math.round(s.value / stats.total * 100)}% of list</p>}
          </div>
        )}
      </section>

      <div className="mb-4 flex flex-col gap-2 lg:flex-row lg:items-center">
        <div className="relative flex-1">
          <SearchIcon className="pointer-events-none absolute left-3 top-1/2 h-4 w-4 -translate-y-1/2 text-ink-400" aria-hidden />
          <input value={query} onChange={(e) => setQuery(e.target.value)} placeholder="Search by name or phone" aria-label="Search guests" className={`${inputClass} pl-9`} />
        </div>
        <div className="flex flex-wrap gap-2">
          {isSupport ?
          <Segmented<SideFilter>
            ariaLabel="Filter by side"
            value={sideFilter}
            onChange={setSideFilter}
            options={[{ value: 'all', label: 'All I can see' }, { value: side, label: side === 'bride' ? 'Bride side' : 'Groom side' }, { value: 'both', label: 'Mutual' }]} /> :


          <Segmented<SideFilter>
            ariaLabel="Filter by side"
            value={sideFilter}
            onChange={setSideFilter}
            options={[{ value: 'all', label: 'All' }, { value: 'bride', label: 'Bride side' }, { value: 'groom', label: 'Groom side' }, { value: 'both', label: 'Mutual' }]} />

          }
          <Select<'all' | RsvpStatus>
            ariaLabel="Filter by RSVP"
            value={rsvp}
            onChange={setRsvp}
            className="w-40"
            options={[{ value: 'all', label: 'Any RSVP' }, ...(Object.keys(rsvpMeta) as RsvpStatus[]).map((r) => ({ value: r, label: rsvpMeta[r].label }))]} />
          
        </div>
      </div>

      <section aria-label="Guest list" className="overflow-hidden rounded-lg border border-line bg-white shadow-card">
        <div className={`hidden gap-4 border-b border-line bg-ivory px-5 py-3 text-xs font-medium text-ink-500 md:grid ${cols}`}>
          <span>Guest</span>
          <span>Side</span>
          <span>Phone</span>
          <span>RSVP</span>
          <span>Table</span>
          <span className="sr-only">Open</span>
        </div>
        {list.length === 0 ?
        <EmptyState icon={UsersIcon} title="No guests found" description="Try another name or clear the filters." /> :

        <ul className="divide-y divide-line">
            {list.map((g) => {
            const meta = rsvpMeta[g.rsvp];
            const table = tableName(g.table);
            return (
              <li key={g.id}>
                  <button
                  type="button"
                  onClick={() => setSelected(g.id)}
                  className={`grid w-full grid-cols-[minmax(0,1fr)_auto] items-center gap-x-4 gap-y-1.5 px-5 py-3.5 text-left transition-colors duration-150 hover:bg-ivory/60 ${cols}`}>
                  
                    <span className="min-w-0">
                      <span className="block truncate text-sm font-medium text-ink">{g.name}</span>
                      <span className="block text-xs text-ink-500">
                        {g.group}
                        {g.plusOnes > 0 && ` · +${g.plusOnes}`}
                      </span>
                    </span>
                    <span className="justify-self-end md:justify-self-start">
                      <Badge tone={sideTone[g.side]}>{sideShort[g.side]}</Badge>
                    </span>
                    <span className="tnum hidden text-sm text-ink-600 md:block">{g.phone}</span>
                    <span>
                      <Badge tone={meta.tone} dot>{meta.label}</Badge>
                    </span>
                    <span className={`justify-self-end text-sm md:justify-self-start ${table ? 'text-ink-700' : 'text-ink-500'}`}>
                      {table ?? (g.rsvp === 'declined' ? '—' : 'Unassigned')}
                    </span>
                    <ChevronRightIcon className="hidden h-4 w-4 text-ink-300 md:block" aria-hidden />
                  </button>
                </li>);

          })}
          </ul>
        }
        <div className="border-t border-line px-5 py-3 text-xs text-ink-500">
          Showing {list.length} of {stats.total} guests
        </div>
      </section>

      <GuestDrawer guestId={selected} onClose={() => setSelected(null)} />
    </div>);

}