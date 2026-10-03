import React, { createContext, useCallback, useContext, useEffect, useMemo, useState } from 'react';
import { Expense, Guest, HomeItem, Task, Vendor } from '../types/wedding';
import { tasks as initialTasks } from '../data/tasks';
import { expenses as initialExpenses } from '../data/budget';
import { guests as initialGuests } from '../data/guests';
import { homeItems as initialHomeItems } from '../data/homePreparation';
import { vendors as initialVendors } from '../data/vendors';
import { weddingApi, WeddingDto, CeremonyDto } from '../api/weddingApi';
import { homePrepApi, mapDtoToHomeItem, mapCategoryToBackend } from '../api/homePrepApi';
import { templateApi } from '../api/templateApi';
import { budgetApi, BudgetSummaryDto, mapExpenseDtoToExpense } from '../api/budgetApi';
import { useAuth } from './AuthContext';

interface WeddingDataValue {
  activeWedding: WeddingDto | null;
  loadingWedding: boolean;
  ceremonies: CeremonyDto[];
  fetchActiveWedding: () => Promise<void>;
  setActiveWedding: (w: WeddingDto | null) => void;
  tasks: Task[];
  addTask: (t: Omit<Task, 'id'>) => void;
  updateTask: (id: string, patch: Partial<Task>) => void;
  deleteTask: (id: string) => void;
  expenses: Expense[];
  budgetSummary: BudgetSummaryDto | null;
  fetchBudgetSummary: () => Promise<void>;
  addExpense: (e: Omit<Expense, 'id'>) => Promise<void>;
  deleteExpense: (id: string) => Promise<void>;
  guests: Guest[];
  addGuest: (g: Omit<Guest, 'id'>) => void;
  updateGuest: (id: string, patch: Partial<Guest>) => void;
  homeItems: HomeItem[];
  fetchHomeItems: () => Promise<void>;
  addHomeItem: (h: Omit<HomeItem, 'id'>) => Promise<void>;
  updateHomeItem: (id: string, patch: Partial<HomeItem>) => Promise<void>;
  deleteHomeItem: (id: string) => Promise<void>;
  applyTemplate: (templateId: number, side?: 'BRIDE_SIDE' | 'GROOM_SIDE' | 'SHARED') => Promise<void>;
  vendors: Vendor[];
  addVendor: (v: Omit<Vendor, 'id'>) => void;
}

const WeddingDataContext = createContext<WeddingDataValue | null>(null);
const uid = (p: string) => `${p}-${Math.random().toString(36).slice(2, 9)}`;

export function WeddingDataProvider({ children }: { children: React.ReactNode }) {
  const { user } = useAuth();
  const [activeWedding, setActiveWedding] = useState<WeddingDto | null>(null);
  const [ceremonies, setCeremonies] = useState<CeremonyDto[]>([]);
  const [loadingWedding, setLoadingWedding] = useState<boolean>(false);

  const [tasks, setTasks] = useState<Task[]>(initialTasks);
  const [expenses, setExpenses] = useState<Expense[]>(initialExpenses);
  const [budgetSummary, setBudgetSummary] = useState<BudgetSummaryDto | null>(null);
  const [guests, setGuests] = useState<Guest[]>(initialGuests);
  const [homeItems, setHomeItems] = useState<HomeItem[]>(initialHomeItems);
  const [vendors, setVendors] = useState<Vendor[]>(initialVendors);

  const fetchBudgetSummary = useCallback(async () => {
    if (!user) return;
    try {
      const res = await budgetApi.getBudgetSummary();
      if (res.success && res.data) {
        setBudgetSummary(res.data);
        if (res.data.expenses && res.data.expenses.length > 0) {
          const mapped = res.data.expenses.map(mapExpenseDtoToExpense);
          setExpenses(mapped);
        }
      }
    } catch {
      // Fallback to initial local expenses
    }
  }, [user]);

  const fetchHomeItems = useCallback(async () => {
    if (!user) return;
    try {
      const res = await homePrepApi.getHomePreps();
      if (res.success && res.data && res.data.length > 0) {
        const mapped = res.data.map(mapDtoToHomeItem);
        setHomeItems(mapped);
      }
    } catch {
      // Fallback to local default if backend fetch fails
    }
  }, [user]);

  const fetchActiveWedding = useCallback(async () => {
    if (!user) {
      setActiveWedding(null);
      setCeremonies([]);
      return;
    }
    setLoadingWedding(true);
    try {
      const res = await weddingApi.getMyWedding();
      if (res.success && res.data) {
        setActiveWedding(res.data);
        if (res.data.ceremonies) {
          setCeremonies(res.data.ceremonies);
        }
        await Promise.all([fetchHomeItems(), fetchBudgetSummary()]);
      }
    } catch {
      setActiveWedding(null);
      setCeremonies([]);
    } finally {
      setLoadingWedding(false);
    }
  }, [user, fetchHomeItems, fetchBudgetSummary]);

  useEffect(() => {
    fetchActiveWedding();
  }, [fetchActiveWedding]);

  const addTask = useCallback((t: Omit<Task, 'id'>) => setTasks((p) => [{ ...t, id: uid('t') }, ...p]), []);
  const updateTask = useCallback(
    (id: string, patch: Partial<Task>) => setTasks((p) => p.map((t) => t.id === id ? { ...t, ...patch } : t)),
    []
  );
  const deleteTask = useCallback((id: string) => setTasks((p) => p.filter((t) => t.id !== id)), []);

  const addExpense = useCallback(
    async (e: Omit<Expense, 'id'>) => {
      if (activeWedding) {
        try {
          const scope =
            e.scope === 'private-bride'
              ? ('BRIDE_PRIVATE' as const)
              : e.scope === 'private-groom'
              ? ('GROOM_PRIVATE' as const)
              : ('SHARED' as const);

          const payload = {
            weddingId: activeWedding.id,
            title: e.title,
            amountRwf: e.amount,
            dateSpent: e.date || new Date().toISOString().split('T')[0],
            visibilityScope: scope,
          };
          const res = await budgetApi.createExpense(payload);
          if (res.success && res.data) {
            const newExpense = mapExpenseDtoToExpense(res.data);
            setExpenses((p) => [newExpense, ...p]);
            await fetchBudgetSummary();
            return;
          }
        } catch {
          // Fall back to local update if network error
        }
      }
      setExpenses((p) => [{ ...e, id: uid('e') }, ...p]);
    },
    [activeWedding, fetchBudgetSummary]
  );

  const deleteExpense = useCallback(
    async (id: string) => {
      const numId = Number(id);
      if (!isNaN(numId) && activeWedding) {
        try {
          await budgetApi.deleteExpense(numId);
          await fetchBudgetSummary();
        } catch {
          // Fall back to local update
        }
      }
      setExpenses((p) => p.filter((exp) => exp.id !== id));
    },
    [activeWedding, fetchBudgetSummary]
  );

  const addGuest = useCallback((g: Omit<Guest, 'id'>) => setGuests((p) => [{ ...g, id: uid('g') }, ...p]), []);
  const updateGuest = useCallback(
    (id: string, patch: Partial<Guest>) => setGuests((p) => p.map((g) => g.id === id ? { ...g, ...patch } : g)),
    []
  );

  const addHomeItem = useCallback(
    async (h: Omit<HomeItem, 'id'>) => {
      if (activeWedding) {
        try {
          const payload = {
            weddingId: activeWedding.id,
            category: mapCategoryToBackend(h.category),
            itemName: h.name,
            side: h.side === 'bride' ? ('BRIDE_SIDE' as const) : ('GROOM_SIDE' as const),
            budgetRwf: h.budget,
            dueDate: h.deadline,
            isCompleted: h.completed,
            notes: h.notes,
          };
          const res = await homePrepApi.createHomePrep(payload);
          if (res.success && res.data) {
            const newItem = mapDtoToHomeItem(res.data);
            setHomeItems((p) => [...p, newItem]);
            return;
          }
        } catch {
          // Fall back to local update if network error
        }
      }
      setHomeItems((p) => [...p, { ...h, id: uid('h') }]);
    },
    [activeWedding]
  );

  const updateHomeItem = useCallback(
    async (id: string, patch: Partial<HomeItem>) => {
      const numId = Number(id);
      if (!isNaN(numId) && activeWedding) {
        try {
          const payload: Record<string, unknown> = {};
          if (patch.name) payload.itemName = patch.name;
          if (patch.category) payload.category = mapCategoryToBackend(patch.category);
          if (patch.side) payload.side = patch.side === 'bride' ? 'BRIDE_SIDE' : 'GROOM_SIDE';
          if (patch.budget !== undefined) payload.budgetRwf = patch.budget;
          if (patch.deadline) payload.dueDate = patch.deadline;
          if (patch.completed !== undefined) payload.isCompleted = patch.completed;
          if (patch.notes !== undefined) payload.notes = patch.notes;

          const res = await homePrepApi.updateHomePrep(numId, payload);
          if (res.success && res.data) {
            const updated = mapDtoToHomeItem(res.data);
            setHomeItems((p) => p.map((item) => (item.id === id ? updated : item)));
            return;
          }
        } catch {
          // Fall back to local update if network error
        }
      }
      setHomeItems((p) => p.map((h) => (h.id === id ? { ...h, ...patch } : h)));
    },
    [activeWedding]
  );

  const deleteHomeItem = useCallback(
    async (id: string) => {
      const numId = Number(id);
      if (!isNaN(numId) && activeWedding) {
        try {
          await homePrepApi.deleteHomePrep(numId);
        } catch {
          // Fall back to local delete
        }
      }
      setHomeItems((p) => p.filter((h) => h.id !== id));
    },
    [activeWedding]
  );

  const applyTemplate = useCallback(
    async (templateId: number, side?: 'BRIDE_SIDE' | 'GROOM_SIDE' | 'SHARED') => {
      try {
        const res = await templateApi.applyTemplate(templateId, { side });
        if (res.success && res.data) {
          const newItems = res.data.map(mapDtoToHomeItem);
          setHomeItems((prev) => [...prev, ...newItems]);
        }
      } catch {
        // Handle error silently or via caller
      }
    },
    []
  );

  const addVendor = useCallback((v: Omit<Vendor, 'id'>) => setVendors((p) => [{ ...v, id: uid('v') }, ...p]), []);

  const value = useMemo(
    () => ({
      activeWedding,
      loadingWedding,
      ceremonies,
      fetchActiveWedding,
      setActiveWedding,
      tasks, addTask, updateTask, deleteTask,
      expenses, budgetSummary, fetchBudgetSummary, addExpense, deleteExpense,
      guests, addGuest, updateGuest,
      homeItems, fetchHomeItems, addHomeItem, updateHomeItem, deleteHomeItem, applyTemplate,
      vendors, addVendor
    }),
    [
      activeWedding, loadingWedding, ceremonies, fetchActiveWedding,
      tasks, expenses, budgetSummary, fetchBudgetSummary, guests, homeItems, fetchHomeItems, vendors,
      addTask, updateTask, deleteTask, addExpense, deleteExpense, addGuest, updateGuest,
      addHomeItem, updateHomeItem, deleteHomeItem, applyTemplate, addVendor
    ]
  );

  return <WeddingDataContext.Provider value={value}>{children}</WeddingDataContext.Provider>;
}

export function useWeddingData() {
  const ctx = useContext(WeddingDataContext);
  if (!ctx) throw new Error('useWeddingData must be used within WeddingDataProvider');
  return ctx;
}