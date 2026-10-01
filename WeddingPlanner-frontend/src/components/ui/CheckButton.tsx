import React from 'react';
import { CheckIcon } from 'lucide-react';

interface CheckButtonProps {
  checked: boolean;
  onChange: () => void;
  label: string;
}

export function CheckButton({ checked, onChange, label }: CheckButtonProps) {
  return (
    <button
      type="button"
      role="checkbox"
      aria-checked={checked}
      aria-label={label}
      onClick={(e) => {
        e.stopPropagation();
        onChange();
      }}
      className={`flex h-5 w-5 shrink-0 items-center justify-center rounded border transition-[background-color,border-color,transform] duration-150 ease-out active:scale-90 ${
      checked ? 'border-wine-700 bg-wine-700 text-white' : 'border-line-strong bg-white hover:border-wine-500'}`
      }>
      
      {checked && <CheckIcon className="h-3.5 w-3.5" strokeWidth={3} aria-hidden />}
    </button>);

}