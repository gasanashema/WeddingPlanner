import React from 'react';
import { Role } from '../../types/wedding';
import { people } from '../../data/wedding';
import { sideOf } from '../../utils/permissions';

const sizes = {
  xs: 'h-6 w-6 text-[10px]',
  sm: 'h-8 w-8 text-xs',
  md: 'h-10 w-10 text-sm'
};

interface AvatarProps {
  role: Role;
  size?: keyof typeof sizes;
  className?: string;
}

export function Avatar({ role, size = 'sm', className = '' }: AvatarProps) {
  const p = people[role];
  const tone = sideOf(role) === 'bride' ? 'bg-bride-100 text-bride-700' : 'bg-groom-100 text-groom-700';
  return (
    <span
      title={`${p.name} · ${p.roleLabel}`}
      className={`inline-flex shrink-0 items-center justify-center rounded-full font-semibold ring-2 ring-white ${tone} ${sizes[size]} ${className}`}>
      
      {p.initials}
    </span>);

}