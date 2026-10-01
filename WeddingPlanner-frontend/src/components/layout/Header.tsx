import React from 'react';
import { CalendarIcon, MenuIcon } from 'lucide-react';
import { wedding } from '../../data/wedding';
import { daysFromToday, formatDate } from '../../utils/format';
import { NotificationsMenu } from './NotificationsMenu';
import { ProfileMenu } from './ProfileMenu';

export function Header({ onMenu }: {onMenu: () => void;}) {
  const days = daysFromToday(wedding.date);
  return (
    <header className="sticky top-0 z-30 border-b border-line bg-ivory">
      <div className="flex h-16 items-center gap-3 px-4 sm:px-6 lg:px-8">
        <button
          type="button"
          onClick={onMenu}
          aria-label="Open navigation"
          className="-ml-1 flex h-9 w-9 items-center justify-center rounded-md text-ink-600 hover:bg-ivory-100 lg:hidden">
          
          <MenuIcon className="h-5 w-5" />
        </button>
        <div className="min-w-0 flex-1">
          <p className="truncate font-serif text-lg font-semibold text-ink sm:text-xl">{wedding.name}</p>
          <p className="flex items-center gap-1.5 text-xs text-ink-500">
            <CalendarIcon className="h-3.5 w-3.5 text-gold-600" aria-hidden />
            <span className="truncate">{formatDate(wedding.date, 'EEEE, d MMMM yyyy')}</span>
          </p>
        </div>
        <span className="tnum hidden whitespace-nowrap rounded border border-gold-200 bg-gold-50 px-2.5 py-1 text-xs font-medium text-gold-700 sm:inline-flex">
          {days} days to go
        </span>
        <NotificationsMenu />
        <ProfileMenu />
      </div>
    </header>);

}