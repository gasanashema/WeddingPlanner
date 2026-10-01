import React, { useState } from 'react';
import { toast } from 'sonner';
import { HouseIcon, PencilIcon, PlusIcon, Trash2Icon } from 'lucide-react';
import { HomeItem } from '../types/wedding';
import { useRole } from '../contexts/RoleContext';
import { useWeddingData } from '../contexts/WeddingDataContext';
import { homeTemplates } from '../data/homePreparation';
import { TODAY } from '../data/wedding';
import { addDaysISO, daysFromToday, formatCompact, formatDate, formatRWF, relativeDue } from '../utils/format';
import { sideScopeOf } from '../utils/permissions';
import { PageHeader } from '../components/ui/PageHeader';
import { Button } from '../components/ui/Button';
import { Segmented } from '../components/ui/Segmented';
import { ProgressBar } from '../components/ui/ProgressBar';
import { PersonChip } from '../components/ui/PersonChip';
import { CheckButton } from '../components/ui/CheckButton';
import { EmptyState } from '../components/ui/EmptyState';
import { ConfirmDialog } from '../components/ui/ConfirmDialog';
import { HomeItemFormModal } from '../components/forms/HomeItemFormModal';

type Filter = 'all' | 'open' | 'done';
const cols = 'md:grid-cols-[28px_minmax(0,1fr)_60px_140px_120px_110px_64px]';

export function HomePreparation() {
  const { side, role } = useRole();
  const { homeItems, addHomeItem, updateHomeItem, deleteHomeItem } = useWeddingData();
  const [filter, setFilter] = useState<Filter>('all');
  const [formOpen, setFormOpen] = useState(false);
  const [editing, setEditing] = useState<HomeItem | null>(null);
  const [deleting, setDeleting] = useState<HomeItem | null>(null);

  const items = homeItems.filter((h) => h.side === side);
  const visible = items.filter((h) => filter === 'all' ? true : filter === 'done' ? h.completed : !h.completed);
  const categories = Array.from(new Set(visible.map((h) => h.category)));
  const done = items.filter((h) => h.completed);
  const totalBudget = items.reduce((s, h) => s + h.budget, 0);
  const secured = done.reduce((s, h) => s + h.budget, 0);
  const suggestions = homeTemplates[side].filter((t) => !items.some((i) => i.name === t.name));
  const sideWord = side === 'bride' ? 'Bride' : 'Groom';

  const addFromTemplate = (name: string, category: string, budget: number) => {
    addHomeItem({ name, category, budget, side, quantity: 1, deadline: addDaysISO(TODAY, 45), assignee: role, completed: false, notes: '' });
    toast.success(`${name} added to your list`);
  };

  return (
    <div>
      <PageHeader
        title="Home Preparation"
        description={`What the ${side}’s side is providing for the new home in Kimironko.`}
        scope={sideScopeOf(side)}
        actions={
        <Button
          icon={PlusIcon}
          onClick={() => {
            setEditing(null);
            setFormOpen(true);
          }}>
          
            Add item
          </Button>
        } />
      

      <div className="mb-6 grid gap-6 lg:grid-cols-3">
        <section aria-label="Progress" className="rounded-lg border border-line bg-white p-6 shadow-card lg:col-span-2">
          <div className="grid gap-6 sm:grid-cols-3">
            <div>
              <p className="text-xs text-ink-500">Items ready</p>
              <p className="tnum mt-1 font-serif text-3xl font-semibold text-ink">
                {done.length}<span className="text-lg text-ink-500">/{items.length}</span>
              </p>
            </div>
            <div>
              <p className="text-xs text-ink-500">Total budget</p>
              <p className="tnum mt-1 text-2xl font-semibold text-ink">RWF {formatCompact(totalBudget)}</p>
            </div>
            <div>
              <p className="text-xs text-ink-500">Already bought</p>
              <p className="tnum mt-1 text-2xl font-semibold text-wine-700">RWF {formatCompact(secured)}</p>
            </div>
          </div>
          <ProgressBar value={items.length ? done.length / items.length * 100 : 0} size="md" className="mt-6" label="Home items ready" />
          <p className="mt-2 text-xs text-ink-500">New home should be ready by {formatDate('2027-08-21', 'd MMMM')} — {daysFromToday('2027-08-21')} days from now</p>
        </section>

        <section aria-label="Template suggestions" className="rounded-lg border border-gold-200 bg-gold-50/60 p-5">
          <p className="text-sm font-semibold text-ink">{sideWord}-side template</p>
          <p className="mt-1 text-xs text-ink-600">Common items Rwandan families provide on the {side}’s side. Add what fits — every item can be edited or removed.</p>
          {suggestions.length ?
          <div className="mt-4 flex flex-wrap gap-2">
              {suggestions.map((s) =>
            <button
              key={s.name}
              type="button"
              onClick={() => addFromTemplate(s.name, s.category, s.budget)}
              className="inline-flex h-8 items-center gap-1.5 rounded-md border border-gold-200 bg-white px-2.5 text-xs font-medium text-ink transition-colors duration-150 hover:border-gold-400">
              
                  <PlusIcon className="h-3.5 w-3.5 text-gold-600" aria-hidden />
                  {s.name}
                </button>
            )}
            </div> :

          <p className="mt-4 text-xs font-medium text-gold-700">You’ve added every template item.</p>
          }
        </section>
      </div>

      <div className="mb-4 flex items-center justify-between gap-3">
        <h2 className="font-serif text-xl font-semibold text-ink">Items</h2>
        <Segmented<Filter>
          ariaLabel="Filter items"
          value={filter}
          onChange={setFilter}
          options={[
          { value: 'all', label: 'All' },
          { value: 'open', label: 'To buy' },
          { value: 'done', label: 'Bought' }]
          } />
        
      </div>

      {visible.length === 0 ?
      <div className="rounded-lg border border-line bg-white shadow-card">
          <EmptyState icon={HouseIcon} title="Nothing in this view" description="Add an item or pick one from the template." />
        </div> :

      <div className="space-y-6">
          {categories.map((cat) =>
        <section key={cat} aria-label={cat} className="overflow-hidden rounded-lg border border-line bg-white shadow-card">
              <header className="flex items-center justify-between border-b border-line bg-ivory px-5 py-2.5">
                <h3 className="text-xs font-semibold text-ink-700">{cat}</h3>
                <span className="tnum text-xs text-ink-500">
                  {formatRWF(visible.filter((h) => h.category === cat).reduce((s, h) => s + h.budget, 0))}
                </span>
              </header>
              <ul className="divide-y divide-line">
                {visible.
            filter((h) => h.category === cat).
            map((h) => {
              const late = !h.completed && daysFromToday(h.deadline) < 0;
              return (
                <li key={h.id} className={`grid grid-cols-[28px_minmax(0,1fr)] items-center gap-x-3 gap-y-2 px-5 py-3 md:gap-4 ${cols}`}>
                        <CheckButton
                    checked={h.completed}
                    onChange={() => {
                      updateHomeItem(h.id, { completed: !h.completed });
                      if (!h.completed) toast.success(`${h.name} marked as bought`);
                    }}
                    label={`Mark ${h.name} ${h.completed ? 'not bought' : 'bought'}`} />
                  
                        <div className="min-w-0">
                          <p className={`truncate text-sm font-medium ${h.completed ? 'text-ink-500 line-through' : 'text-ink'}`}>{h.name}</p>
                          {h.notes && <p className="truncate text-xs text-ink-500">{h.notes}</p>}
                        </div>
                        <div className="col-start-2 flex flex-wrap items-center gap-x-4 gap-y-2 md:contents">
                          <span className="tnum text-xs text-ink-600 md:text-sm">×{h.quantity}</span>
                          <PersonChip role={h.assignee} />
                          <span className="tnum text-xs text-ink-700 md:text-sm">{formatRWF(h.budget)}</span>
                          <span className={`tnum text-xs md:text-sm ${late ? 'font-medium text-danger-700' : 'text-ink-600'}`} title={formatDate(h.deadline)}>
                            {h.completed ? 'Bought' : relativeDue(h.deadline)}
                          </span>
                          <div className="ml-auto flex gap-1 md:ml-0 md:justify-end">
                            <button
                        type="button"
                        onClick={() => {
                          setEditing(h);
                          setFormOpen(true);
                        }}
                        aria-label={`Edit ${h.name}`}
                        className="rounded p-1.5 text-ink-400 hover:bg-ivory-100 hover:text-ink">
                        
                              <PencilIcon className="h-4 w-4" />
                            </button>
                            <button type="button" onClick={() => setDeleting(h)} aria-label={`Delete ${h.name}`} className="rounded p-1.5 text-ink-400 hover:bg-danger-50 hover:text-danger-700">
                              <Trash2Icon className="h-4 w-4" />
                            </button>
                          </div>
                        </div>
                      </li>);

            })}
              </ul>
            </section>
        )}
        </div>
      }

      <HomeItemFormModal open={formOpen} onClose={() => setFormOpen(false)} side={side} initial={editing} />
      <ConfirmDialog
        open={!!deleting}
        onClose={() => setDeleting(null)}
        onConfirm={() => {
          if (deleting) {
            deleteHomeItem(deleting.id);
            toast('Item removed', { description: deleting.name });
          }
        }}
        title="Remove this item?"
        description={`"${deleting?.name ?? ''}" will be removed from the ${side}-side list.`}
        confirmLabel="Remove" />
      
    </div>);

}