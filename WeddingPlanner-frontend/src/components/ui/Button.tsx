import React from "react";
import { BoxIcon } from "lucide-react";
type Variant = 'primary' | 'secondary' | 'ghost' | 'danger' | 'gold';
type Size = 'sm' | 'md';
interface ButtonProps extends React.ButtonHTMLAttributes<HTMLButtonElement> {
  variant?: Variant;
  size?: Size;
  icon?: BoxIcon;
}
const variants: Record<Variant, string> = {
  primary: 'bg-wine-700 text-white hover:bg-wine-800 shadow-card',
  secondary: 'bg-white text-ink border border-line-strong hover:bg-ivory-100',
  ghost: 'text-ink-600 hover:bg-ivory-100 hover:text-ink',
  danger: 'bg-danger-600 text-white hover:bg-danger-700',
  gold: 'bg-gold-100 text-gold-700 border border-gold-200 hover:bg-gold-200'
};
const sizes: Record<Size, string> = {
  sm: 'h-8 px-3 text-[13px] gap-1.5',
  md: 'h-10 px-4 text-sm gap-2'
};
export function Button({
  variant = 'primary',
  size = 'md',
  icon: Icon,
  className = '',
  children,
  type = 'button',
  ...rest
}: ButtonProps) {
  return <button type={type} className={`inline-flex items-center justify-center whitespace-nowrap rounded-md font-medium transition-[background-color,border-color,color,transform] duration-150 ease-out active:scale-[0.98] focus-visible:outline-none focus-visible:ring-2 focus-visible:ring-wine-500/40 focus-visible:ring-offset-1 disabled:pointer-events-none disabled:opacity-50 ${variants[variant]} ${sizes[size]} ${className}`} {...rest}>
      {Icon && <Icon className={size === 'sm' ? 'h-3.5 w-3.5' : 'h-4 w-4'} aria-hidden />}
      {children}
    </button>;
}