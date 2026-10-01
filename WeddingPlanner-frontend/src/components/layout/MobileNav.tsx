import React from 'react';
import { NavLink } from 'react-router-dom';
import { LayoutDashboardIcon, ListChecksIcon, MenuIcon, PlusIcon, WalletIcon } from 'lucide-react';
import { useQuickAdd } from '../../contexts/QuickAddContext';

export function MobileNav({ onMore }: {onMore: () => void;}) {
  const { openSheet } = useQuickAdd();
  const itemClass = ({ isActive }: {isActive: boolean;}) =>
  `flex flex-1 flex-col items-center gap-1 py-2 text-[11px] font-medium ${isActive ? 'text-wine-700' : 'text-ink-500'}`;

  return (
    <nav aria-label="Mobile" className="fixed inset-x-0 bottom-0 z-30 border-t border-line bg-surface pb-[env(safe-area-inset-bottom)] lg:hidden">
      <div className="flex items-end px-2">
        <NavLink to="/" end className={itemClass}>
          <LayoutDashboardIcon className="h-5 w-5" aria-hidden />
          Overview
        </NavLink>
        <NavLink to="/tasks" className={itemClass}>
          <ListChecksIcon className="h-5 w-5" aria-hidden />
          Tasks
        </NavLink>
        <div className="flex flex-1 justify-center">
          <button
            type="button"
            onClick={openSheet}
            aria-label="Quick add"
            className="-mt-5 mb-1 flex h-12 w-12 items-center justify-center rounded-full bg-wine-700 text-white shadow-pop transition-transform duration-150 active:scale-95">
            
            <PlusIcon className="h-5 w-5" />
          </button>
        </div>
        <NavLink to="/budget" className={itemClass}>
          <WalletIcon className="h-5 w-5" aria-hidden />
          Budget
        </NavLink>
        <button type="button" onClick={onMore} className="flex flex-1 flex-col items-center gap-1 py-2 text-[11px] font-medium text-ink-500">
          <MenuIcon className="h-5 w-5" aria-hidden />
          More
        </button>
      </div>
    </nav>);

}