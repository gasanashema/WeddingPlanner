import React, { createContext, useContext, useMemo, useState } from 'react';
import { ListChecksIcon, ReceiptIcon, UserPlusIcon } from 'lucide-react';
import { Scope } from '../types/wedding';
import { Modal } from '../components/ui/Modal';
import { TaskFormModal } from '../components/forms/TaskFormModal';
import { ExpenseFormModal } from '../components/forms/ExpenseFormModal';
import { GuestFormModal } from '../components/forms/GuestFormModal';

type ActiveForm = 'task' | 'expense' | 'guest' | null;

interface QuickAddValue {
  openTask: (scope?: Scope) => void;
  openExpense: () => void;
  openGuest: () => void;
  openSheet: () => void;
}

const QuickAddContext = createContext<QuickAddValue | null>(null);

export function QuickAddProvider({ children }: {children: React.ReactNode;}) {
  const [active, setActive] = useState<ActiveForm>(null);
  const [taskScope, setTaskScope] = useState<Scope | undefined>(undefined);
  const [sheetOpen, setSheetOpen] = useState(false);

  const value = useMemo<QuickAddValue>(
    () => ({
      openTask: (scope?: Scope) => {
        setTaskScope(scope);
        setActive('task');
      },
      openExpense: () => setActive('expense'),
      openGuest: () => setActive('guest'),
      openSheet: () => setSheetOpen(true)
    }),
    []
  );

  const sheetActions = [
  { label: 'Add a task', description: 'Something to do before the big day', icon: ListChecksIcon, onClick: () => value.openTask() },
  { label: 'Log an expense', description: 'Record a payment you just made', icon: ReceiptIcon, onClick: value.openExpense },
  { label: 'Add a guest', description: 'Add someone to the guest list', icon: UserPlusIcon, onClick: value.openGuest }];


  return (
    <QuickAddContext.Provider value={value}>
      {children}
      <TaskFormModal open={active === 'task'} onClose={() => setActive(null)} defaultScope={taskScope} />
      <ExpenseFormModal open={active === 'expense'} onClose={() => setActive(null)} />
      <GuestFormModal open={active === 'guest'} onClose={() => setActive(null)} />
      <Modal open={sheetOpen} onClose={() => setSheetOpen(false)} title="Quick add">
        <div className="space-y-2">
          {sheetActions.map((a) =>
          <button
            key={a.label}
            type="button"
            onClick={() => {
              setSheetOpen(false);
              a.onClick();
            }}
            className="flex w-full items-center gap-3 rounded-md border border-line bg-white p-3.5 text-left transition-colors duration-150 hover:bg-ivory">
            
              <span className="flex h-10 w-10 items-center justify-center rounded-md bg-wine-50 text-wine-700">
                <a.icon className="h-5 w-5" aria-hidden />
              </span>
              <span>
                <span className="block text-sm font-medium text-ink">{a.label}</span>
                <span className="block text-xs text-ink-500">{a.description}</span>
              </span>
            </button>
          )}
        </div>
      </Modal>
    </QuickAddContext.Provider>);

}

export function useQuickAdd() {
  const ctx = useContext(QuickAddContext);
  if (!ctx) throw new Error('useQuickAdd must be used within QuickAddProvider');
  return ctx;
}