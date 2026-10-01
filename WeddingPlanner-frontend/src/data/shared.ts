import { InvitationActivity, NotificationItem, SharedArea } from '../types/wedding';

export const sharedAreas: SharedArea[] = [
{ id: 'a1', name: 'Venue', icon: 'Venue', nextStep: 'Pay deposit balance to secure the date', progress: 70, assignee: 'groom', deadline: '2027-06-10', budget: 4500000, spent: 1350000, status: 'in-progress' },
{ id: 'a2', name: 'Food & catering', icon: 'Food', nextStep: 'Confirm final menu with Umusambi', progress: 40, assignee: 'groom', deadline: '2027-06-18', budget: 5200000, spent: 60000, status: 'not-started' },
{ id: 'a3', name: 'Drinks', icon: 'Drinks', nextStep: 'Agree on soft drinks vs. banana wine split', progress: 55, assignee: 'groom', deadline: '2027-07-15', budget: 1800000, spent: 700000, status: 'in-progress' },
{ id: 'a4', name: 'Decoration', icon: 'Decoration', nextStep: 'Pick one of two décor concepts', progress: 60, assignee: 'bride', deadline: '2027-06-20', budget: 2800000, spent: 1000000, status: 'in-progress' },
{ id: 'a5', name: 'Entertainment', icon: 'Entertainment', nextStep: 'Share first-dance song with DJ', progress: 75, assignee: 'groom', deadline: '2027-08-01', budget: 1200000, spent: 300000, status: 'in-progress' },
{ id: 'a6', name: 'Photography', icon: 'Photography', nextStep: 'Send shot list to Lens of Kigali', progress: 90, assignee: 'bride', deadline: '2027-08-10', budget: 1800000, spent: 850000, status: 'in-progress' },
{ id: 'a7', name: 'Invitations', icon: 'Invitations', nextStep: 'Approve design and send to all guests', progress: 35, assignee: 'groom', deadline: '2027-06-30', budget: 250000, spent: 0, status: 'not-started' },
{ id: 'a8', name: 'Guest management', icon: 'Guests', nextStep: 'Chase 81 pending RSVPs', progress: 67, assignee: 'bride', deadline: '2027-07-30', budget: 0, spent: 0, status: 'in-progress' },
{ id: 'a9', name: 'Seating arrangement', icon: 'Seating', nextStep: 'Seat confirmed guests without a table', progress: 50, assignee: 'bride', deadline: '2027-08-10', budget: 0, spent: 0, status: 'in-progress' },
{ id: 'a10', name: 'Shared timeline', icon: 'Timeline', nextStep: 'Lock the wedding-day run of show', progress: 80, assignee: 'bride', deadline: '2027-08-01', budget: 0, spent: 0, status: 'in-progress' },
{ id: 'a11', name: 'Shared budget', icon: 'Budget', nextStep: 'Reconcile May expenses', progress: 100, assignee: 'groom', deadline: '2027-05-31', budget: 18550000, spent: 5360000, status: 'completed' }];


export const notifications: NotificationItem[] = [
{ id: 'n1', title: 'Venue hold expires in 13 days', body: 'Intare Gardens needs the balance of the deposit by 15 June.', time: '2h ago', scope: 'shared', unread: true },
{ id: 'n2', title: 'Claudine completed a task', body: 'Confirm Gusaba hosting at family home, Remera', time: '5h ago', scope: 'bride-side', unread: true },
{ id: 'n3', title: 'Eric logged an expense', body: 'Sofa set — RWF 1,150,000', time: 'Yesterday', scope: 'groom-side', unread: true },
{ id: 'n4', title: '12 guests confirmed attendance', body: 'RSVPs from the church community and colleagues.', time: 'Yesterday', scope: 'shared', unread: false },
{ id: 'n5', title: 'Makeup trial on 8 June', body: 'Keza Beauty Lounge, 10:00.', time: '2 days ago', scope: 'private-bride', unread: false },
{ id: 'n6', title: 'Second suit fitting booked', body: 'Kimironko tailor, 12 June at 14:00.', time: '2 days ago', scope: 'private-groom', unread: false }];


export const invitationActivity: InvitationActivity[] = [
{ id: 'i1', guest: 'Alice Uwera', action: 'confirmed', time: '12 min ago' },
{ id: 'i2', guest: 'Fabrice Mugabo', action: 'sent', time: '1h ago' },
{ id: 'i3', guest: 'Sandrine Uwamahoro', action: 'opened', time: '2h ago' },
{ id: 'i4', guest: 'Olivier Ndayisaba', action: 'declined', time: '5h ago' },
{ id: 'i5', guest: 'Didier Kamanzi', action: 'confirmed', time: 'Yesterday' },
{ id: 'i6', guest: 'Grace Mutoni', action: 'opened', time: 'Yesterday' }];