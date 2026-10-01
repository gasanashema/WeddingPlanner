import React from 'react';
import { Link2Icon, LockIcon, UsersIcon } from 'lucide-react';
import { Scope } from '../../types/wedding';
import { useRole } from '../../contexts/RoleContext';
import { audienceLabel, scopeMeta } from '../../utils/permissions';

const toneStyles = {
  private: 'bg-ink-50 text-ink-700 border-ink-100',
  shared: 'bg-wine-50 text-wine-700 border-wine-100',
  bride: 'bg-bride-50 text-bride-700 border-bride-100',
  groom: 'bg-groom-50 text-groom-700 border-groom-100'
};

const toneIcons = { private: LockIcon, shared: Link2Icon, bride: UsersIcon, groom: UsersIcon };

interface ScopeBadgeProps {
  scope: Scope;
  size?: 'sm' | 'md';
  withAudience?: boolean;
}

export function ScopeBadge({ scope, size = 'sm', withAudience = false }: ScopeBadgeProps) {
  const { role } = useRole();
  const meta = scopeMeta[scope];
  const Icon = toneIcons[meta.tone];
  const audience = audienceLabel(scope, role);
  return (
    <span
      title={`Visible to: ${audience}`}
      className={`inline-flex items-center gap-1 whitespace-nowrap rounded border font-medium ${toneStyles[meta.tone]} ${
      size === 'sm' ? 'px-1.5 py-0.5 text-[11px]' : 'px-2 py-1 text-xs'}`
      }>
      
      <Icon className={size === 'sm' ? 'h-3 w-3' : 'h-3.5 w-3.5'} aria-hidden />
      {meta.label}
      {withAudience && <span className="font-normal opacity-80">· {audience}</span>}
    </span>);

}