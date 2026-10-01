import { Role, Scope, Side } from '../types/wedding';
import { people } from '../data/wedding';

export const sideOf = (role: Role): Side => role === 'bride' || role === 'bride-support' ? 'bride' : 'groom';
export const isSupportRole = (role: Role) => role === 'bride-support' || role === 'groom-support';
export const privateScopeOf = (side: Side): Scope => side === 'bride' ? 'private-bride' : 'private-groom';
export const sideScopeOf = (side: Side): Scope => side === 'bride' ? 'bride-side' : 'groom-side';

export const scopeAudience: Record<Scope, Role[]> = {
  'private-bride': ['bride'],
  'private-groom': ['groom'],
  'bride-side': ['bride', 'bride-support'],
  'groom-side': ['groom', 'groom-support'],
  shared: ['bride', 'groom']
};

export const scopeMeta: Record<Scope, {label: string;tone: 'private' | 'bride' | 'groom' | 'shared';}> = {
  'private-bride': { label: 'Private', tone: 'private' },
  'private-groom': { label: 'Private', tone: 'private' },
  'bride-side': { label: 'Bride-side', tone: 'bride' },
  'groom-side': { label: 'Groom-side', tone: 'groom' },
  shared: { label: 'Shared', tone: 'shared' }
};

export function visibleScopesFor(role: Role): Scope[] {
  const side = sideOf(role);
  if (isSupportRole(role)) return [sideScopeOf(side)];
  return [privateScopeOf(side), sideScopeOf(side), 'shared'];
}

export function audienceLabel(scope: Scope, viewer: Role): string {
  const list = scopeAudience[scope];
  if (list.length === 1) return list[0] === viewer ? 'Only you' : `Only ${people[list[0]].firstName}`;
  const names = list.map((r) => r === viewer ? 'You' : people[r].firstName);
  names.sort((a) => a === 'You' ? -1 : 1);
  return names.join(' & ');
}

export const pickAssignee = (scope: Scope, role: Role): Role =>
scopeAudience[scope].includes(role) ? role : scopeAudience[scope][0];

export const supportRestrictedPaths = ['/shared', '/seating', '/vendors', '/invitations'];

export const canAccessPath = (role: Role, path: string) =>
!(isSupportRole(role) && supportRestrictedPaths.includes(path));