import React, { useMemo, useState } from 'react';
import { toast } from 'sonner';
import { FlagIcon, ListChecksIcon, PencilIcon, PlusIcon, SearchIcon, Trash2Icon } from 'lucide-react';
import { Role, Scope, Task, TaskStatus } from '../types/wedding';
import { useRole } from '../contexts/RoleContext';
import { useWeddingData } from '../contexts/WeddingDataContext';
import { useVisibleData } from '../hooks/useVisibleData';
import { people } from '../data/wedding';
import { scopeAudience, scopeMeta } from '../utils/permissions';
import { priorityMeta, taskStatusMeta } from '../utils/status';
import { daysFromToday, formatCompact, formatDate, relativeDue } from '../utils/format';
import { inputClass } from '../utils/ui';
import { PageHeader } from '../components/ui/PageHeader';
import { Button } from '../components/ui/Button';
import { Tabs } from '../components/ui/Tabs';
import { Select } from '../components/ui/Select';
import { Badge } from '../components/ui/Badge';
import { ScopeBadge } from '../components/ui/ScopeBadge';
import { PersonChip } from '../components/ui/PersonChip';
import { CheckButton } from '../components/ui/CheckButton';
import { EmptyState } from '../components/ui/EmptyState';
import { ConfirmDialog } from '../components/ui/ConfirmDialog';
import { TaskFormModal } from '../components/forms/TaskFormModal';

type StatusFilter = 'all' | TaskStatus;
const priorityColor = { high: 'text-wine-700', medium: 'text-gold-600', low: 'text-ink-400' };
const gridCols = 'lg:grid-cols-[28px_minmax(0,1fr)_150px_110px_90px_120px_100px_64px]';

export function Tasks() {
  const { visibleScopes } = useRole();
  const { tasks } = useVisibleData();
  const { updateTask, deleteTask } = useWeddingData();
  const [status, setStatus] = useState<StatusFilter>('all');
  const [assignee, setAssignee] = useState<'all' | Role>('all');
  const [scope, setScope] = useState<'all' | Scope>('all');
  const [query, setQuery] = useState('');
  const [editing, setEditing] = useState<Task | null>(null);
  const [formOpen, setFormOpen] = useState(false);
  const [deleting, setDeleting] = useState<Task | null>(null);

  const assignable = Array.from(new Set(visibleScopes.flatMap((s) => scopeAudience[s])));
  const statusOrder: Record<TaskStatus, number> = { overdue: 0, 'in-progress': 1, 'not-started': 2, completed: 3 };

  const filtered = useMemo(
    () =>
    tasks.
    filter((t) => status === 'all' ? true : t.status === status).
    filter((t) => assignee === 'all' ? true : t.assignee === assignee).
    filter((t) => scope === 'all' ? true : t.scope === scope).
    filter((t) => t.title.toLowerCase().includes(query.toLowerCase())).
    sort((a, b) => statusOrder[a.status] - statusOrder[b.status] || a.due.localeCompare(b.due)),
    // eslint-disable-next-line react-hooks/exhaustive-deps
    [tasks, status, assignee, scope, query]
  );

  const count = (s: TaskStatus) => tasks.filter((t) => t.status === s).length;

  const openEdit = (t: Task) => {
    setEditing(t);
    setFormOpen(true);
  };

  const toggle = (t: Task) => {
    const done = t.status === 'completed';
    updateTask(t.id, { status: done ? 'in-progress' : 'completed' });
    if (!done) toast.success('Task completed', { description: t.title });
  };

  return (
    <div>
      <PageHeader
        title="Tasks"
        description="Every to-do across the areas you can access. Each task shows who can see it."
        actions={
        <Button
          icon={PlusIcon}
          onClick={() => {
            setEditing(null);
            setFormOpen(true);
          }}>
          
            New task
          </Button>
        } />
      

      <Tabs<StatusFilter>
        id="task-status"
        value={status}
        onChange={setStatus}
        items={[
        { value: 'all', label: 'All', count: tasks.length },
        { value: 'not-started', label: 'Not Started', count: count('not-started') },
        { value: 'in-progress', label: 'In Progress', count: count('in-progress') },
        { value: 'completed', label: 'Completed', count: count('completed') },
        { value: 'overdue', label: 'Overdue', count: count('overdue') }]
        } />
      

      <div className="mt-4 flex flex-col gap-2 sm:flex-row">
        <div className="relative flex-1">
          <SearchIcon className="pointer-events-none absolute left-3 top-1/2 h-4 w-4 -translate-y-1/2 text-ink-400" aria-hidden />
          <input value={query} onChange={(e) => setQuery(e.target.value)} placeholder="Search tasks" aria-label="Search tasks" className={`${inputClass} pl-9`} />
        </div>
        <Select<'all' | Role>
          ariaLabel="Filter by assignee"
          value={assignee}
          onChange={setAssignee}
          className="sm:w-52"
          options={[{ value: 'all', label: 'Anyone' }, ...assignable.map((r) => ({ value: r, label: people[r].firstName + ' · ' + people[r].roleLabel }))]} />
        
        {visibleScopes.length > 1 &&
        <Select<'all' | Scope>
          ariaLabel="Filter by visibility"
          value={scope}
          onChange={setScope}
          className="sm:w-44"
          options={[{ value: 'all', label: 'All areas' }, ...visibleScopes.map((s) => ({ value: s, label: scopeMeta[s].label }))]} />

        }
      </div>

      <section aria-label="Task list" className="mt-4 overflow-hidden rounded-lg border border-line bg-white shadow-card">
        <div className={`hidden gap-4 border-b border-line bg-ivory px-5 py-3 text-xs font-medium text-ink-500 lg:grid ${gridCols}`}>
          <span className="sr-only">Done</span>
          <span>Task</span>
          <span>Assigned to</span>
          <span>Due</span>
          <span>Priority</span>
          <span>Status</span>
          <span className="text-right">Budget</span>
          <span className="sr-only">Actions</span>
        </div>
        {filtered.length === 0 ?
        <EmptyState icon={ListChecksIcon} title="No tasks match" description="Try a different filter, or add a new task." /> :

        <ul className="divide-y divide-line">
            {filtered.map((t) => {
            const done = t.status === 'completed';
            const overdue = !done && (t.status === 'overdue' || daysFromToday(t.due) < 0);
            const st = taskStatusMeta[t.status];
            return (
              <li key={t.id} className={`group grid grid-cols-[28px_minmax(0,1fr)] gap-x-3 gap-y-2 px-5 py-3.5 transition-colors duration-150 hover:bg-ivory/60 lg:items-center lg:gap-4 ${gridCols}`}>
                  <div className="pt-0.5 lg:pt-0">
                    <CheckButton checked={done} onChange={() => toggle(t)} label={`Mark "${t.title}" ${done ? 'not done' : 'done'}`} />
                  </div>
                  <button type="button" onClick={() => openEdit(t)} className="min-w-0 text-left">
                    <p className={`truncate text-sm font-medium ${done ? 'text-ink-500 line-through' : 'text-ink'}`}>{t.title}</p>
                    <div className="mt-1 flex flex-wrap items-center gap-2 text-xs text-ink-500">
                      <span>{t.category}</span>
                      <ScopeBadge scope={t.scope} />
                      {t.notes && <span className="hidden max-w-[260px] truncate xl:inline">· {t.notes}</span>}
                    </div>
                  </button>
                  <div className="col-start-2 flex flex-wrap items-center gap-x-4 gap-y-2 lg:contents">
                    <PersonChip role={t.assignee} />
                    <span className={`tnum text-xs lg:text-sm ${overdue ? 'font-medium text-danger-700' : 'text-ink-600'}`} title={formatDate(t.due)}>
                      {done ? formatDate(t.due, 'd MMM') : relativeDue(t.due)}
                    </span>
                    <span className={`flex items-center gap-1.5 text-xs font-medium ${priorityColor[t.priority]}`}>
                      <FlagIcon className="h-3.5 w-3.5" aria-hidden />
                      {priorityMeta[t.priority].label}
                    </span>
                    <span>
                      <Badge tone={st.tone} dot>{st.label}</Badge>
                    </span>
                    <span className="tnum text-xs text-ink-700 lg:text-right lg:text-sm">{t.budget ? `RWF ${formatCompact(t.budget)}` : '—'}</span>
                    <div className="ml-auto flex items-center gap-1 lg:ml-0 lg:justify-end">
                      <button type="button" onClick={() => openEdit(t)} aria-label={`Edit ${t.title}`} className="rounded p-1.5 text-ink-400 hover:bg-ivory-100 hover:text-ink">
                        <PencilIcon className="h-4 w-4" />
                      </button>
                      <button type="button" onClick={() => setDeleting(t)} aria-label={`Delete ${t.title}`} className="rounded p-1.5 text-ink-400 hover:bg-danger-50 hover:text-danger-700">
                        <Trash2Icon className="h-4 w-4" />
                      </button>
                    </div>
                  </div>
                </li>);

          })}
          </ul>
        }
      </section>

      <TaskFormModal open={formOpen} onClose={() => setFormOpen(false)} initial={editing} />
      <ConfirmDialog
        open={!!deleting}
        onClose={() => setDeleting(null)}
        onConfirm={() => {
          if (deleting) {
            deleteTask(deleting.id);
            toast('Task deleted', { description: deleting.title });
          }
        }}
        title="Delete this task?"
        description={`"${deleting?.title ?? ''}" will be removed for everyone who can see it.`} />
      
    </div>);

}