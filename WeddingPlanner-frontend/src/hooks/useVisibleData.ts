import { useMemo } from 'react';
import { useRole } from '../contexts/RoleContext';
import { useWeddingData } from '../contexts/WeddingDataContext';
import { budgetLines } from '../data/budget';

export function useVisibleData() {
  const { canSee } = useRole();
  const { tasks, expenses } = useWeddingData();

  return useMemo(() => {
    const vTasks = tasks.filter((t) => canSee(t.scope));
    const vExpenses = expenses.filter((e) => canSee(e.scope));
    const vLines = budgetLines.filter((l) => canSee(l.scope));
    const planned = vLines.reduce((s, l) => s + l.planned, 0);
    const spent = vExpenses.reduce((s, e) => s + e.amount, 0);
    const completedCount = vTasks.filter((t) => t.status === 'completed').length;
    const progress = vTasks.length ? Math.round(completedCount / vTasks.length * 100) : 0;
    return { tasks: vTasks, expenses: vExpenses, budgetLines: vLines, planned, spent, completedCount, progress };
  }, [tasks, expenses, canSee]);
}