import React, { useEffect, useState } from 'react';
import { toast } from 'sonner';
import { Guest, GuestSide, RsvpStatus } from '../../types/wedding';
import { useRole } from '../../contexts/RoleContext';
import { useWeddingData } from '../../contexts/WeddingDataContext';
import { guestGroups } from '../../data/guests';
import { rsvpMeta } from '../../utils/status';
import { inputClass } from '../../utils/ui';
import { Modal } from '../ui/Modal';
import { Button } from '../ui/Button';
import { Field } from '../ui/Field';
import { Select } from '../ui/Select';

type FormState = Omit<Guest, 'id'>;

export function GuestFormModal({ open, onClose }: {open: boolean;onClose: () => void;}) {
  const { isSupport, side } = useRole();
  const { addGuest } = useWeddingData();
  const [form, setForm] = useState<FormState>({ name: '', side, group: 'Family', phone: '+250 ', rsvp: 'pending', table: null, plusOnes: 0, invitationOpened: false });
  const [error, setError] = useState('');

  useEffect(() => {
    if (!open) return;
    setForm({ name: '', side, group: 'Family', phone: '+250 ', rsvp: 'pending', table: null, plusOnes: 0, invitationOpened: false });
    setError('');
  }, [open, side]);

  const set = <K extends keyof FormState,>(key: K, value: FormState[K]) => setForm((f) => ({ ...f, [key]: value }));

  const sideOptions: {value: GuestSide;label: string;}[] = isSupport ?
  [{ value: side, label: side === 'bride' ? "Bride's side" : "Groom's side" }] :
  [
  { value: 'bride', label: "Bride's side" },
  { value: 'groom', label: "Groom's side" },
  { value: 'both', label: 'Both families' }];


  const submit = (e: React.FormEvent) => {
    e.preventDefault();
    if (!form.name.trim()) {
      setError('Enter the guest’s full name.');
      return;
    }
    addGuest(form);
    toast.success('Guest added', { description: form.name });
    onClose();
  };

  return (
    <Modal
      open={open}
      onClose={onClose}
      title="Add a guest"
      footer={
      <>
          <Button variant="secondary" onClick={onClose}>
            Cancel
          </Button>
          <Button type="submit" form="guest-form">
            Add guest
          </Button>
        </>
      }>
      
      <form id="guest-form" onSubmit={submit} className="space-y-4">
        <Field label="Full name" htmlFor="guest-name" error={error}>
          <input id="guest-name" autoFocus value={form.name} onChange={(e) => set('name', e.target.value)} placeholder="e.g. Olive Mukamurenzi" className={inputClass} />
        </Field>
        <Field label="Phone (for WhatsApp / SMS invitation)" htmlFor="guest-phone">
          <input id="guest-phone" type="tel" value={form.phone} onChange={(e) => set('phone', e.target.value)} className={`${inputClass} tnum`} />
        </Field>
        <div className="grid gap-4 sm:grid-cols-2">
          <Field label="Side" htmlFor="guest-side" hint={isSupport ? 'You can add guests for your side only.' : undefined}>
            <Select<GuestSide> id="guest-side" value={form.side} onChange={(v) => set('side', v)} options={sideOptions} disabled={isSupport} />
          </Field>
          <Field label="Group" htmlFor="guest-group">
            <Select<string> id="guest-group" value={form.group} onChange={(v) => set('group', v)} options={guestGroups.map((g) => ({ value: g, label: g }))} />
          </Field>
          <Field label="RSVP status" htmlFor="guest-rsvp">
            <Select<RsvpStatus>
              id="guest-rsvp"
              value={form.rsvp}
              onChange={(v) => set('rsvp', v)}
              options={(Object.keys(rsvpMeta) as RsvpStatus[]).map((r) => ({ value: r, label: rsvpMeta[r].label }))} />
            
          </Field>
          <Field label="Plus-ones" htmlFor="guest-plus">
            <input id="guest-plus" type="number" min={0} max={4} value={form.plusOnes} onChange={(e) => set('plusOnes', Number(e.target.value))} className={`${inputClass} tnum`} />
          </Field>
        </div>
      </form>
    </Modal>);

}