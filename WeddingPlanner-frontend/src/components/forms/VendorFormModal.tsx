import React, { useEffect, useState } from 'react';
import { toast } from 'sonner';
import { BookingStatus, PaymentStatus, Vendor, VendorCategory } from '../../types/wedding';
import { useWeddingData } from '../../contexts/WeddingDataContext';
import { vendorCategories } from '../../data/vendors';
import { bookingMeta, paymentMeta } from '../../utils/status';
import { inputClass } from '../../utils/ui';
import { Modal } from '../ui/Modal';
import { Button } from '../ui/Button';
import { Field } from '../ui/Field';
import { Select } from '../ui/Select';

type FormState = Omit<Vendor, 'id'>;
const blank: FormState = { name: '', category: 'Venue', contactName: '', phone: '+250 ', email: '', cost: 0, paid: 0, paymentStatus: 'unpaid', bookingStatus: 'shortlisted', notes: '' };

export function VendorFormModal({ open, onClose }: {open: boolean;onClose: () => void;}) {
  const { addVendor } = useWeddingData();
  const [form, setForm] = useState<FormState>(blank);
  const [error, setError] = useState('');

  useEffect(() => {
    if (open) {
      setForm(blank);
      setError('');
    }
  }, [open]);

  const set = <K extends keyof FormState,>(key: K, value: FormState[K]) => setForm((f) => ({ ...f, [key]: value }));

  const submit = (e: React.FormEvent) => {
    e.preventDefault();
    if (!form.name.trim()) {
      setError('Enter the vendor’s business name.');
      return;
    }
    addVendor(form);
    toast.success('Vendor added', { description: form.name });
    onClose();
  };

  return (
    <Modal
      open={open}
      onClose={onClose}
      size="lg"
      title="Add vendor"
      footer={
      <>
          <Button variant="secondary" onClick={onClose}>
            Cancel
          </Button>
          <Button type="submit" form="vendor-form">
            Add vendor
          </Button>
        </>
      }>
      
      <form id="vendor-form" onSubmit={submit} className="grid gap-4 sm:grid-cols-2">
        <Field label="Business name" htmlFor="v-name" error={error} className="sm:col-span-2">
          <input id="v-name" autoFocus value={form.name} onChange={(e) => set('name', e.target.value)} className={inputClass} />
        </Field>
        <Field label="Category" htmlFor="v-cat">
          <Select<VendorCategory> id="v-cat" value={form.category} onChange={(v) => set('category', v)} options={vendorCategories.map((c) => ({ value: c, label: c }))} />
        </Field>
        <Field label="Contact person" htmlFor="v-contact">
          <input id="v-contact" value={form.contactName} onChange={(e) => set('contactName', e.target.value)} className={inputClass} />
        </Field>
        <Field label="Phone" htmlFor="v-phone">
          <input id="v-phone" type="tel" value={form.phone} onChange={(e) => set('phone', e.target.value)} className={`${inputClass} tnum`} />
        </Field>
        <Field label="Email" htmlFor="v-email">
          <input id="v-email" type="email" value={form.email} onChange={(e) => set('email', e.target.value)} className={inputClass} />
        </Field>
        <Field label="Agreed cost (RWF)" htmlFor="v-cost">
          <input id="v-cost" type="number" min={0} step={1000} value={form.cost || ''} placeholder="0" onChange={(e) => set('cost', Number(e.target.value))} className={`${inputClass} tnum`} />
        </Field>
        <Field label="Paid so far (RWF)" htmlFor="v-paid">
          <input id="v-paid" type="number" min={0} step={1000} value={form.paid || ''} placeholder="0" onChange={(e) => set('paid', Number(e.target.value))} className={`${inputClass} tnum`} />
        </Field>
        <Field label="Payment status" htmlFor="v-pay">
          <Select<PaymentStatus> id="v-pay" value={form.paymentStatus} onChange={(v) => set('paymentStatus', v)} options={(Object.keys(paymentMeta) as PaymentStatus[]).map((p) => ({ value: p, label: paymentMeta[p].label }))} />
        </Field>
        <Field label="Booking status" htmlFor="v-book">
          <Select<BookingStatus> id="v-book" value={form.bookingStatus} onChange={(v) => set('bookingStatus', v)} options={(Object.keys(bookingMeta) as BookingStatus[]).map((b) => ({ value: b, label: bookingMeta[b].label }))} />
        </Field>
        <Field label="Notes" htmlFor="v-notes" className="sm:col-span-2">
          <textarea id="v-notes" rows={2} value={form.notes} onChange={(e) => set('notes', e.target.value)} className={`${inputClass} h-auto py-2`} />
        </Field>
      </form>
    </Modal>);

}