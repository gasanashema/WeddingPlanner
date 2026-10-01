import React from 'react';

interface CardProps {
  title?: string;
  description?: string;
  action?: React.ReactNode;
  meta?: React.ReactNode;
  children: React.ReactNode;
  className?: string;
  bodyClassName?: string;
}

export function Card({ title, description, action, meta, children, className = '', bodyClassName = 'p-5' }: CardProps) {
  return (
    <section className={`rounded-lg border border-line bg-white shadow-card ${className}`}>
      {(title || action) &&
      <header className="flex items-start justify-between gap-3 border-b border-line px-5 py-4">
          <div className="min-w-0">
            <div className="flex flex-wrap items-center gap-2">
              {title && <h2 className="text-[15px] font-semibold text-ink">{title}</h2>}
              {meta}
            </div>
            {description && <p className="mt-0.5 text-xs text-ink-500">{description}</p>}
          </div>
          {action && <div className="shrink-0">{action}</div>}
        </header>
      }
      <div className={bodyClassName}>{children}</div>
    </section>);

}