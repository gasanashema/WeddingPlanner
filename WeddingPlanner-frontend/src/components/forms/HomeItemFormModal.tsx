import React, { useEffect, useState } from 'react';
import { toast } from 'sonner';
import { HomeItem, Role, Side } from '../../types/wedding';
import { useRole } from '../../contexts/RoleContext';
import { useWeddingData } from '../../contexts/WeddingDataContext';
import { homeCategories } from '../../data/homePreparation';
import { people, TODAY } from '../../data/wedding';
import { scopeAudience, sideScopeOf } from '../../utils/permissions';
import { addDaysISO } from '../../utils/format';
import { inputClass } from '../../utils/ui';
import { Modal } from '../ui/Modal';
import { Button } from '../ui/Button';
import { Field } from '../ui/Field';
import { Select } from '../ui/Select';

interface HomeItemFormModalProps {
  open: boolean;
  onClose: () => void;
  side: Side;
  initial?: HomeItem | null;
}

type FormState = Omit<HomeItem, 'id'>;

export function HomeItemFormModal({ open, onClose, side, initial }: HomeItemFormModalProps) {
  const { role } = useRole();
  const { addHomeItem, updateHomeItem } = useWeddingData();
  const categories = homeCategories[side];
  const assignees = scopeAudience[sideScopeOf(side)];
  const blank = (): FormState => ({ name: '', category: categories[0], side, quantity: 1, budget: 0, deadline: addDaysISO(TODAY, 30), assignee: assignees.includes(role) ? role : assignees[0], completed: false, notes: '' });
  const [form, setForm] = useState<FormState>(blank);
  const [error, setError] = useState('');

  useEffect(() => {
    if (!open) return;
    setForm(initial ? { ...initial } : blank());
    setError('');
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [open, initial, side]);

  const set = <K extends keyof FormState,>(key: K, value: FormState[K]) => setForm((f) => ({ ...f, [key]: value }));

  const submit = (e: React.FormEvent) => {
    e.preventDefault();
    if (!form.name.trim()) {
      setError('Name the item.');
      return;
    }
    if (initial) {
      updateHomeItem(initial.id, form);
      toast.success('Item updated');
    } else {
      addHomeItem(form);
      toast.success('Item added', { description: form.name });
    }
    onClose();
  };

  return (
    <Modal
      open={open}
      onClose={onClose}
      title={initial ? 'Edit item' : 'Add home item'}
      description={`${side === 'bride' ? 'Bride' : 'Groom'}-side home preparation`}
      footer={
      <>
          <Button variant="secondary" onClick={onClose}>
            Cancel
          </Button>
          <Button type="submit" form="home-form">
            {initial ? 'Save changes' : 'Add item'}
          </Button>
        </>
      }>
      
      <form id="home-form" onSubmit={submit} className="space-y-4">
        <Field label="Item" htmlFor="home-name" error={error}>
          <input id="home-name" autoFocus value={form.name} onChange={(e) => set('name', e.target.value)} placeholder="e.g. Electric kettle" className={inputClass} />
        </Field>
        <div className="grid gap-4 sm:grid-cols-2">
          <Field label="Category" htmlFor="home-cat">
            <Select<string> id="home-cat" value={form.category} onChange={(v) => set('category', v)} options={categories.map((c) => ({ value: c, label: c }))} />
          </Field>
          <Field label="Assigned to" htmlFor="home-assignee">
            <Select<Role> id="home-assignee" value={form.assignee} onChange={(v) => set('assignee', v)} options={assignees.map((r) => ({ value: r, label: `${people[r].firstName} · ${people[r].roleLabel}` }))} />
          </Field>
          <Field label="Quantity" htmlFor="home-qty">
            <input id="home-qty" type="number" min={1} value={form.quantity} onChange={(e) => set('quantity', Number(e.target.value))} className={`${inputClass} tnum`} />
          </Field>
          <Field label="Budget (RWF)" htmlFor="home-budget">
            <input id="home-budget" type="number" min={0} step={1000} value={form.budget || ''} placeholder="0" onChange={(e) => set('budget', Number(e.target.value))} className={`${inputClass} tnum`} />
          </Field>
          <Field label="Deadline" htmlFor="home-deadline">
            <input id="home-deadline" type="date" value={form.deadline} onChange={(e) => set('deadline', e.target.value)} className={inputClass} />
          </Field>
          <label className="flex items-center gap-2 self-end pb-2.5 text-sm text-ink">
            <input type="checkbox" checked={form.completed} onChange={(e) => set('completed', e.target.checked)} className="h-4 w-4 rounded border-line-strong accent-wine-700" />
            Already bought
          </label>
        </div>
        <Field label="Notes" htmlFor="home-notes">
          <textarea id="home-notes" rows={2} value={form.notes} onChange={(e) => set('notes', e.target.value)} className={`${inputClass} h-auto py-2`} />
        </Field>
      </form>
    </Modal>);

}