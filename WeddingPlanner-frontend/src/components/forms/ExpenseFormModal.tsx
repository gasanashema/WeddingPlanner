import React, { useEffect, useState } from 'react';
import { toast } from 'sonner';
import { BudgetCategory, Expense, PaymentMethod, Role, Scope } from '../../types/wedding';
import { useRole } from '../../contexts/RoleContext';
import { useWeddingData } from '../../contexts/WeddingDataContext';
import { budgetCategories } from '../../data/budget';
import { people, TODAY } from '../../data/wedding';
import { pickAssignee, scopeAudience, sideScopeOf } from '../../utils/permissions';
import { formatRWF } from '../../utils/format';
import { inputClass } from '../../utils/ui';
import { Modal } from '../ui/Modal';
import { Button } from '../ui/Button';
import { Field } from '../ui/Field';
import { Select } from '../ui/Select';
import { ScopeSelector } from '../ui/ScopeSelector';

type FormState = Omit<Expense, 'id'>;
const methods: PaymentMethod[] = ['MoMo', 'Bank transfer', 'Cash', 'Card'];

export function ExpenseFormModal({ open, onClose }: {open: boolean;onClose: () => void;}) {
  const { role, visibleScopes, isSupport, side } = useRole();
  const { addExpense } = useWeddingData();
  const [form, setForm] = useState<FormState | null>(null);
  const [errors, setErrors] = useState<{title?: string;amount?: string;}>({});

  useEffect(() => {
    if (!open) return;
    const scope: Scope = isSupport ? sideScopeOf(side) : 'shared';
    setForm({ title: '', category: 'Other', scope, amount: 0, date: TODAY, paidBy: pickAssignee(scope, role), method: 'MoMo', vendor: '' });
    setErrors({});
  }, [open, isSupport, side, role]);

  if (!form) return null;
  const set = <K extends keyof FormState,>(key: K, value: FormState[K]) => setForm((f) => f ? { ...f, [key]: value } : f);

  const submit = (e: React.FormEvent) => {
    e.preventDefault();
    const next: typeof errors = {};
    if (!form.title.trim()) next.title = 'What was this payment for?';
    if (!form.amount || form.amount <= 0) next.amount = 'Enter an amount above zero.';
    setErrors(next);
    if (Object.keys(next).length) return;
    addExpense(form);
    toast.success('Expense logged', { description: `${form.title} · ${formatRWF(form.amount)}` });
    onClose();
  };

  return (
    <Modal
      open={open}
      onClose={onClose}
      title="Log an expense"
      description="Payments are added to the budget for the area you choose."
      footer={
      <>
          <Button variant="secondary" onClick={onClose}>
            Cancel
          </Button>
          <Button type="submit" form="expense-form">
            Save expense
          </Button>
        </>
      }>
      
      <form id="expense-form" onSubmit={submit} className="space-y-4">
        <Field label="Amount (RWF)" htmlFor="exp-amount" error={errors.amount}>
          <input
            id="exp-amount"
            type="number"
            inputMode="numeric"
            min={0}
            step={500}
            autoFocus
            value={form.amount || ''}
            onChange={(e) => set('amount', Number(e.target.value))}
            placeholder="0"
            className={`${inputClass} tnum h-12 text-lg font-semibold`} />
          
        </Field>
        <Field label="What was it for?" htmlFor="exp-title" error={errors.title}>
          <input id="exp-title" value={form.title} onChange={(e) => set('title', e.target.value)} placeholder="e.g. Drinks deposit" className={inputClass} />
        </Field>
        {visibleScopes.length > 1 &&
        <div>
            <p className="mb-1.5 text-xs font-medium text-ink-700">Budget area</p>
            <ScopeSelector
            value={form.scope}
            onChange={(s) => setForm((f) => f ? { ...f, scope: s, paidBy: pickAssignee(s, f.paidBy) } : f)}
            scopes={visibleScopes} />
          
          </div>
        }
        <div className="grid gap-4 sm:grid-cols-2">
          <Field label="Category" htmlFor="exp-category">
            <Select<BudgetCategory> id="exp-category" value={form.category} onChange={(v) => set('category', v)} options={budgetCategories.map((c) => ({ value: c, label: c }))} />
          </Field>
          <Field label="Date" htmlFor="exp-date">
            <input id="exp-date" type="date" value={form.date} onChange={(e) => set('date', e.target.value)} className={inputClass} />
          </Field>
          <Field label="Paid by" htmlFor="exp-paidby">
            <Select<Role> id="exp-paidby" value={form.paidBy} onChange={(v) => set('paidBy', v)} options={scopeAudience[form.scope].map((r) => ({ value: r, label: people[r].firstName }))} />
          </Field>
          <Field label="Payment method" htmlFor="exp-method">
            <Select<PaymentMethod> id="exp-method" value={form.method} onChange={(v) => set('method', v)} options={methods.map((m) => ({ value: m, label: m }))} />
          </Field>
        </div>
        <Field label="Vendor (optional)" htmlFor="exp-vendor">
          <input id="exp-vendor" value={form.vendor ?? ''} onChange={(e) => set('vendor', e.target.value)} placeholder="e.g. Kigali Beverage Depot" className={inputClass} />
        </Field>
      </form>
    </Modal>);

}