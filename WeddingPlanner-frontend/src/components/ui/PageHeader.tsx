import React from 'react';
import { Scope } from '../../types/wedding';
import { VisibilityNote } from './VisibilityNote';

interface PageHeaderProps {
  title: string;
  description?: string;
  scope?: Scope;
  actions?: React.ReactNode;
}

export function PageHeader({ title, description, scope, actions }: PageHeaderProps) {
  return (
    <div className="mb-6 flex flex-col gap-4 lg:mb-8 lg:flex-row lg:items-end lg:justify-between">
      <div className="min-w-0">
        <h1 className="font-serif text-[28px] font-semibold leading-tight text-ink sm:text-[32px]">{title}</h1>
        {description && <p className="mt-1.5 max-w-2xl text-sm text-ink-500">{description}</p>}
        {scope &&
        <div className="mt-3">
            <VisibilityNote scope={scope} />
          </div>
        }
      </div>
      {actions && <div className="flex flex-wrap items-center gap-2">{actions}</div>}
    </div>);

}