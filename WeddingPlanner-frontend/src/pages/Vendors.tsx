import React, { useState } from 'react';
import { MailIcon, PhoneIcon, PlusIcon, StoreIcon } from 'lucide-react';
import { VendorCategory } from '../types/wedding';
import { useWeddingData } from '../contexts/WeddingDataContext';
import { vendorCategories } from '../data/vendors';
import { bookingMeta, paymentMeta } from '../utils/status';
import { formatRWF } from '../utils/format';
import { PageHeader } from '../components/ui/PageHeader';
import { Button } from '../components/ui/Button';
import { Badge } from '../components/ui/Badge';
import { ProgressBar } from '../components/ui/ProgressBar';
import { EmptyState } from '../components/ui/EmptyState';
import { VendorFormModal } from '../components/forms/VendorFormModal';

type Filter = 'All' | VendorCategory;
const cols = 'lg:grid-cols-[minmax(0,1.3fr)_minmax(0,1fr)_minmax(0,1fr)_150px_140px]';

export function Vendors() {
  const { vendors } = useWeddingData();
  const [filter, setFilter] = useState<Filter>('All');
  const [formOpen, setFormOpen] = useState(false);

  const list = filter === 'All' ? vendors : vendors.filter((v) => v.category === filter);
  const totalCost = vendors.reduce((s, v) => s + v.cost, 0);
  const totalPaid = vendors.reduce((s, v) => s + v.paid, 0);
  const booked = vendors.filter((v) => v.bookingStatus === 'booked').length;

  return (
    <div>
      <PageHeader
        title="Vendors"
        description="Everyone you’re working with, what they cost and where each booking stands."
        scope="shared"
        actions={<Button icon={PlusIcon} onClick={() => setFormOpen(true)}>Add vendor</Button>} />
      

      <section aria-label="Vendor totals" className="mb-6 grid gap-px overflow-hidden rounded-lg border border-line bg-line shadow-card sm:grid-cols-3">
        <div className="bg-white p-5">
          <p className="text-xs text-ink-500">Booked</p>
          <p className="tnum mt-1 text-2xl font-semibold text-ink">{booked} <span className="text-base font-normal text-ink-500">of {vendors.length} vendors</span></p>
        </div>
        <div className="bg-white p-5">
          <p className="text-xs text-ink-500">Committed cost</p>
          <p className="tnum mt-1 text-2xl font-semibold text-ink">{formatRWF(totalCost)}</p>
        </div>
        <div className="bg-white p-5">
          <p className="text-xs text-ink-500">Still to pay</p>
          <p className="tnum mt-1 text-2xl font-semibold text-wine-700">{formatRWF(totalCost - totalPaid)}</p>
          <ProgressBar value={totalPaid / totalCost * 100} tone="success" className="mt-2" label="Vendor payments made" />
        </div>
      </section>

      <div role="tablist" aria-label="Vendor category" className="scrollbar-none mb-4 flex gap-2 overflow-x-auto">
        {(['All', ...vendorCategories] as Filter[]).map((c) => {
          const active = c === filter;
          return (
            <button
              key={c}
              type="button"
              role="tab"
              aria-selected={active}
              onClick={() => setFilter(c)}
              className={`h-8 shrink-0 whitespace-nowrap rounded-md border px-3 text-[13px] font-medium transition-colors duration-150 ${
              active ? 'border-wine-700 bg-wine-700 text-white' : 'border-line-strong bg-white text-ink-600 hover:bg-ivory-100'}`
              }>
              
              {c}
            </button>);

        })}
      </div>

      <section aria-label="Vendor list" className="overflow-hidden rounded-lg border border-line bg-white shadow-card">
        <div className={`hidden gap-4 border-b border-line bg-ivory px-5 py-3 text-xs font-medium text-ink-500 lg:grid ${cols}`}>
          <span>Vendor</span>
          <span>Contact</span>
          <span>Notes</span>
          <span>Cost & payment</span>
          <span>Booking</span>
        </div>
        {list.length === 0 ?
        <EmptyState icon={StoreIcon} title={`No ${filter.toLowerCase()} vendors yet`} description="Add a vendor to keep their contact, cost and payments together." action={<Button icon={PlusIcon} onClick={() => setFormOpen(true)}>Add vendor</Button>} /> :

        <ul className="divide-y divide-line">
            {list.map((v) => {
            const pay = paymentMeta[v.paymentStatus];
            const book = bookingMeta[v.bookingStatus];
            return (
              <li key={v.id} className={`grid gap-3 px-5 py-4 lg:items-start lg:gap-4 ${cols}`}>
                  <div className="flex items-start justify-between gap-3">
                    <div className="min-w-0">
                      <p className="text-sm font-semibold text-ink">{v.name}</p>
                      <p className="text-xs text-ink-500">{v.category}</p>
                    </div>
                    <span className="lg:hidden"><Badge tone={book.tone} dot>{book.label}</Badge></span>
                  </div>
                  <div className="min-w-0 space-y-1 text-sm">
                    <p className="text-ink-700">{v.contactName}</p>
                    <a href={`tel:${v.phone.replace(/\s/g, '')}`} className="tnum flex items-center gap-1.5 text-xs text-ink-500 hover:text-wine-700">
                      <PhoneIcon className="h-3 w-3" aria-hidden /> {v.phone}
                    </a>
                    <a href={`mailto:${v.email}`} className="flex items-center gap-1.5 truncate text-xs text-ink-500 hover:text-wine-700">
                      <MailIcon className="h-3 w-3 shrink-0" aria-hidden /> <span className="truncate">{v.email}</span>
                    </a>
                  </div>
                  <p className="text-xs leading-relaxed text-ink-600">{v.notes}</p>
                  <div>
                    <p className="tnum text-sm font-medium text-ink">{formatRWF(v.cost)}</p>
                    <p className="tnum mt-0.5 text-xs text-ink-500">Paid {formatRWF(v.paid)}</p>
                    <div className="mt-1.5"><Badge tone={pay.tone}>{pay.label}</Badge></div>
                  </div>
                  <span className="hidden lg:block"><Badge tone={book.tone} dot>{book.label}</Badge></span>
                </li>);

          })}
          </ul>
        }
      </section>

      <VendorFormModal open={formOpen} onClose={() => setFormOpen(false)} />
    </div>);

}