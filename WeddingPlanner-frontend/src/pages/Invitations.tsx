import React, { useState } from 'react';
import { toast } from 'sonner';
import { CheckIcon, CopyIcon, MailOpenIcon, SendIcon, XIcon, CircleCheckIcon } from 'lucide-react';
import { useGuestStats } from '../hooks/useGuestStats';
import { invitationActivity } from '../data/shared';
import { invitationStats } from '../data/guests';
import { wedding } from '../data/wedding';
import { PageHeader } from '../components/ui/PageHeader';
import { Button } from '../components/ui/Button';
import { Card } from '../components/ui/Card';
import { InvitationPreview } from '../components/invitations/InvitationPreview';
import { SendInvitationModal } from '../components/invitations/SendInvitationModal';

const activityMeta = {
  opened: { label: 'opened the invitation', icon: MailOpenIcon, cls: 'bg-gold-50 text-gold-700' },
  confirmed: { label: 'confirmed attendance', icon: CircleCheckIcon, cls: 'bg-success-50 text-success-700' },
  declined: { label: 'declined', icon: XIcon, cls: 'bg-danger-50 text-danger-700' },
  sent: { label: 'was sent an invitation', icon: SendIcon, cls: 'bg-ink-50 text-ink-600' }
};

export function Invitations() {
  const stats = useGuestStats();
  const [sendOpen, setSendOpen] = useState(false);
  const [copied, setCopied] = useState(false);

  const copy = async () => {
    try {
      await navigator.clipboard.writeText(wedding.inviteLink);
      setCopied(true);
      toast.success('Invitation link copied');
      window.setTimeout(() => setCopied(false), 2000);
    } catch {
      toast.error('Couldn’t copy — select the link and copy it manually');
    }
  };

  const funnel = [
  { label: 'Sent', value: invitationStats.sent, base: stats.total },
  { label: 'Opened', value: invitationStats.opened, base: invitationStats.sent },
  { label: 'Confirmed', value: stats.confirmed, base: invitationStats.sent },
  { label: 'Pending', value: stats.pending, base: invitationStats.sent },
  { label: 'Declined', value: stats.declined, base: invitationStats.sent }];


  return (
    <div>
      <PageHeader
        title="Invitations"
        description="One digital invitation for every guest, with RSVP built in."
        scope="shared"
        actions={
        <>
            <Button variant="secondary" icon={copied ? CheckIcon : CopyIcon} onClick={copy}>
              {copied ? 'Copied' : 'Copy link'}
            </Button>
            <Button icon={SendIcon} onClick={() => setSendOpen(true)}>
              Send invitation
            </Button>
          </>
        } />
      

      <div className="grid gap-6 lg:grid-cols-[minmax(0,1fr)_minmax(0,1.1fr)]">
        <section aria-label="Invitation preview" className="rounded-lg border border-line bg-ivory-100 p-6 sm:p-10">
          <InvitationPreview />
          <div className="mx-auto mt-6 flex max-w-sm items-center gap-2 rounded-md border border-line bg-white p-1.5 pl-3">
            <span className="min-w-0 flex-1 truncate text-sm text-ink-600">{wedding.inviteLink}</span>
            <Button size="sm" variant="secondary" icon={copied ? CheckIcon : CopyIcon} onClick={copy}>
              {copied ? 'Copied' : 'Copy'}
            </Button>
          </div>
        </section>

        <div className="space-y-6">
          <Card title="Responses" description={`${stats.total} guests on the list`}>
            <dl className="space-y-4">
              {funnel.map((f) => {
                const pct = Math.round(f.value / f.base * 100);
                return (
                  <div key={f.label}>
                    <div className="flex items-baseline justify-between">
                      <dt className="text-sm text-ink-700">{f.label}</dt>
                      <dd className="tnum text-sm">
                        <span className="text-lg font-semibold text-ink">{f.value}</span>
                        <span className="ml-2 text-xs text-ink-500">{pct}%</span>
                      </dd>
                    </div>
                    <div className="mt-1.5 h-1.5 overflow-hidden rounded-full bg-ivory-200">
                      <div
                        className={`h-full rounded-full ${f.label === 'Confirmed' ? 'bg-success-600' : f.label === 'Declined' ? 'bg-danger-600' : f.label === 'Pending' ? 'bg-gold-400' : 'bg-wine-700'}`}
                        style={{ width: `${pct}%` }} />
                      
                    </div>
                  </div>);

              })}
            </dl>
          </Card>

          <Card title="Recent activity" bodyClassName="px-5 py-2">
            <ul className="divide-y divide-line">
              {invitationActivity.map((a) => {
                const meta = activityMeta[a.action];
                return (
                  <li key={a.id} className="flex items-center gap-3 py-3">
                    <span className={`flex h-8 w-8 shrink-0 items-center justify-center rounded-full ${meta.cls}`}>
                      <meta.icon className="h-4 w-4" aria-hidden />
                    </span>
                    <p className="min-w-0 flex-1 text-sm text-ink-600">
                      <span className="font-medium text-ink">{a.guest}</span> {meta.label}
                    </p>
                    <span className="shrink-0 text-xs text-ink-500">{a.time}</span>
                  </li>);

              })}
            </ul>
          </Card>
        </div>
      </div>

      <SendInvitationModal open={sendOpen} onClose={() => setSendOpen(false)} />
    </div>);

}