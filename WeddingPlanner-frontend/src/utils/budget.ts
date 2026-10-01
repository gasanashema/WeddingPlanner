import { BudgetCategory, BudgetLine, Expense } from '../types/wedding';

export interface CategoryTotal {
  category: BudgetCategory;
  planned: number;
  spent: number;
}

export function aggregateByCategory(lines: BudgetLine[], expenses: Expense[]): CategoryTotal[] {
  const map = new Map<BudgetCategory, CategoryTotal>();
  lines.forEach((l) => {
    const cur = map.get(l.category) ?? { category: l.category, planned: 0, spent: 0 };
    cur.planned += l.planned;
    map.set(l.category, cur);
  });
  expenses.forEach((e) => {
    const cur = map.get(e.category) ?? { category: e.category, planned: 0, spent: 0 };
    cur.spent += e.amount;
    map.set(e.category, cur);
  });
  return Array.from(map.values()).sort((a, b) => b.planned - a.planned);
}

export const shortCategory = (c: BudgetCategory) =>
c === 'Home Preparation' ? 'Home' : c === 'Transportation' ? 'Transport' : c === 'Entertainment' ? 'Music' : c === 'Photography' ? 'Photo' : c === 'Decoration' ? 'Décor' : c === 'Invitations' ? 'Invites' : c;