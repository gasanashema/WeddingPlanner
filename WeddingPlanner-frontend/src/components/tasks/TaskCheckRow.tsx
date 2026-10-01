import React from 'react';
import { toast } from 'sonner';
import { Task } from '../../types/wedding';
import { useWeddingData } from '../../contexts/WeddingDataContext';
import { daysFromToday, relativeDue } from '../../utils/format';
import { CheckButton } from '../ui/CheckButton';
import { ScopeBadge } from '../ui/ScopeBadge';
import { Avatar } from '../ui/Avatar';

export function TaskCheckRow({ task, showScope = true }: {task: Task;showScope?: boolean;}) {
  const { updateTask } = useWeddingData();
  const done = task.status === 'completed';
  const overdue = !done && (task.status === 'overdue' || daysFromToday(task.due) < 0);

  const toggle = () => {
    updateTask(task.id, { status: done ? 'in-progress' : 'completed' });
    if (!done) toast.success('Task completed', { description: task.title });
  };

  return (
    <li className="flex items-center gap-3 py-3">
      <CheckButton checked={done} onChange={toggle} label={`Mark "${task.title}" ${done ? 'not done' : 'done'}`} />
      <div className="min-w-0 flex-1">
        <p className={`truncate text-sm ${done ? 'text-ink-500 line-through' : 'text-ink'}`}>{task.title}</p>
        <div className="mt-1 flex flex-wrap items-center gap-2 text-xs text-ink-500">
          <span>{task.category}</span>
          {showScope && <ScopeBadge scope={task.scope} />}
        </div>
      </div>
      <Avatar role={task.assignee} size="xs" className="hidden sm:inline-flex" />
      <span className={`tnum w-24 shrink-0 text-right text-xs ${overdue ? 'font-medium text-danger-700' : 'text-ink-500'}`}>
        {done ? 'Done' : relativeDue(task.due)}
      </span>
    </li>);

}