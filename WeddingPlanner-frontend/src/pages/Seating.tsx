import React, { useState } from 'react';
import { toast } from 'sonner';
import { ArmchairIcon, PlusIcon, XIcon } from 'lucide-react';
import { Guest } from '../types/wedding';
import { useWeddingData } from '../contexts/WeddingDataContext';
import { seatingTables } from '../data/guests';
import { rsvpMeta } from '../utils/status';
import { PageHeader } from '../components/ui/PageHeader';
import { Card } from '../components/ui/Card';
import { Badge } from '../components/ui/Badge';
import { EmptyState } from '../components/ui/EmptyState';
import { SeatingTable } from '../components/seating/SeatingTable';

const seatsFor = (g: Guest) => 1 + g.plusOnes;

export function Seating() {
  const { guests, updateGuest } = useWeddingData();
  const [selectedId, setSelectedId] = useState<number>(2);

  const usedFor = (tableId: number) => {
    const t = seatingTables.find((x) => x.id === tableId);
    return (t?.reserved ?? 0) + guests.filter((g) => g.table === tableId).reduce((s, g) => s + seatsFor(g), 0);
  };

  const selected = seatingTables.find((t) => t.id === selectedId) ?? seatingTables[0];
  const seated = guests.filter((g) => g.table === selected.id);
  const used = usedFor(selected.id);
  const free = selected.capacity - used;
  const unassigned = guests.filter((g) => g.table === null && g.rsvp !== 'declined').sort((a, b) => (a.rsvp === 'confirmed' ? -1 : 1) - (b.rsvp === 'confirmed' ? -1 : 1));
  const totalCapacity = seatingTables.reduce((s, t) => s + t.capacity, 0);
  const totalUsed = seatingTables.reduce((s, t) => s + usedFor(t.id), 0);

  const assign = (g: Guest) => {
    if (seatsFor(g) > free) {
      toast.error(`${selected.name} doesn’t have ${seatsFor(g)} free seats`);
      return;
    }
    updateGuest(g.id, { table: selected.id });
    toast.success(`${g.name} seated at ${selected.name}`);
  };

  return (
    <div>
      <PageHeader title="Seating" description="Reception layout at Intare Gardens. Select a table to see and change who sits there." scope="shared" />

      <div className="grid gap-6 xl:grid-cols-[minmax(0,1fr)_360px]">
        <Card
          title="Floor plan"
          description={`${totalUsed} of ${totalCapacity} seats filled · ${seatingTables.length} tables`}
          action={
          <div className="flex items-center gap-4 text-xs text-ink-500">
              <span className="flex items-center gap-1.5"><span className="h-2.5 w-2.5 rounded-full bg-wine-700" aria-hidden />Taken</span>
              <span className="flex items-center gap-1.5"><span className="h-2.5 w-2.5 rounded-full border border-line-strong bg-white" aria-hidden />Free</span>
            </div>
          }
          bodyClassName="p-4 bg-ivory/50">
          
          <div className="mb-4 rounded-md border border-dashed border-gold-300 bg-gold-50 py-2 text-center text-xs font-medium text-gold-700">Stage & dance floor</div>
          <div className="grid grid-cols-2 gap-2 sm:grid-cols-3 lg:grid-cols-4">
            {seatingTables.map((t) =>
            <SeatingTable key={t.id} table={t} used={usedFor(t.id)} selected={t.id === selected.id} onSelect={() => setSelectedId(t.id)} />
            )}
          </div>
        </Card>

        <div className="space-y-6">
          <Card title={selected.name} description={selected.note} meta={<Badge tone={free <= 0 ? 'wine' : 'success'}>{free <= 0 ? 'Full' : `${free} free`}</Badge>} bodyClassName="px-5 py-2">
            {seated.length ?
            <ul className="divide-y divide-line">
                {seated.map((g) =>
              <li key={g.id} className="flex items-center gap-3 py-2.5">
                    <div className="min-w-0 flex-1">
                      <p className="truncate text-sm text-ink">{g.name}</p>
                      <p className="text-xs text-ink-500">{g.group}{g.plusOnes ? ` · +${g.plusOnes}` : ''}</p>
                    </div>
                    <button
                  type="button"
                  onClick={() => {
                    updateGuest(g.id, { table: null });
                    toast(`${g.name} removed from ${selected.name}`);
                  }}
                  aria-label={`Remove ${g.name} from table`}
                  className="rounded p-1.5 text-ink-400 hover:bg-ivory-100 hover:text-ink">
                  
                      <XIcon className="h-4 w-4" />
                    </button>
                  </li>
              )}
              </ul> :

            <p className="py-4 text-sm text-ink-500">No named guests seated here yet.</p>
            }
            {selected.reserved > 0 &&
            <p className="border-t border-line py-3 text-xs text-ink-500">+ {selected.reserved} seats held for guests further down the list</p>
            }
          </Card>

          <Card title="Waiting for a table" description={`Seat them at ${selected.name}`} bodyClassName="px-5 py-2">
            {unassigned.length ?
            <ul className="divide-y divide-line">
                {unassigned.map((g) =>
              <li key={g.id} className="flex items-center gap-3 py-2.5">
                    <div className="min-w-0 flex-1">
                      <p className="truncate text-sm text-ink">{g.name}</p>
                      <div className="mt-0.5 flex items-center gap-2">
                        <Badge tone={rsvpMeta[g.rsvp].tone}>{rsvpMeta[g.rsvp].label}</Badge>
                        {g.plusOnes > 0 && <span className="text-xs text-ink-500">+{g.plusOnes}</span>}
                      </div>
                    </div>
                    <button
                  type="button"
                  onClick={() => assign(g)}
                  disabled={free < seatsFor(g)}
                  className="inline-flex h-8 items-center gap-1 rounded-md border border-line-strong px-2.5 text-xs font-medium text-ink transition-colors duration-150 hover:bg-ivory-100 disabled:cursor-not-allowed disabled:opacity-40">
                  
                      <PlusIcon className="h-3.5 w-3.5" aria-hidden /> Seat
                    </button>
                  </li>
              )}
              </ul> :

            <EmptyState icon={ArmchairIcon} title="Everyone has a seat" description="All confirmed and pending guests have a table." />
            }
          </Card>
        </div>
      </div>
    </div>);

}