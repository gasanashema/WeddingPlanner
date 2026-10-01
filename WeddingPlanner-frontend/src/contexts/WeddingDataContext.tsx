import React, { createContext, useCallback, useContext, useMemo, useState } from 'react';
import { Expense, Guest, HomeItem, Task, Vendor } from '../types/wedding';
import { tasks as initialTasks } from '../data/tasks';
import { expenses as initialExpenses } from '../data/budget';
import { guests as initialGuests } from '../data/guests';
import { homeItems as initialHomeItems } from '../data/homePreparation';
import { vendors as initialVendors } from '../data/vendors';

interface WeddingDataValue {
  tasks: Task[];
  addTask: (t: Omit<Task, 'id'>) => void;
  updateTask: (id: string, patch: Partial<Task>) => void;
  deleteTask: (id: string) => void;
  expenses: Expense[];
  addExpense: (e: Omit<Expense, 'id'>) => void;
  guests: Guest[];
  addGuest: (g: Omit<Guest, 'id'>) => void;
  updateGuest: (id: string, patch: Partial<Guest>) => void;
  homeItems: HomeItem[];
  addHomeItem: (h: Omit<HomeItem, 'id'>) => void;
  updateHomeItem: (id: string, patch: Partial<HomeItem>) => void;
  deleteHomeItem: (id: string) => void;
  vendors: Vendor[];
  addVendor: (v: Omit<Vendor, 'id'>) => void;
}

const WeddingDataContext = createContext<WeddingDataValue | null>(null);
const uid = (p: string) => `${p}-${Math.random().toString(36).slice(2, 9)}`;

export function WeddingDataProvider({ children }: {children: React.ReactNode;}) {
  const [tasks, setTasks] = useState<Task[]>(initialTasks);
  const [expenses, setExpenses] = useState<Expense[]>(initialExpenses);
  const [guests, setGuests] = useState<Guest[]>(initialGuests);
  const [homeItems, setHomeItems] = useState<HomeItem[]>(initialHomeItems);
  const [vendors, setVendors] = useState<Vendor[]>(initialVendors);

  const addTask = useCallback((t: Omit<Task, 'id'>) => setTasks((p) => [{ ...t, id: uid('t') }, ...p]), []);
  const updateTask = useCallback(
    (id: string, patch: Partial<Task>) => setTasks((p) => p.map((t) => t.id === id ? { ...t, ...patch } : t)),
    []
  );
  const deleteTask = useCallback((id: string) => setTasks((p) => p.filter((t) => t.id !== id)), []);
  const addExpense = useCallback((e: Omit<Expense, 'id'>) => setExpenses((p) => [{ ...e, id: uid('e') }, ...p]), []);
  const addGuest = useCallback((g: Omit<Guest, 'id'>) => setGuests((p) => [{ ...g, id: uid('g') }, ...p]), []);
  const updateGuest = useCallback(
    (id: string, patch: Partial<Guest>) => setGuests((p) => p.map((g) => g.id === id ? { ...g, ...patch } : g)),
    []
  );
  const addHomeItem = useCallback((h: Omit<HomeItem, 'id'>) => setHomeItems((p) => [...p, { ...h, id: uid('h') }]), []);
  const updateHomeItem = useCallback(
    (id: string, patch: Partial<HomeItem>) =>
    setHomeItems((p) => p.map((h) => h.id === id ? { ...h, ...patch } : h)),
    []
  );
  const deleteHomeItem = useCallback((id: string) => setHomeItems((p) => p.filter((h) => h.id !== id)), []);
  const addVendor = useCallback((v: Omit<Vendor, 'id'>) => setVendors((p) => [{ ...v, id: uid('v') }, ...p]), []);

  const value = useMemo(
    () => ({
      tasks, addTask, updateTask, deleteTask,
      expenses, addExpense,
      guests, addGuest, updateGuest,
      homeItems, addHomeItem, updateHomeItem, deleteHomeItem,
      vendors, addVendor
    }),
    [tasks, expenses, guests, homeItems, vendors, addTask, updateTask, deleteTask, addExpense, addGuest, updateGuest, addHomeItem, updateHomeItem, deleteHomeItem, addVendor]
  );

  return <WeddingDataContext.Provider value={value}>{children}</WeddingDataContext.Provider>;
}

export function useWeddingData() {
  const ctx = useContext(WeddingDataContext);
  if (!ctx) throw new Error('useWeddingData must be used within WeddingDataProvider');
  return ctx;
}