import React, { useCallback, useState } from 'react';
import { AnimatePresence, motion } from 'framer-motion';
import { ChevronDownIcon, LockIcon } from 'lucide-react';
import { useRole } from '../../contexts/RoleContext';
import { useClickOutside } from '../../hooks/useClickOutside';
import { EASE } from '../../utils/ui';
import { Scope } from '../../types/wedding';
import { Avatar } from '../ui/Avatar';
import { ScopeBadge } from '../ui/ScopeBadge';

const allScopes: Scope[] = ['private-bride', 'private-groom', 'bride-side', 'groom-side', 'shared'];

export function ProfileMenu() {
  const { role, person, visibleScopes } = useRole();
  const [open, setOpen] = useState(false);
  const close = useCallback(() => setOpen(false), []);
  const ref = useClickOutside<HTMLDivElement>(open, close);
  const hidden = allScopes.filter((s) => !visibleScopes.includes(s));
  const hiddenLabels = Array.from(new Set(hidden.map((s) => s === 'private-bride' ? "Aline's private" : s === 'private-groom' ? "Shema's private" : s === 'bride-side' ? 'Bride-side' : s === 'groom-side' ? 'Groom-side' : 'Shared')));

  return (
    <div ref={ref} className="relative">
      <button
        type="button"
        onClick={() => setOpen((o) => !o)}
        aria-expanded={open}
        aria-label="Profile and access"
        className="flex items-center gap-2 rounded-md py-1 pl-1 pr-2 transition-colors duration-150 hover:bg-ivory-100">
        
        <Avatar role={role} size="sm" />
        <span className="hidden text-left md:block">
          <span className="block text-sm font-medium leading-tight text-ink">{person.firstName}</span>
          <span className="block text-[11px] leading-tight text-ink-500">{person.roleLabel}</span>
        </span>
        <ChevronDownIcon className="hidden h-4 w-4 text-ink-400 md:block" aria-hidden />
      </button>
      <AnimatePresence>
        {open &&
        <motion.div
          initial={{ opacity: 0, y: -4, scale: 0.98 }}
          animate={{ opacity: 1, y: 0, scale: 1 }}
          exit={{ opacity: 0, y: -4, scale: 0.98 }}
          transition={{ duration: 0.16, ease: EASE }}
          style={{ transformOrigin: 'top right' }}
          className="absolute right-0 top-12 z-40 w-72 rounded-lg border border-line bg-white p-4 shadow-pop">
          
            <div className="flex items-center gap-3">
              <Avatar role={role} size="md" />
              <div className="min-w-0">
                <p className="truncate text-sm font-semibold text-ink">{person.name}</p>
                <p className="truncate text-xs text-ink-500">{person.email}</p>
              </div>
            </div>
            <div className="mt-4 border-t border-line pt-4">
              <p className="text-xs font-medium text-ink-700">You can see</p>
              <div className="mt-2 flex flex-wrap gap-1.5">
                {visibleScopes.map((s) =>
              <ScopeBadge key={s} scope={s} size="md" />
              )}
              </div>
              {hiddenLabels.length > 0 &&
            <>
                  <p className="mt-4 text-xs font-medium text-ink-700">Hidden from you</p>
                  <ul className="mt-2 space-y-1">
                    {hiddenLabels.map((l) =>
                <li key={l} className="flex items-center gap-2 text-xs text-ink-500">
                        <LockIcon className="h-3 w-3" aria-hidden /> {l} planning
                      </li>
                )}
                  </ul>
                </>
            }
            </div>
          </motion.div>
        }
      </AnimatePresence>
    </div>);

}