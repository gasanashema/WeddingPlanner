import { Task } from '../types/wedding';

export const tasks: Task[] = [
{ id: 't1', title: 'Confirm wedding venue', category: 'Venue', scope: 'shared', assignee: 'groom', due: '2027-06-10', priority: 'high', status: 'in-progress', budget: 4500000, notes: 'Intare Gardens is holding 24 Aug until 15 June. Pay the 30% deposit to secure it.' },
{ id: 't2', title: 'Finalize decoration', category: 'Decoration', scope: 'shared', assignee: 'bride', due: '2027-06-20', priority: 'high', status: 'in-progress', budget: 2800000, notes: 'Imena Décor sent two concepts — wine and champagne palette, low centrepieces.' },
{ id: 't3', title: 'Order wedding outfits', category: 'Clothing', scope: 'shared', assignee: 'bride', due: '2027-06-14', priority: 'medium', status: 'not-started', budget: 1200000, notes: 'Coordinated mushanana and suit fabrics for the reception.' },
{ id: 't4', title: 'Send digital invitations', category: 'Invitations', scope: 'shared', assignee: 'groom', due: '2027-06-30', priority: 'medium', status: 'not-started', budget: 150000, notes: 'WhatsApp and SMS for most guests. Printed cards for elders only.' },
{ id: 't5', title: 'Confirm catering', category: 'Food', scope: 'shared', assignee: 'groom', due: '2027-06-18', priority: 'high', status: 'not-started', budget: 5200000, notes: 'Umusambi Catering tasting done. Confirm the final menu for 320 guests.' },
{ id: 't6', title: 'Book photographer', category: 'Photography', scope: 'shared', assignee: 'bride', due: '2027-05-15', priority: 'high', status: 'completed', budget: 900000, notes: 'Lens of Kigali — full-day coverage and printed album.' },
{ id: 't7', title: 'Register civil ceremony at Kicukiro Sector', category: 'Other', scope: 'shared', assignee: 'groom', due: '2027-05-05', priority: 'high', status: 'completed', budget: 20000, notes: 'Civil ceremony booked for 15 August, 10:00.' },
{ id: 't8', title: 'Book St. Michel Parish', category: 'Other', scope: 'shared', assignee: 'groom', due: '2027-04-20', priority: 'high', status: 'completed', budget: 150000, notes: 'Mass at 11:00. Pre-marriage classes completed.' },
{ id: 't9', title: 'Set the wedding date', category: 'Other', scope: 'shared', assignee: 'bride', due: '2027-01-10', priority: 'medium', status: 'completed', budget: 0, notes: '' },
{ id: 't10', title: 'Draft the guest list', category: 'Other', scope: 'shared', assignee: 'bride', due: '2027-02-15', priority: 'medium', status: 'completed', budget: 0, notes: 'Target 320 guests across both families.' },
{ id: 't11', title: 'Book the MC', category: 'Entertainment', scope: 'shared', assignee: 'groom', due: '2027-05-20', priority: 'medium', status: 'completed', budget: 600000, notes: 'MC Gatete — bilingual Kinyarwanda / English.' },
{ id: 't12', title: 'Shortlist caterers', category: 'Food', scope: 'shared', assignee: 'groom', due: '2027-04-10', priority: 'medium', status: 'completed', budget: 0, notes: '' },
{ id: 't13', title: 'Choose wedding colours', category: 'Decoration', scope: 'shared', assignee: 'bride', due: '2027-03-01', priority: 'low', status: 'completed', budget: 0, notes: 'Wine, champagne and ivory.' },
{ id: 't14', title: 'Open joint wedding savings account', category: 'Other', scope: 'shared', assignee: 'groom', due: '2027-01-25', priority: 'low', status: 'completed', budget: 0, notes: '' },
{ id: 't15', title: 'Book videographer', category: 'Photography', scope: 'shared', assignee: 'bride', due: '2027-05-25', priority: 'medium', status: 'completed', budget: 900000, notes: 'Isimbi Films — highlight reel plus full ceremony.' },

{ id: 't16', title: 'Makeup trial with Keza Beauty', category: 'Clothing', scope: 'private-bride', assignee: 'bride', due: '2027-06-08', priority: 'medium', status: 'in-progress', budget: 80000, notes: 'Bring reference photos of soft glam looks.' },
{ id: 't17', title: 'Book makeup artist', category: 'Clothing', scope: 'private-bride', assignee: 'bride', due: '2027-04-28', priority: 'medium', status: 'completed', budget: 350000, notes: '' },
{ id: 't18', title: 'Choose bridesmaids', category: 'Other', scope: 'private-bride', assignee: 'bride', due: '2027-03-15', priority: 'low', status: 'completed', budget: 0, notes: '' },
{ id: 't19', title: 'Buy wedding dress', category: 'Clothing', scope: 'private-bride', assignee: 'bride', due: '2027-05-10', priority: 'high', status: 'completed', budget: 480000, notes: 'Keep this away from Shema!' },
{ id: 't20', title: 'First wedding dress fitting', category: 'Clothing', scope: 'private-bride', assignee: 'bride', due: '2027-05-29', priority: 'medium', status: 'completed', budget: 0, notes: '' },

{ id: 't21', title: 'Buy imishanana for bridesmaids', category: 'Clothing', scope: 'bride-side', assignee: 'bride-support', due: '2027-06-25', priority: 'medium', status: 'not-started', budget: 900000, notes: 'Six bridesmaids. Champagne fabric with wine sash.' },
{ id: 't22', title: 'Prepare Gusaba gifts (ibiseke & inzoga)', category: 'Other', scope: 'bride-side', assignee: 'bride-support', due: '2027-05-28', priority: 'high', status: 'overdue', budget: 400000, notes: 'Baskets ordered from Nyamirambo weavers — collection pending.' },
{ id: 't23', title: 'Confirm Gusaba hosting at family home, Remera', category: 'Other', scope: 'bride-side', assignee: 'bride-support', due: '2027-05-20', priority: 'high', status: 'completed', budget: 0, notes: '' },
{ id: 't24', title: 'Book bridal shower venue', category: 'Other', scope: 'bride-side', assignee: 'bride-support', due: '2027-05-30', priority: 'low', status: 'completed', budget: 200000, notes: '' },
{ id: 't25', title: 'Buy kitchen set for new home', category: 'Home Preparation', scope: 'bride-side', assignee: 'bride-support', due: '2027-05-18', priority: 'medium', status: 'completed', budget: 185000, notes: '' },

{ id: 't26', title: 'Suit tailoring in Kimironko', category: 'Clothing', scope: 'private-groom', assignee: 'groom', due: '2027-06-15', priority: 'medium', status: 'in-progress', budget: 600000, notes: 'Second fitting booked.' },
{ id: 't27', title: 'Order wedding bands', category: 'Clothing', scope: 'private-groom', assignee: 'groom', due: '2027-04-30', priority: 'high', status: 'completed', budget: 700000, notes: 'Engraving: "24.08.27"' },
{ id: 't28', title: 'Choose groomsmen', category: 'Other', scope: 'private-groom', assignee: 'groom', due: '2027-03-15', priority: 'low', status: 'completed', budget: 0, notes: '' },

{ id: 't29', title: 'Book Intore dance troupe for Gusaba', category: 'Entertainment', scope: 'groom-side', assignee: 'groom-support', due: '2027-05-30', priority: 'high', status: 'overdue', budget: 350000, notes: 'Inganzo troupe waiting for deposit.' },
{ id: 't30', title: 'Arrange inkwano (dowry cows)', category: 'Other', scope: 'groom-side', assignee: 'groom-support', due: '2027-06-05', priority: 'high', status: 'in-progress', budget: 1500000, notes: 'Two cows from the family farm in Musanze.' },
{ id: 't31', title: 'Transport for family from Musanze', category: 'Transportation', scope: 'groom-side', assignee: 'groom-support', due: '2027-08-10', priority: 'low', status: 'not-started', budget: 450000, notes: 'One 30-seat coaster bus.' },
{ id: 't32', title: 'Find a house in Kimironko', category: 'Home Preparation', scope: 'groom-side', assignee: 'groom', due: '2027-05-01', priority: 'high', status: 'completed', budget: 0, notes: '' },
{ id: 't33', title: 'Confirm Gusaba delegation list', category: 'Other', scope: 'groom-side', assignee: 'groom-support', due: '2027-05-25', priority: 'medium', status: 'completed', budget: 0, notes: '' }];