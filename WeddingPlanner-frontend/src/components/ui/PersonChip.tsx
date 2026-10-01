import React from 'react';
import { Role } from '../../types/wedding';
import { people } from '../../data/wedding';
import { useRole } from '../../contexts/RoleContext';
import { Avatar } from './Avatar';

export function PersonChip({ role, showRole = false }: {role: Role;showRole?: boolean;}) {
  const { role: viewer } = useRole();
  const p = people[role];
  return (
    <span className="inline-flex min-w-0 items-center gap-2">
      <Avatar role={role} size="xs" />
      <span className="min-w-0">
        <span className="block truncate text-sm text-ink">{role === viewer ? `${p.firstName} (you)` : p.firstName}</span>
        {showRole && <span className="block truncate text-[11px] text-ink-500">{p.roleLabel}</span>}
      </span>
    </span>);

}