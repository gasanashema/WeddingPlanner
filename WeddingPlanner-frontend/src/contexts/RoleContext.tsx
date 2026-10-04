import React, { createContext, useCallback, useContext, useMemo } from 'react';
import { Person, Role, Scope, Side } from '../types/wedding';
import { people } from '../data/wedding';
import { isSupportRole, sideOf, visibleScopesFor } from '../utils/permissions';
import { useAuth } from './AuthContext';

interface RoleContextValue {
  role: Role;
  person: Person;
  side: Side;
  isSupport: boolean;
  visibleScopes: Scope[];
  canSee: (scope: Scope) => boolean;
}

const RoleContext = createContext<RoleContextValue | null>(null);

export function RoleProvider({ role: propRole, children }: { role?: Role; children: React.ReactNode }) {
  const { user } = useAuth();

  const mappedRole: Role = useMemo(() => {
    if (!user) return propRole || 'bride';
    switch (user.role) {
      case 'ROLE_BRIDE': return 'bride';
      case 'ROLE_GROOM': return 'groom';
      case 'ROLE_BRIDE_FAMILY_SUPPORT': return 'bride-support';
      case 'ROLE_GROOM_FAMILY_SUPPORT': return 'groom-support';
      default: return 'bride';
    }
  }, [user, propRole]);

  const person: Person = useMemo(() => {
    if (!user) return people[mappedRole];
    const first = user.firstName || 'User';
    const last = user.lastName || '';
    const initials = (first[0] || '').toUpperCase() + (last[0] || '').toUpperCase();
    return {
      role: mappedRole,
      name: `${first} ${last}`.trim(),
      firstName: first,
      initials: initials || 'U',
      roleLabel: mappedRole === 'bride' ? 'Bride' : mappedRole === 'groom' ? 'Groom' : mappedRole === 'bride-support' ? 'Bride Support' : 'Groom Support',
      relation: mappedRole === 'bride' ? 'Bride' : mappedRole === 'groom' ? 'Groom' : 'Family Support',
      side: sideOf(mappedRole),
      email: user.email,
      phone: user.phoneNumber || ''
    };
  }, [user, mappedRole]);

  const visibleScopes = useMemo(() => visibleScopesFor(mappedRole), [mappedRole]);
  const canSee = useCallback((scope: Scope) => visibleScopes.includes(scope), [visibleScopes]);

  const value = useMemo<RoleContextValue>(
    () => ({
      role: mappedRole,
      person,
      side: sideOf(mappedRole),
      isSupport: isSupportRole(mappedRole),
      visibleScopes,
      canSee
    }),
    [mappedRole, person, visibleScopes, canSee]
  );

  return <RoleContext.Provider value={value}>{children}</RoleContext.Provider>;
}

export function useRole() {
  const ctx = useContext(RoleContext);
  if (!ctx) throw new Error('useRole must be used within RoleProvider');
  return ctx;
}