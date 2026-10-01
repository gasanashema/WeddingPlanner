import { BudgetCategory, BudgetLine, Expense } from '../types/wedding';

export const budgetCategories: BudgetCategory[] = [
'Venue',
'Food',
'Drinks',
'Decoration',
'Clothing',
'Transportation',
'Photography',
'Entertainment',
'Invitations',
'Home Preparation',
'Other'];


export const budgetLines: BudgetLine[] = [
{ category: 'Venue', scope: 'shared', planned: 4500000 },
{ category: 'Food', scope: 'shared', planned: 5200000 },
{ category: 'Drinks', scope: 'shared', planned: 1800000 },
{ category: 'Decoration', scope: 'shared', planned: 2800000 },
{ category: 'Photography', scope: 'shared', planned: 1800000 },
{ category: 'Entertainment', scope: 'shared', planned: 1200000 },
{ category: 'Invitations', scope: 'shared', planned: 250000 },
{ category: 'Transportation', scope: 'shared', planned: 600000 },
{ category: 'Other', scope: 'shared', planned: 400000 },
{ category: 'Clothing', scope: 'bride-side', planned: 1200000 },
{ category: 'Home Preparation', scope: 'bride-side', planned: 1650000 },
{ category: 'Other', scope: 'bride-side', planned: 800000 },
{ category: 'Clothing', scope: 'private-bride', planned: 900000 },
{ category: 'Home Preparation', scope: 'groom-side', planned: 5400000 },
{ category: 'Other', scope: 'groom-side', planned: 1500000 },
{ category: 'Transportation', scope: 'groom-side', planned: 450000 },
{ category: 'Entertainment', scope: 'groom-side', planned: 350000 },
{ category: 'Clothing', scope: 'private-groom', planned: 1300000 }];


export const expenses: Expense[] = [
{ id: 'e1', title: 'Venue deposit (30%)', category: 'Venue', scope: 'shared', amount: 1350000, date: '2027-05-30', paidBy: 'groom', method: 'Bank transfer', vendor: 'Intare Gardens' },
{ id: 'e2', title: 'Photography booking', category: 'Photography', scope: 'shared', amount: 450000, date: '2027-05-15', paidBy: 'bride', method: 'MoMo', vendor: 'Lens of Kigali' },
{ id: 'e3', title: 'Videography deposit', category: 'Photography', scope: 'shared', amount: 400000, date: '2027-05-25', paidBy: 'bride', method: 'MoMo', vendor: 'Isimbi Films' },
{ id: 'e4', title: 'Decoration deposit', category: 'Decoration', scope: 'shared', amount: 1000000, date: '2027-05-27', paidBy: 'bride', method: 'Bank transfer', vendor: 'Imena Décor' },
{ id: 'e5', title: 'Church booking fee', category: 'Other', scope: 'shared', amount: 150000, date: '2027-04-20', paidBy: 'groom', method: 'Cash', vendor: 'St. Michel Parish' },
{ id: 'e6', title: 'MC booking deposit', category: 'Entertainment', scope: 'shared', amount: 300000, date: '2027-05-20', paidBy: 'groom', method: 'MoMo', vendor: 'MC Gatete' },
{ id: 'e7', title: 'Catering tasting session', category: 'Food', scope: 'shared', amount: 60000, date: '2027-05-22', paidBy: 'groom', method: 'MoMo', vendor: 'Umusambi Catering' },
{ id: 'e8', title: 'Drinks advance order', category: 'Drinks', scope: 'shared', amount: 700000, date: '2027-05-31', paidBy: 'groom', method: 'Bank transfer', vendor: 'Kigali Beverage Depot' },
{ id: 'e9', title: 'Wedding dress', category: 'Clothing', scope: 'private-bride', amount: 480000, date: '2027-05-10', paidBy: 'bride', method: 'Card', vendor: 'Atelier Muhoza' },
{ id: 'e10', title: 'Makeup artist deposit', category: 'Clothing', scope: 'private-bride', amount: 150000, date: '2027-04-28', paidBy: 'bride', method: 'MoMo', vendor: 'Keza Beauty Lounge' },
{ id: 'e11', title: 'Imishanana fabric', category: 'Clothing', scope: 'bride-side', amount: 420000, date: '2027-05-26', paidBy: 'bride-support', method: 'Cash' },
{ id: 'e12', title: 'Plates & cutlery set', category: 'Home Preparation', scope: 'bride-side', amount: 185000, date: '2027-05-18', paidBy: 'bride-support', method: 'MoMo' },
{ id: 'e13', title: 'Bed sheets & curtains', category: 'Home Preparation', scope: 'bride-side', amount: 260000, date: '2027-05-29', paidBy: 'bride', method: 'MoMo' },
{ id: 'e14', title: 'Gusaba gift baskets (deposit)', category: 'Other', scope: 'bride-side', amount: 210000, date: '2027-05-21', paidBy: 'bride-support', method: 'Cash' },
{ id: 'e15', title: 'House rent — 3 months, Kimironko', category: 'Home Preparation', scope: 'groom-side', amount: 900000, date: '2027-05-01', paidBy: 'groom', method: 'Bank transfer' },
{ id: 'e16', title: 'Refrigerator', category: 'Home Preparation', scope: 'groom-side', amount: 780000, date: '2027-05-24', paidBy: 'groom', method: 'Card' },
{ id: 'e17', title: 'Sofa set', category: 'Home Preparation', scope: 'groom-side', amount: 1150000, date: '2027-05-28', paidBy: 'groom-support', method: 'Bank transfer' },
{ id: 'e18', title: 'Inkwano — first cow', category: 'Other', scope: 'groom-side', amount: 800000, date: '2027-05-19', paidBy: 'groom-support', method: 'Cash' },
{ id: 'e19', title: 'Suit fabric', category: 'Clothing', scope: 'private-groom', amount: 320000, date: '2027-05-12', paidBy: 'groom', method: 'MoMo' },
{ id: 'e20', title: 'Wedding bands', category: 'Clothing', scope: 'private-groom', amount: 700000, date: '2027-04-30', paidBy: 'groom', method: 'Card' }];