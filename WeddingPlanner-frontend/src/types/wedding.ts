export type Role = 'bride' | 'groom' | 'bride-support' | 'groom-support';
export type Side = 'bride' | 'groom';
export type Scope = 'private-bride' | 'private-groom' | 'bride-side' | 'groom-side' | 'shared';
export type TaskStatus = 'not-started' | 'in-progress' | 'completed' | 'overdue';
export type Priority = 'low' | 'medium' | 'high';
export type BudgetCategory =
'Venue' |
'Food' |
'Drinks' |
'Decoration' |
'Clothing' |
'Transportation' |
'Photography' |
'Entertainment' |
'Invitations' |
'Home Preparation' |
'Other';

export interface Person {
  role: Role;
  name: string;
  firstName: string;
  initials: string;
  roleLabel: string;
  relation: string;
  side: Side;
  email: string;
  phone: string;
}

export interface Task {
  id: string;
  title: string;
  category: BudgetCategory;
  scope: Scope;
  assignee: Role;
  due: string;
  priority: Priority;
  status: TaskStatus;
  budget: number;
  notes: string;
}

export type PaymentMethod = 'MoMo' | 'Bank transfer' | 'Cash' | 'Card';

export interface Expense {
  id: string;
  title: string;
  category: BudgetCategory;
  scope: Scope;
  amount: number;
  date: string;
  paidBy: Role;
  method: PaymentMethod;
  vendor?: string;
}

export interface BudgetLine {
  category: BudgetCategory;
  scope: Scope;
  planned: number;
}

export type GuestSide = Side | 'both';
export type RsvpStatus = 'confirmed' | 'pending' | 'declined';

export interface Guest {
  id: string;
  name: string;
  side: GuestSide;
  group: string;
  phone: string;
  rsvp: RsvpStatus;
  table: number | null;
  plusOnes: number;
  invitationOpened: boolean;
}

export interface SeatingTable {
  id: number;
  name: string;
  note: string;
  capacity: number;
  reserved: number;
}

export type VendorCategory =
'Venue' |
'Caterer' |
'Decorator' |
'Photographer' |
'Videographer' |
'DJ / Entertainment' |
'Makeup' |
'Transport' |
'Other';

export type PaymentStatus = 'paid' | 'deposit' | 'unpaid';
export type BookingStatus = 'booked' | 'pending' | 'shortlisted';

export interface Vendor {
  id: string;
  name: string;
  category: VendorCategory;
  contactName: string;
  phone: string;
  email: string;
  cost: number;
  paid: number;
  paymentStatus: PaymentStatus;
  bookingStatus: BookingStatus;
  notes: string;
}

export interface HomeItem {
  id: string;
  name: string;
  category: string;
  side: Side;
  quantity: number;
  budget: number;
  deadline: string;
  assignee: Role;
  completed: boolean;
  notes: string;
}

export interface HomeTemplateItem {
  name: string;
  category: string;
  budget: number;
}

export type MilestoneKind = 'Tradition' | 'Planning' | 'Ceremony' | 'Deadline';

export interface Milestone {
  id: string;
  title: string;
  date: string;
  description: string;
  scope: Scope;
  kind: MilestoneKind;
  location?: string;
}

export interface ScheduleItem {
  time: string;
  title: string;
  location: string;
  owner: string;
}

export interface SharedArea {
  id: string;
  name: string;
  icon: string;
  nextStep: string;
  progress: number;
  assignee: Role;
  deadline: string;
  budget: number;
  spent: number;
  status: TaskStatus;
}

export interface NotificationItem {
  id: string;
  title: string;
  body: string;
  time: string;
  scope: Scope;
  unread: boolean;
}

export interface InvitationActivity {
  id: string;
  guest: string;
  action: 'opened' | 'confirmed' | 'declined' | 'sent';
  time: string;
}