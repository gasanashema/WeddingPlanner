import React, { useState } from 'react';
import { BellIcon, InboxIcon, LayoutDashboardIcon, LockIcon, NotebookPenIcon, PlusIcon, SearchIcon, SendIcon, Trash2Icon, WalletIcon } from 'lucide-react';
import { Scope } from '../types/wedding';
import { colorTokens, radiusTokens, spacingTokens, typeScale } from '../data/designTokens';
import { inputClass } from '../utils/ui';
import { priorityMeta, rsvpMeta, taskStatusMeta, paymentMeta, bookingMeta } from '../utils/status';
import { PageHeader } from '../components/ui/PageHeader';
import { Button } from '../components/ui/Button';
import { Badge } from '../components/ui/Badge';
import { ScopeBadge } from '../components/ui/ScopeBadge';
import { Field } from '../components/ui/Field';
import { Select } from '../components/ui/Select';
import { Tabs } from '../components/ui/Tabs';
import { Segmented } from '../components/ui/Segmented';
import { ProgressBar } from '../components/ui/ProgressBar';
import { Card } from '../components/ui/Card';
import { Modal } from '../components/ui/Modal';
import { EmptyState } from '../components/ui/EmptyState';
import { CheckButton } from '../components/ui/CheckButton';
import { PersonChip } from '../components/ui/PersonChip';
import { DSSection } from '../components/design-system/DSSection';

const scopes: Scope[] = ['private-bride', 'bride-side', 'groom-side', 'shared'];

export function DesignSystem() {
  const [tab, setTab] = useState<'details' | 'payments' | 'notes'>('details');
  const [seg, setSeg] = useState<'list' | 'board'>('list');
  const [sel, setSel] = useState<'venue' | 'food'>('venue');
  const [checked, setChecked] = useState(true);
  const [modalOpen, setModalOpen] = useState(false);

  return (
    <div className="space-y-10">
      <PageHeader title="Design system" description="Tokens and components behind Ubukwe. Warm neutrals, one wine primary, champagne used sparingly, and a clear marker for who can see each item." />

      <DSSection id="colors" title="Colour" description="Wine is the only primary. Champagne gold marks key numbers and highlights. Each access area has its own quiet colour.">
        <div className="grid gap-6 md:grid-cols-2 xl:grid-cols-3">
          {colorTokens.map((g) =>
          <div key={g.group}>
              <p className="mb-2 text-xs font-medium text-ink-700">{g.group}</p>
              <div className="overflow-hidden rounded-lg border border-line bg-white">
                {g.swatches.map((s) =>
              <div key={s.name} className="flex items-center gap-3 border-b border-line px-3 py-2 last:border-b-0">
                    <span className="h-7 w-7 shrink-0 rounded border border-black/5" style={{ backgroundColor: s.hex }} aria-hidden />
                    <span className="flex-1 text-sm text-ink">{s.name}</span>
                    <span className="tnum text-xs text-ink-500">{s.hex}</span>
                  </div>
              )}
              </div>
            </div>
          )}
        </div>
      </DSSection>

      <DSSection id="access" title="Access indicators" description="Every task, expense and page shows who can see it. The lock always means private. Colour and label separate the two family sides.">
        <div className="flex flex-wrap items-center gap-3">
          {scopes.map((s) =>
          <ScopeBadge key={s} scope={s} size="md" withAudience />
          )}
        </div>
      </DSSection>

      <DSSection id="type" title="Typography" description="Playfair Display for page titles and wedding moments; Inter for all interface text and numbers.">
        <div className="divide-y divide-line rounded-lg border border-line bg-white">
          {typeScale.map((t) =>
          <div key={t.name} className="grid gap-2 px-5 py-4 md:grid-cols-[200px_minmax(0,1fr)] md:items-center">
              <div>
                <p className="text-sm font-medium text-ink">{t.name}</p>
                <p className="text-xs text-ink-500">{t.spec}</p>
              </div>
              <p className={`truncate text-ink ${t.className}`}>{t.sample}</p>
            </div>
          )}
        </div>
        <div className="mt-4 flex flex-wrap gap-6 text-xs text-ink-500">
          <span>Spacing: {spacingTokens.map((s) => `${s}px`).join(' · ')}</span>
          <span>Radius: {radiusTokens.map((r) => `${r.name} ${r.value}`).join(' · ')}</span>
        </div>
      </DSSection>

      <DSSection id="buttons" title="Buttons">
        <div className="flex flex-wrap items-center gap-3">
          <Button icon={PlusIcon}>Primary</Button>
          <Button variant="secondary">Secondary</Button>
          <Button variant="ghost">Ghost</Button>
          <Button variant="gold" icon={SendIcon}>Accent</Button>
          <Button variant="danger" icon={Trash2Icon}>Delete</Button>
          <Button disabled>Disabled</Button>
          <Button size="sm">Small</Button>
          <Button size="sm" variant="secondary">Small secondary</Button>
        </div>
      </DSSection>

      <DSSection id="inputs" title="Inputs & dropdowns">
        <div className="grid max-w-3xl gap-4 sm:grid-cols-2">
          <Field label="Task title" htmlFor="ds-input" hint="Short and specific works best.">
            <input id="ds-input" className={inputClass} placeholder="e.g. Confirm drinks order" />
          </Field>
          <Field label="Search" htmlFor="ds-search">
            <div className="relative">
              <SearchIcon className="pointer-events-none absolute left-3 top-1/2 h-4 w-4 -translate-y-1/2 text-ink-400" aria-hidden />
              <input id="ds-search" className={`${inputClass} pl-9`} placeholder="Search guests" />
            </div>
          </Field>
          <Field label="Category" htmlFor="ds-select">
            <Select<'venue' | 'food'> id="ds-select" value={sel} onChange={setSel} options={[{ value: 'venue', label: 'Venue' }, { value: 'food', label: 'Food & catering' }]} />
          </Field>
          <Field label="Amount (RWF)" htmlFor="ds-error" error="Enter an amount above zero.">
            <input id="ds-error" className={`${inputClass} border-danger-600`} defaultValue="0" />
          </Field>
          <label className="flex items-center gap-3 text-sm text-ink">
            <CheckButton checked={checked} onChange={() => setChecked((c) => !c)} label="Example checkbox" />
            Mark as completed
          </label>
        </div>
      </DSSection>

      <DSSection id="tabs" title="Tabs & segmented controls">
        <div className="space-y-5">
          <Tabs
            id="ds-tabs"
            value={tab}
            onChange={setTab}
            items={[
            { value: 'details', label: 'Details' },
            { value: 'payments', label: 'Payments', count: 3 },
            { value: 'notes', label: 'Notes' }]
            } />
          
          <Segmented ariaLabel="View" value={seg} onChange={setSeg} options={[{ value: 'list', label: 'List' }, { value: 'board', label: 'Board' }]} />
        </div>
      </DSSection>

      <DSSection id="badges" title="Status badges">
        <div className="space-y-3">
          {[taskStatusMeta, rsvpMeta, paymentMeta, bookingMeta, priorityMeta].map((group, i) =>
          <div key={i} className="flex flex-wrap gap-2">
              {Object.values(group).map((m) =>
            <Badge key={m.label} tone={m.tone} dot={i < 2}>{m.label}</Badge>
            )}
            </div>
          )}
        </div>
      </DSSection>

      <DSSection id="progress" title="Progress">
        <div className="grid max-w-3xl gap-4 sm:grid-cols-2">
          <ProgressBar value={68} size="md" label="Example" />
          <ProgressBar value={45} tone="gold" size="md" label="Example" />
          <ProgressBar value={100} tone="success" label="Example" />
          <ProgressBar value={92} tone="warning" label="Example" />
        </div>
      </DSSection>

      <DSSection id="cards" title="Cards, tables & empty states">
        <div className="grid gap-6 lg:grid-cols-3">
          <Card title="Budget summary" meta={<ScopeBadge scope="shared" />}>
            <p className="tnum text-2xl font-semibold text-ink">RWF 12,395,000</p>
            <p className="text-xs text-ink-500">spent of RWF 23,100,000</p>
            <ProgressBar value={54} className="mt-3" label="Example" />
          </Card>
          <Card title="Table" bodyClassName="p-0">
            <table className="w-full text-sm">
              <thead className="bg-ivory text-left text-xs text-ink-500">
                <tr>
                  <th className="px-4 py-2.5 font-medium">Task</th>
                  <th className="px-4 py-2.5 font-medium">Owner</th>
                </tr>
              </thead>
              <tbody className="divide-y divide-line">
                <tr><td className="px-4 py-3 text-ink">Confirm venue</td><td className="px-4 py-3"><PersonChip role="groom" /></td></tr>
                <tr><td className="px-4 py-3 text-ink">Finalize décor</td><td className="px-4 py-3"><PersonChip role="bride" /></td></tr>
                <tr><td className="px-4 py-3 text-ink">Gusaba gifts</td><td className="px-4 py-3"><PersonChip role="bride-support" /></td></tr>
              </tbody>
            </table>
          </Card>
          <Card bodyClassName="p-0">
            <EmptyState icon={InboxIcon} title="No expenses yet" description="Log the first payment and it will appear here." action={<Button size="sm" icon={PlusIcon}>Log expense</Button>} />
          </Card>
        </div>
      </DSSection>

      <DSSection id="overlays" title="Modals & notifications">
        <div className="grid gap-6 lg:grid-cols-2">
          <div className="flex items-start">
            <Button variant="secondary" onClick={() => setModalOpen(true)}>Open example modal</Button>
          </div>
          <div className="rounded-lg border border-line bg-white shadow-pop">
            <div className="flex items-center gap-2 border-b border-line px-4 py-3 text-sm font-semibold text-ink">
              <BellIcon className="h-4 w-4 text-ink-400" aria-hidden /> Notifications
            </div>
            <div className="flex gap-3 px-4 py-3">
              <span className="mt-1.5 h-2 w-2 shrink-0 rounded-full bg-wine-600" aria-hidden />
              <div>
                <p className="text-sm font-medium text-ink">Venue hold expires in 13 days</p>
                <p className="mt-0.5 text-xs text-ink-500">Intare Gardens needs the balance by 15 June.</p>
                <div className="mt-2 flex items-center gap-2"><ScopeBadge scope="shared" /><span className="text-[11px] text-ink-500">2h ago</span></div>
              </div>
            </div>
          </div>
        </div>
      </DSSection>

      <DSSection id="navigation" title="Navigation">
        <div className="w-64 space-y-0.5 rounded-lg border border-line bg-surface p-3">
          <div className="flex h-9 items-center gap-3 rounded-md bg-wine-50 px-3 text-sm font-medium text-wine-800">
            <LayoutDashboardIcon className="h-4 w-4 text-wine-700" aria-hidden /> Overview
          </div>
          <div className="flex h-9 items-center gap-3 rounded-md px-3 text-sm text-ink-600">
            <NotebookPenIcon className="h-4 w-4 text-ink-400" aria-hidden />
            <span className="flex-1">My Planning</span>
            <span className="rounded bg-bride-50 px-1.5 py-0.5 text-[10px] font-semibold text-bride-700">Bride side</span>
          </div>
          <div className="flex h-9 items-center gap-3 rounded-md px-3 text-sm text-ink-600">
            <WalletIcon className="h-4 w-4 text-ink-400" aria-hidden />
            <span className="flex-1">Vendors</span>
            <LockIcon className="h-3.5 w-3.5 text-ink-400" aria-label="Couple only" />
          </div>
        </div>
      </DSSection>

      <Modal
        open={modalOpen}
        onClose={() => setModalOpen(false)}
        title="Delete this task?"
        footer={
        <>
            <Button variant="secondary" onClick={() => setModalOpen(false)}>Cancel</Button>
            <Button variant="danger" onClick={() => setModalOpen(false)}>Delete</Button>
          </>
        }>
        
        <p className="text-sm text-ink-600">"Confirm catering" will be removed for everyone who can see it.</p>
      </Modal>
    </div>);

}