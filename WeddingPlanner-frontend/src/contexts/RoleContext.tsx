import React, { createContext, useCallback, useContext, useMemo } from 'react';
import { Person, Role, Scope, Side } from '../types/wedding';
import { people } from '../data/wedding';
import { isSupportRole, sideOf, visibleScopesFor } from '../utils/permissions';

interface RoleContextValue {
  role: Role;
  person: Person;
  side: Side;
  isSupport: boolean;
  visibleScopes: Scope[];
  canSee: (scope: Scope) => boolean;
}

const RoleContext = createContext<RoleContextValue | null>(null);

export function RoleProvider({ role, children }: {role: Role;children: React.ReactNode;}) {
  const visibleScopes = useMemo(() => visibleScopesFor(role), [role]);
  const canSee = useCallback((scope: Scope) => visibleScopes.includes(scope), [visibleScopes]);
  const value = useMemo<RoleContextValue>(
    () => ({
      role,
      person: people[role],
      side: sideOf(role),
      isSupport: isSupportRole(role),
      visibleScopes,
      canSee
    }),
    [role, visibleScopes, canSee]
  );
  return <RoleContext.Provider value={value}>{children}</RoleContext.Provider>;
}

export function useRole() {
  const ctx = useContext(RoleContext);
  if (!ctx) throw new Error('useRole must be used within RoleProvider');
  return ctx;
}