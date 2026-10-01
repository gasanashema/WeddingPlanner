import React, { useEffect, useState } from 'react';
import { toast } from 'sonner';
import { MailIcon, MessageCircleIcon, SmartphoneIcon } from 'lucide-react';
import { Modal } from '../ui/Modal';
import { Button } from '../ui/Button';
import { Field } from '../ui/Field';
import { Select } from '../ui/Select';

type Audience = 'new' | 'pending' | 'bride' | 'groom';
type Channel = 'whatsapp' | 'sms' | 'email';

const audienceCounts: Record<Audience, {label: string;count: number;}> = {
  new: { label: 'Guests not yet invited', count: 24 },
  pending: { label: 'Reminder to pending RSVPs', count: 81 },
  bride: { label: "Everyone on the bride's side", count: 138 },
  groom: { label: "Everyone on the groom's side", count: 144 }
};

const channels: {value: Channel;label: string;icon: typeof MailIcon;}[] = [
{ value: 'whatsapp', label: 'WhatsApp', icon: MessageCircleIcon },
{ value: 'sms', label: 'SMS', icon: SmartphoneIcon },
{ value: 'email', label: 'Email', icon: MailIcon }];


export function SendInvitationModal({ open, onClose }: {open: boolean;onClose: () => void;}) {
  const [audience, setAudience] = useState<Audience>('new');
  const [selected, setSelected] = useState<Channel[]>(['whatsapp', 'sms']);

  useEffect(() => {
    if (open) {
      setAudience('new');
      setSelected(['whatsapp', 'sms']);
    }
  }, [open]);

  const toggle = (c: Channel) => setSelected((s) => s.includes(c) ? s.filter((x) => x !== c) : [...s, c]);
  const count = audienceCounts[audience].count;

  const send = () => {
    const names = channels.filter((c) => selected.includes(c.value)).map((c) => c.label).join(' & ');
    toast.success(`Invitations on their way to ${count} guests`, { description: `Sent via ${names}` });
    onClose();
  };

  return (
    <Modal
      open={open}
      onClose={onClose}
      title="Send invitations"
      description="Each guest gets a personal link to view the invitation and RSVP."
      footer={
      <>
          <Button variant="secondary" onClick={onClose}>
            Cancel
          </Button>
          <Button onClick={send} disabled={!selected.length}>
            Send to {count} guests
          </Button>
        </>
      }>
      
      <div className="space-y-5">
        <Field label="Who should receive it" htmlFor="inv-audience">
          <Select<Audience>
            id="inv-audience"
            value={audience}
            onChange={setAudience}
            options={(Object.keys(audienceCounts) as Audience[]).map((a) => ({ value: a, label: `${audienceCounts[a].label} (${audienceCounts[a].count})` }))} />
          
        </Field>
        <div>
          <p className="mb-1.5 text-xs font-medium text-ink-700">Send via</p>
          <div className="grid grid-cols-3 gap-2">
            {channels.map((c) => {
              const on = selected.includes(c.value);
              return (
                <button
                  key={c.value}
                  type="button"
                  role="checkbox"
                  aria-checked={on}
                  onClick={() => toggle(c.value)}
                  className={`flex flex-col items-center gap-1.5 rounded-md border py-3 text-sm font-medium transition-colors duration-150 ${
                  on ? 'border-wine-600 bg-wine-50 text-wine-800' : 'border-line bg-white text-ink-600 hover:bg-ivory'}`
                  }>
                  
                  <c.icon className="h-5 w-5" aria-hidden />
                  {c.label}
                </button>);

            })}
          </div>
          {!selected.length && <p className="mt-1.5 text-xs text-danger-700">Pick at least one channel.</p>}
        </div>
      </div>
    </Modal>);

}