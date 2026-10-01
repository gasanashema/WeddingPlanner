import React, { useEffect, useState } from 'react';
import { toast } from 'sonner';
import { BudgetCategory, Priority, Role, Scope, Task, TaskStatus } from '../../types/wedding';
import { useRole } from '../../contexts/RoleContext';
import { useWeddingData } from '../../contexts/WeddingDataContext';
import { budgetCategories } from '../../data/budget';
import { people, TODAY } from '../../data/wedding';
import { pickAssignee, scopeAudience, sideScopeOf } from '../../utils/permissions';
import { priorityMeta, taskStatusMeta } from '../../utils/status';
import { addDaysISO } from '../../utils/format';
import { inputClass } from '../../utils/ui';
import { Modal } from '../ui/Modal';
import { Button } from '../ui/Button';
import { Field } from '../ui/Field';
import { Select } from '../ui/Select';
import { ScopeSelector } from '../ui/ScopeSelector';

interface TaskFormModalProps {
  open: boolean;
  onClose: () => void;
  initial?: Task | null;
  defaultScope?: Scope;
}

type FormState = Omit<Task, 'id'>;

export function TaskFormModal({ open, onClose, initial, defaultScope }: TaskFormModalProps) {
  const { role, visibleScopes, isSupport, side } = useRole();
  const { addTask, updateTask } = useWeddingData();
  const [form, setForm] = useState<FormState>(() => blank());
  const [error, setError] = useState('');

  function blank(): FormState {
    const scope = defaultScope ?? (isSupport ? sideScopeOf(side) : 'shared');
    return {
      title: '',
      category: 'Other',
      scope,
      assignee: pickAssignee(scope, role),
      due: addDaysISO(TODAY, 14),
      priority: 'medium',
      status: 'not-started',
      budget: 0,
      notes: ''
    };
  }

  useEffect(() => {
    if (!open) return;
    setForm(initial ? { ...initial } : blank());
    setError('');
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [open, initial]);

  const set = <K extends keyof FormState,>(key: K, value: FormState[K]) => setForm((f) => ({ ...f, [key]: value }));

  const changeScope = (scope: Scope) =>
  setForm((f) => ({ ...f, scope, assignee: scopeAudience[scope].includes(f.assignee) ? f.assignee : pickAssignee(scope, role) }));

  const submit = (e: React.FormEvent) => {
    e.preventDefault();
    if (!form.title.trim()) {
      setError('Give the task a short title.');
      return;
    }
    if (initial) {
      updateTask(initial.id, form);
      toast.success('Task updated');
    } else {
      addTask(form);
      toast.success('Task added', { description: form.title });
    }
    onClose();
  };

  return (
    <Modal
      open={open}
      onClose={onClose}
      size="lg"
      title={initial ? 'Edit task' : 'New task'}
      description="Choose who can see it before you save."
      footer={
      <>
          <Button variant="secondary" onClick={onClose}>
            Cancel
          </Button>
          <Button type="submit" form="task-form">
            {initial ? 'Save changes' : 'Add task'}
          </Button>
        </>
      }>
      
      <form id="task-form" onSubmit={submit} className="space-y-5">
        <Field label="Task title" htmlFor="task-title" error={error}>
          <input
            id="task-title"
            autoFocus
            value={form.title}
            onChange={(e) => set('title', e.target.value)}
            placeholder="e.g. Confirm drinks order"
            className={inputClass} />
          
        </Field>

        {visibleScopes.length > 1 &&
        <div>
            <p className="mb-1.5 text-xs font-medium text-ink-700">Who can see this</p>
            <ScopeSelector value={form.scope} onChange={changeScope} scopes={visibleScopes} />
          </div>
        }

        <div className="grid gap-4 sm:grid-cols-2">
          <Field label="Category" htmlFor="task-category">
            <Select<BudgetCategory>
              id="task-category"
              value={form.category}
              onChange={(v) => set('category', v)}
              options={budgetCategories.map((c) => ({ value: c, label: c }))} />
            
          </Field>
          <Field label="Assigned to" htmlFor="task-assignee" hint="Only people who can see this task.">
            <Select<Role>
              id="task-assignee"
              value={form.assignee}
              onChange={(v) => set('assignee', v)}
              options={scopeAudience[form.scope].map((r) => ({ value: r, label: `${people[r].firstName} · ${people[r].roleLabel}` }))} />
            
          </Field>
          <Field label="Due date" htmlFor="task-due">
            <input id="task-due" type="date" value={form.due} onChange={(e) => set('due', e.target.value)} className={inputClass} />
          </Field>
          <Field label="Budget (RWF)" htmlFor="task-budget">
            <input
              id="task-budget"
              type="number"
              min={0}
              step={1000}
              value={form.budget || ''}
              onChange={(e) => set('budget', Number(e.target.value))}
              placeholder="0"
              className={`${inputClass} tnum`} />
            
          </Field>
          <Field label="Priority" htmlFor="task-priority">
            <Select<Priority>
              id="task-priority"
              value={form.priority}
              onChange={(v) => set('priority', v)}
              options={(Object.keys(priorityMeta) as Priority[]).map((p) => ({ value: p, label: priorityMeta[p].label }))} />
            
          </Field>
          <Field label="Status" htmlFor="task-status">
            <Select<TaskStatus>
              id="task-status"
              value={form.status}
              onChange={(v) => set('status', v)}
              options={(Object.keys(taskStatusMeta) as TaskStatus[]).map((s) => ({ value: s, label: taskStatusMeta[s].label }))} />
            
          </Field>
        </div>

        <Field label="Notes" htmlFor="task-notes">
          <textarea
            id="task-notes"
            rows={3}
            value={form.notes}
            onChange={(e) => set('notes', e.target.value)}
            placeholder="Contacts, links, reminders…"
            className={`${inputClass} h-auto py-2`} />
          
        </Field>
      </form>
    </Modal>);

}