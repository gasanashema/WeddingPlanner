import React from 'react';
import { CheckIcon, LockIcon } from 'lucide-react';
import { useRole } from '../../contexts/RoleContext';
import { people } from '../../data/wedding';
import { Role } from '../../types/wedding';
import { Avatar } from './Avatar';

interface LockedStateProps {
  area: string;
}

export function LockedState({ area }: LockedStateProps) {
  const { person, side } = useRole();
  const principal: Role = side;
  const allowed: Role[] = ['bride', 'groom'];
  return (
    <div className="mx-auto max-w-xl py-10 lg:py-16">
      <div className="rounded-lg border border-line bg-white p-8 text-center shadow-card">
        <span className="mx-auto flex h-12 w-12 items-center justify-center rounded-full bg-ink-50 text-ink-700">
          <LockIcon className="h-5 w-5" aria-hidden />
        </span>
        <h1 className="mt-4 font-serif text-2xl font-semibold text-ink">{area} is shared between the couple</h1>
        <p className="mx-auto mt-2 max-w-md text-sm text-ink-500">
          Only the couple can open this area. As {person.relation.toLowerCase()}, you can help with everything on the{' '}
          {side === 'bride' ? 'bride' : 'groom'} side.
        </p>
        <ul className="mx-auto mt-6 max-w-xs space-y-2 text-left">
          {allowed.map((r) =>
          <li key={r} className="flex items-center gap-3 rounded-md border border-line px-3 py-2">
              <Avatar role={r} size="xs" />
              <span className="flex-1 text-sm text-ink">{people[r].name}</span>
              <CheckIcon className="h-4 w-4 text-success-600" aria-label="Has access" />
            </li>
          )}
          <li className="flex items-center gap-3 rounded-md border border-dashed border-line-strong px-3 py-2">
            <Avatar role={person.role} size="xs" />
            <span className="flex-1 text-sm text-ink-500">{person.name} (you)</span>
            <LockIcon className="h-4 w-4 text-ink-400" aria-label="No access" />
          </li>
        </ul>
      </div>
    </div>);

}