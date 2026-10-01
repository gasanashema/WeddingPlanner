import React from 'react';
import { Link2Icon, LockIcon, UsersIcon } from 'lucide-react';
import { Scope } from '../../types/wedding';
import { useRole } from '../../contexts/RoleContext';
import { audienceLabel, scopeMeta } from '../../utils/permissions';

interface ScopeSelectorProps {
  value: Scope;
  onChange: (s: Scope) => void;
  scopes: Scope[];
}

const icons = { private: LockIcon, shared: Link2Icon, bride: UsersIcon, groom: UsersIcon };
const activeStyles = {
  private: 'border-ink-700 bg-ink-50',
  shared: 'border-wine-600 bg-wine-50',
  bride: 'border-bride-600 bg-bride-50',
  groom: 'border-groom-600 bg-groom-50'
};

export function ScopeSelector({ value, onChange, scopes }: ScopeSelectorProps) {
  const { role } = useRole();
  return (
    <div role="radiogroup" aria-label="Visibility" className="grid gap-2 sm:grid-cols-3">
      {scopes.map((s) => {
        const meta = scopeMeta[s];
        const Icon = icons[meta.tone];
        const active = s === value;
        return (
          <button
            key={s}
            type="button"
            role="radio"
            aria-checked={active}
            onClick={() => onChange(s)}
            className={`rounded-md border p-3 text-left transition-colors duration-150 ${
            active ? activeStyles[meta.tone] : 'border-line bg-white hover:bg-ivory'}`
            }>
            
            <span className="flex items-center gap-1.5 text-sm font-medium text-ink">
              <Icon className="h-3.5 w-3.5" aria-hidden />
              {meta.label}
            </span>
            <span className="mt-0.5 block text-xs text-ink-500">{audienceLabel(s, role)}</span>
          </button>);

      })}
    </div>);

}