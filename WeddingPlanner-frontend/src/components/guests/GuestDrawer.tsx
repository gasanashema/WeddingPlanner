import React, { useEffect } from 'react';
import { AnimatePresence, motion } from 'framer-motion';
import { MailOpenIcon, PhoneIcon, UsersIcon, XIcon } from 'lucide-react';
import { toast } from 'sonner';
import { Guest, RsvpStatus } from '../../types/wedding';
import { useWeddingData } from '../../contexts/WeddingDataContext';
import { seatingTables } from '../../data/guests';
import { rsvpMeta } from '../../utils/status';
import { EASE } from '../../utils/ui';
import { Badge } from '../ui/Badge';
import { Segmented } from '../ui/Segmented';
import { Select } from '../ui/Select';

const sideLabel = { bride: "Bride's side", groom: "Groom's side", both: 'Both families' };

export function GuestDrawer({ guestId, onClose }: {guestId: string | null;onClose: () => void;}) {
  const { guests, updateGuest } = useWeddingData();
  const guest: Guest | undefined = guests.find((g) => g.id === guestId);

  useEffect(() => {
    if (!guestId) return;
    const onKey = (e: KeyboardEvent) => e.key === 'Escape' && onClose();
    document.addEventListener('keydown', onKey);
    return () => document.removeEventListener('keydown', onKey);
  }, [guestId, onClose]);

  return (
    <AnimatePresence>
      {guest &&
      <div className="fixed inset-0 z-50">
          <motion.div className="absolute inset-0 bg-ink/30" initial={{ opacity: 0 }} animate={{ opacity: 1 }} exit={{ opacity: 0 }} transition={{ duration: 0.2 }} onClick={onClose} />
          <motion.aside
          role="dialog"
          aria-modal="true"
          aria-label={`Guest details: ${guest.name}`}
          className="absolute inset-y-0 right-0 flex w-full max-w-md flex-col border-l border-line bg-white shadow-modal"
          initial={{ x: '100%' }}
          animate={{ x: 0 }}
          exit={{ x: '100%' }}
          transition={{ duration: 0.25, ease: EASE }}>
          
            <header className="flex items-start justify-between border-b border-line px-6 py-5">
              <div>
                <p className="text-xs text-ink-500">Guest details</p>
                <h2 className="mt-1 font-serif text-2xl font-semibold text-ink">{guest.name}</h2>
                <div className="mt-2 flex gap-2">
                  <Badge tone={guest.side === 'bride' ? 'bride' : guest.side === 'groom' ? 'groom' : 'wine'}>{sideLabel[guest.side]}</Badge>
                  <Badge>{guest.group}</Badge>
                </div>
              </div>
              <button type="button" onClick={onClose} aria-label="Close" className="rounded-md p-1.5 text-ink-500 hover:bg-ivory-100 hover:text-ink">
                <XIcon className="h-5 w-5" />
              </button>
            </header>
            <div className="flex-1 space-y-6 overflow-y-auto px-6 py-6">
              <dl className="space-y-3 text-sm">
                <div className="flex items-center gap-3">
                  <PhoneIcon className="h-4 w-4 text-ink-400" aria-hidden />
                  <dt className="sr-only">Phone</dt>
                  <dd className="tnum text-ink">{guest.phone}</dd>
                </div>
                <div className="flex items-center gap-3">
                  <UsersIcon className="h-4 w-4 text-ink-400" aria-hidden />
                  <dt className="sr-only">Party size</dt>
                  <dd className="text-ink">{guest.plusOnes ? `Coming with ${guest.plusOnes} guest${guest.plusOnes > 1 ? 's' : ''}` : 'Attending alone'}</dd>
                </div>
                <div className="flex items-center gap-3">
                  <MailOpenIcon className="h-4 w-4 text-ink-400" aria-hidden />
                  <dt className="sr-only">Invitation</dt>
                  <dd className="text-ink">{guest.invitationOpened ? 'Opened the digital invitation' : 'Hasn’t opened the invitation yet'}</dd>
                </div>
              </dl>
              <div>
                <p className="mb-2 text-xs font-medium text-ink-700">RSVP status</p>
                <Segmented<RsvpStatus>
                ariaLabel="RSVP status"
                value={guest.rsvp}
                onChange={(v) => {
                  updateGuest(guest.id, { rsvp: v, table: v === 'declined' ? null : guest.table });
                  toast.success(`RSVP set to ${rsvpMeta[v].label.toLowerCase()}`);
                }}
                options={(Object.keys(rsvpMeta) as RsvpStatus[]).map((r) => ({ value: r, label: rsvpMeta[r].label }))} />
              
              </div>
              <div>
                <label htmlFor="guest-table" className="mb-2 block text-xs font-medium text-ink-700">
                  Table assignment
                </label>
                <Select<string>
                id="guest-table"
                value={guest.table ? String(guest.table) : ''}
                disabled={guest.rsvp === 'declined'}
                onChange={(v) => updateGuest(guest.id, { table: v ? Number(v) : null })}
                options={[{ value: '', label: 'Not assigned' }, ...seatingTables.map((t) => ({ value: String(t.id), label: `${t.name} — ${t.note}` }))]} />
              
              </div>
            </div>
          </motion.aside>
        </div>
      }
    </AnimatePresence>);

}