import React from 'react';

interface FieldProps {
  label: string;
  htmlFor: string;
  hint?: string;
  error?: string;
  className?: string;
  children: React.ReactNode;
}

export function Field({ label, htmlFor, hint, error, className = '', children }: FieldProps) {
  return (
    <div className={className}>
      <label htmlFor={htmlFor} className="mb-1.5 block text-xs font-medium text-ink-700">
        {label}
      </label>
      {children}
      {error ?
      <p className="mt-1.5 text-xs text-danger-700">{error}</p> :
      hint ?
      <p className="mt-1.5 text-xs text-ink-500">{hint}</p> :
      null}
    </div>);

}