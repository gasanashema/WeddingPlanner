import React from 'react';
import { EyeIcon } from 'lucide-react';
import { Scope } from '../../types/wedding';
import { useRole } from '../../contexts/RoleContext';
import { audienceLabel, scopeAudience } from '../../utils/permissions';
import { ScopeBadge } from './ScopeBadge';
import { Avatar } from './Avatar';

export function VisibilityNote({ scope }: {scope: Scope;}) {
  const { role } = useRole();
  return (
    <div className="inline-flex flex-wrap items-center gap-2 text-xs text-ink-500">
      <ScopeBadge scope={scope} size="md" />
      <span className="inline-flex items-center gap-1.5">
        <EyeIcon className="h-3.5 w-3.5 text-ink-400" aria-hidden />
        Visible to
      </span>
      <span className="flex -space-x-1.5">
        {scopeAudience[scope].map((r) =>
        <Avatar key={r} role={r} size="xs" />
        )}
      </span>
      <span className="font-medium text-ink-700">{audienceLabel(scope, role)}</span>
    </div>);

}