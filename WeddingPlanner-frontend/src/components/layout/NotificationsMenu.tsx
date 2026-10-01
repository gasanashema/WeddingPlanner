import React, { useCallback, useState } from 'react';
import { AnimatePresence, motion } from 'framer-motion';
import { BellIcon } from 'lucide-react';
import { useRole } from '../../contexts/RoleContext';
import { notifications as allNotifications } from '../../data/shared';
import { useClickOutside } from '../../hooks/useClickOutside';
import { EASE } from '../../utils/ui';
import { ScopeBadge } from '../ui/ScopeBadge';

export function NotificationsMenu() {
  const { canSee } = useRole();
  const [open, setOpen] = useState(false);
  const [readIds, setReadIds] = useState<string[]>([]);
  const close = useCallback(() => setOpen(false), []);
  const ref = useClickOutside<HTMLDivElement>(open, close);

  const items = allNotifications.filter((n) => canSee(n.scope));
  const isUnread = (id: string, unread: boolean) => unread && !readIds.includes(id);
  const unreadCount = items.filter((n) => isUnread(n.id, n.unread)).length;

  return (
    <div ref={ref} className="relative">
      <button
        type="button"
        onClick={() => setOpen((o) => !o)}
        aria-label={`Notifications${unreadCount ? `, ${unreadCount} unread` : ''}`}
        aria-expanded={open}
        className="relative flex h-9 w-9 items-center justify-center rounded-md text-ink-600 transition-colors duration-150 hover:bg-ivory-100 hover:text-ink">
        
        <BellIcon className="h-[18px] w-[18px]" />
        {unreadCount > 0 && <span className="absolute right-2 top-2 h-2 w-2 rounded-full bg-wine-600 ring-2 ring-ivory" aria-hidden />}
      </button>
      <AnimatePresence>
        {open &&
        <motion.div
          initial={{ opacity: 0, y: -4, scale: 0.98 }}
          animate={{ opacity: 1, y: 0, scale: 1 }}
          exit={{ opacity: 0, y: -4, scale: 0.98 }}
          transition={{ duration: 0.16, ease: EASE }}
          style={{ transformOrigin: 'top right' }}
          className="absolute right-0 top-11 z-40 w-[min(92vw,380px)] rounded-lg border border-line bg-white shadow-pop">
          
            <div className="flex items-center justify-between border-b border-line px-4 py-3">
              <p className="text-sm font-semibold text-ink">Notifications</p>
              {unreadCount > 0 &&
            <button type="button" onClick={() => setReadIds(items.map((n) => n.id))} className="text-xs font-medium text-wine-700 hover:text-wine-800">
                  Mark all as read
                </button>
            }
            </div>
            <ul className="max-h-96 divide-y divide-line overflow-y-auto">
              {items.map((n) =>
            <li key={n.id} className="flex gap-3 px-4 py-3">
                  <span className={`mt-1.5 h-2 w-2 shrink-0 rounded-full ${isUnread(n.id, n.unread) ? 'bg-wine-600' : 'bg-transparent'}`} aria-hidden />
                  <div className="min-w-0 flex-1">
                    <p className="text-sm font-medium text-ink">{n.title}</p>
                    <p className="mt-0.5 text-xs text-ink-500">{n.body}</p>
                    <div className="mt-2 flex items-center gap-2">
                      <ScopeBadge scope={n.scope} />
                      <span className="text-[11px] text-ink-500">{n.time}</span>
                    </div>
                  </div>
                </li>
            )}
            </ul>
          </motion.div>
        }
      </AnimatePresence>
    </div>);

}