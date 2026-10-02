import React from 'react';
import { NavLink } from 'react-router-dom';
import { LockIcon, PaletteIcon } from 'lucide-react';
import { useRole } from '../../contexts/RoleContext';
import { navItems } from '../../data/navigation';
import { wedding } from '../../data/wedding';
import { canAccessPath } from '../../utils/permissions';
import { daysFromToday, formatDate } from '../../utils/format';
import { Avatar } from '../ui/Avatar';

export function Sidebar({ onNavigate }: {onNavigate?: () => void;}) {
  const { role, person, side } = useRole();
  const days = daysFromToday(wedding.date);

  const linkClass = ({ isActive }: {isActive: boolean;}) =>
  `group flex h-9 items-center gap-3 rounded-md px-3 text-sm transition-colors duration-150 ${
  isActive ? 'bg-wine-50 font-medium text-wine-800' : 'text-ink-600 hover:bg-ivory-100 hover:text-ink'}`;


  return (
    <div className="flex h-full w-64 flex-col border-r border-line bg-surface">
      <div className="flex items-center gap-3 px-5 pb-4 pt-5">
        <img src="/logo.png" alt="Ubukwe Logo" className="h-10 w-auto object-contain" />
        <div>
          <p className="font-serif text-[17px] font-bold leading-none text-ink">Ubukwe</p>
          <p className="mt-1 text-[11px] text-ink-500">Wedding planning</p>
        </div>
      </div>

      <div className="mx-3 mb-4 rounded-md border border-line bg-ivory px-3.5 py-3">
        <p className="font-serif text-[15px] text-ink">{wedding.couple}</p>
        <p className="mt-0.5 text-xs text-ink-500">
          {formatDate(wedding.date)} · <span className="font-medium text-gold-700">{days} days to go</span>
        </p>
      </div>

      <nav aria-label="Main" className="flex-1 space-y-0.5 overflow-y-auto px-3 pb-4">
        {navItems.map((item) => {
          const locked = !canAccessPath(role, item.path);
          return (
            <NavLink key={item.path} to={item.path} end={item.path === '/'} onClick={onNavigate} className={linkClass}>
              {({ isActive }) =>
              <>
                  <item.icon className={`h-4 w-4 shrink-0 ${isActive ? 'text-wine-700' : 'text-ink-400 group-hover:text-ink-600'}`} aria-hidden />
                  <span className="flex-1 truncate">{item.label}</span>
                  {item.path === '/my-planning' &&
                <span
                  className={`rounded px-1.5 py-0.5 text-[10px] font-semibold ${
                  side === 'bride' ? 'bg-bride-50 text-bride-700' : 'bg-groom-50 text-groom-700'}`
                  }>
                  
                      {side === 'bride' ? 'Bride side' : 'Groom side'}
                    </span>
                }
                  {locked && <LockIcon className="h-3.5 w-3.5 text-ink-400" aria-label="Couple only" />}
                </>
              }
            </NavLink>);

        })}
      </nav>

      <div className="space-y-2 border-t border-line p-3">
        <NavLink to="/design-system" onClick={onNavigate} className={linkClass}>
          <PaletteIcon className="h-4 w-4 text-ink-400" aria-hidden />
          <span>Design system</span>
        </NavLink>
        <div className="flex items-center gap-3 rounded-md px-2 py-2">
          <Avatar role={role} size="sm" />
          <div className="min-w-0">
            <p className="truncate text-sm font-medium text-ink">{person.name}</p>
            <p className="truncate text-xs text-ink-500">{person.roleLabel}</p>
          </div>
        </div>
      </div>
    </div>);

}