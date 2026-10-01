import { useMemo } from 'react';
import { GuestSide } from '../types/wedding';
import { useRole } from '../contexts/RoleContext';
import { useWeddingData } from '../contexts/WeddingDataContext';
import { guestBaseline } from '../data/guests';

export function useGuestStats() {
  const { isSupport, side } = useRole();
  const { guests } = useWeddingData();

  return useMemo(() => {
    const sides: GuestSide[] = isSupport ? [side, 'both'] : ['bride', 'groom', 'both'];
    const list = guests.filter((g) => sides.includes(g.side));
    const count = (status: 'confirmed' | 'pending' | 'declined') =>
    sides.reduce((s, sd) => s + guestBaseline[sd][status], 0) + list.filter((g) => g.rsvp === status).length;
    const confirmed = count('confirmed');
    const pending = count('pending');
    const declined = count('declined');
    return { sides, list, confirmed, pending, declined, total: confirmed + pending + declined };
  }, [guests, isSupport, side]);
}