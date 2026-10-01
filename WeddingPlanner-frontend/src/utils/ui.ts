import {
  ArmchairIcon,
  CalendarRangeIcon,
  CameraIcon,
  CarIcon,
  Flower2Icon,
  HouseIcon,
  LandmarkIcon,
  MailIcon,
  MusicIcon,
  PackageIcon,
  ShirtIcon,
  UsersIcon,
  UtensilsIcon,
  WalletIcon,
  WineIcon } from
'lucide-react';
import type { LucideIcon } from 'lucide-react';

export const EASE: [number, number, number, number] = [0.23, 1, 0.32, 1];

export const inputClass =
'w-full h-10 rounded-md border border-line-strong bg-white px-3 text-sm text-ink placeholder:text-ink-400 transition-[border-color,box-shadow] duration-150 focus:outline-none focus:border-wine-500 focus:ring-2 focus:ring-wine-500/20';

export const categoryIcons: Record<string, LucideIcon> = {
  Venue: LandmarkIcon,
  Food: UtensilsIcon,
  Drinks: WineIcon,
  Decoration: Flower2Icon,
  Clothing: ShirtIcon,
  Transportation: CarIcon,
  Photography: CameraIcon,
  Entertainment: MusicIcon,
  Invitations: MailIcon,
  'Home Preparation': HouseIcon,
  Other: PackageIcon,
  Guests: UsersIcon,
  Seating: ArmchairIcon,
  Timeline: CalendarRangeIcon,
  Budget: WalletIcon
};

export const iconFor = (key: string): LucideIcon => categoryIcons[key] ?? PackageIcon;