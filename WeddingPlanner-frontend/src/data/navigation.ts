import {
  ArmchairIcon,
  CalendarRangeIcon,
  HouseIcon,
  LayoutDashboardIcon,
  Link2Icon,
  ListChecksIcon,
  MailIcon,
  NotebookPenIcon,
  StoreIcon,
  UsersIcon,
  WalletIcon } from
'lucide-react';
import type { LucideIcon } from 'lucide-react';

export interface NavItem {
  label: string;
  path: string;
  icon: LucideIcon;
}

export const navItems: NavItem[] = [
{ label: 'Overview', path: '/', icon: LayoutDashboardIcon },
{ label: 'My Planning', path: '/my-planning', icon: NotebookPenIcon },
{ label: 'Shared Wedding', path: '/shared', icon: Link2Icon },
{ label: 'Tasks', path: '/tasks', icon: ListChecksIcon },
{ label: 'Budget & Expenses', path: '/budget', icon: WalletIcon },
{ label: 'Guests & RSVP', path: '/guests', icon: UsersIcon },
{ label: 'Seating', path: '/seating', icon: ArmchairIcon },
{ label: 'Timeline', path: '/timeline', icon: CalendarRangeIcon },
{ label: 'Vendors', path: '/vendors', icon: StoreIcon },
{ label: 'Home Preparation', path: '/home-preparation', icon: HouseIcon },
{ label: 'Invitations', path: '/invitations', icon: MailIcon }];