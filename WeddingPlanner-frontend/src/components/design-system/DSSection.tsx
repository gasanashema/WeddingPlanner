import React from 'react';

interface DSSectionProps {
  id: string;
  title: string;
  description?: string;
  children: React.ReactNode;
}

export function DSSection({ id, title, description, children }: DSSectionProps) {
  return (
    <section id={id} aria-labelledby={`${id}-title`} className="scroll-mt-24 border-t border-line pt-8">
      <div className="mb-5">
        <h2 id={`${id}-title`} className="font-serif text-xl font-semibold text-ink">
          {title}
        </h2>
        {description && <p className="mt-1 max-w-2xl text-sm text-ink-500">{description}</p>}
      </div>
      {children}
    </section>);

}