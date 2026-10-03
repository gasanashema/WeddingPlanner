import React from 'react';
import { Lock, Users, ShieldAlert } from 'lucide-react';

interface ScopeBadgeProps {
  scope: 'BRIDE_PRIVATE' | 'GROOM_PRIVATE' | 'SHARED' | string;
}

export function ScopeBadge({ scope }: ScopeBadgeProps) {
  switch (scope) {
    case 'BRIDE_PRIVATE':
      return (
        <span className="inline-flex items-center gap-1 px-2.5 py-1 rounded-full text-xs font-semibold bg-rose-500/10 text-rose-400 border border-rose-500/20">
          <Lock className="w-3 h-3 text-rose-400" />
          <span>Bride Private</span>
        </span>
      );
    case 'GROOM_PRIVATE':
      return (
        <span className="inline-flex items-center gap-1 px-2.5 py-1 rounded-full text-xs font-semibold bg-blue-500/10 text-blue-400 border border-blue-500/20">
          <Lock className="w-3 h-3 text-blue-400" />
          <span>Groom Private</span>
        </span>
      );
    case 'SHARED':
    default:
      return (
        <span className="inline-flex items-center gap-1 px-2.5 py-1 rounded-full text-xs font-semibold bg-amber-500/10 text-amber-400 border border-amber-500/20">
          <Users className="w-3 h-3 text-amber-400" />
          <span>Shared</span>
        </span>
      );
  }
}