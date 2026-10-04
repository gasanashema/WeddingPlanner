import React, { useEffect, useState } from 'react';
import { Outlet, useLocation } from 'react-router-dom';
import { AnimatePresence, motion } from 'framer-motion';
import { useRole } from '../../contexts/RoleContext';
import { useAuth } from '../../contexts/AuthContext';
import { navItems } from '../../data/navigation';
import { canAccessPath } from '../../utils/permissions';
import { EASE } from '../../utils/ui';
import { LockedState } from '../ui/LockedState';
import { Sidebar } from './Sidebar';
import { Header } from './Header';
import { MobileNav } from './MobileNav';
import { ChangePasswordModal } from '../auth/ChangePasswordModal';

export function AppLayout() {
  const [drawerOpen, setDrawerOpen] = useState(false);
  const { pathname } = useLocation();
  const { role } = useRole();
  const { user } = useAuth();
  const allowed = canAccessPath(role, pathname);
  const areaName = navItems.find((n) => n.path === pathname)?.label ?? 'This area';

  useEffect(() => {
    setDrawerOpen(false);
    window.scrollTo({ top: 0 });
  }, [pathname]);

  return (
    <div className="flex min-h-screen w-full bg-ivory">
      <ChangePasswordModal
        isOpen={Boolean(user?.mustChangePassword)}
        onClose={() => {}}
      />
      <aside className="sticky top-0 hidden h-screen shrink-0 lg:flex">
        <Sidebar />
      </aside>

      <AnimatePresence>
        {drawerOpen &&
        <div className="fixed inset-0 z-50 lg:hidden">
            <motion.div
            className="absolute inset-0 bg-ink/40"
            initial={{ opacity: 0 }}
            animate={{ opacity: 1 }}
            exit={{ opacity: 0 }}
            transition={{ duration: 0.2 }}
            onClick={() => setDrawerOpen(false)} />
          
            <motion.div
            className="absolute inset-y-0 left-0"
            initial={{ x: '-100%' }}
            animate={{ x: 0 }}
            exit={{ x: '-100%' }}
            transition={{ duration: 0.25, ease: EASE }}>
            
              <Sidebar onNavigate={() => setDrawerOpen(false)} />
            </motion.div>
          </div>
        }
      </AnimatePresence>

      <div className="flex min-w-0 flex-1 flex-col">
        <Header onMenu={() => setDrawerOpen(true)} />
        <main className="flex-1 px-4 pb-28 pt-6 sm:px-6 lg:px-8 lg:pb-12 lg:pt-8">
          <div className="mx-auto max-w-[1320px]">{allowed ? <Outlet /> : <LockedState area={areaName} />}</div>
        </main>
      </div>

      <MobileNav onMore={() => setDrawerOpen(true)} />
    </div>);

}