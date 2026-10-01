import { Milestone, ScheduleItem } from '../types/wedding';

export const milestones: Milestone[] = [
{ id: 'm1', title: 'Kuranga — families introduced', date: '2026-11-14', description: 'Shema formally introduced to Aline’s family in Remera.', scope: 'shared', kind: 'Tradition', location: 'Remera, Kigali' },
{ id: 'm2', title: 'Budget & guest list drafted', date: '2027-01-20', description: 'RWF 23M shared budget agreed. 320 guests targeted.', scope: 'shared', kind: 'Planning' },
{ id: 'm3', title: 'Church & venue shortlisted', date: '2027-03-02', description: 'St. Michel Parish confirmed; three reception venues visited.', scope: 'shared', kind: 'Planning' },
{ id: 'm4', title: 'Photographer & décor booked', date: '2027-05-27', description: 'Deposits paid to Lens of Kigali, Isimbi Films and Imena Décor.', scope: 'shared', kind: 'Planning' },
{ id: 'm5', title: 'Inkwano cows prepared', date: '2027-06-05', description: 'Groom’s family finalises the dowry for Gusaba.', scope: 'groom-side', kind: 'Tradition', location: 'Musanze' },
{ id: 'm6', title: 'Gusaba & Gukwa', date: '2027-06-12', description: 'Formal request and dowry ceremony hosted by Aline’s family.', scope: 'shared', kind: 'Tradition', location: 'Family home, Remera' },
{ id: 'm7', title: 'Digital invitations sent', date: '2027-06-30', description: 'All 320 guests invited by WhatsApp and SMS.', scope: 'shared', kind: 'Deadline' },
{ id: 'm8', title: 'Final vendor payments', date: '2027-07-20', description: 'Balance due to venue, caterer and decorator.', scope: 'shared', kind: 'Deadline' },
{ id: 'm9', title: 'RSVP deadline', date: '2027-07-30', description: 'Headcount locked for catering and seating.', scope: 'shared', kind: 'Deadline' },
{ id: 'm10', title: 'Bridal shower', date: '2027-08-07', description: 'Hosted by Claudine with Aline’s friends and family.', scope: 'bride-side', kind: 'Tradition', location: 'Kacyiru' },
{ id: 'm11', title: 'Civil ceremony', date: '2027-08-15', description: 'Legal marriage at the sector office, followed by family lunch.', scope: 'shared', kind: 'Ceremony', location: 'Kicukiro Sector Office' },
{ id: 'm12', title: 'New home ready', date: '2027-08-21', description: 'All home preparation items delivered and set up.', scope: 'shared', kind: 'Deadline', location: 'Kimironko' },
{ id: 'm13', title: 'Wedding day', date: '2027-08-24', description: 'Church ceremony and reception.', scope: 'shared', kind: 'Ceremony', location: 'St. Michel Parish · Intare Gardens' }];


export const weddingDaySchedule: ScheduleItem[] = [
{ time: '07:30', title: 'Bride preparation', location: 'Keza Beauty Lounge', owner: 'Aline' },
{ time: '09:00', title: 'Groom & groomsmen ready', location: 'Family home, Kimironko', owner: 'Eric' },
{ time: '11:00', title: 'Church ceremony', location: 'St. Michel Parish', owner: 'Shema' },
{ time: '13:00', title: 'Couple photo session', location: 'Nyandungu Eco Park', owner: 'Lens of Kigali' },
{ time: '15:00', title: 'Guests arrive', location: 'Intare Gardens', owner: 'Claudine' },
{ time: '16:00', title: 'Couple entrance & Intore dance', location: 'Main tent', owner: 'MC Gatete' },
{ time: '16:45', title: 'Speeches & gifts (amaturo)', location: 'Main tent', owner: 'MC Gatete' },
{ time: '17:30', title: 'Dinner served', location: 'Main tent', owner: 'Umusambi Catering' },
{ time: '19:00', title: 'Cake & first dance', location: 'Dance floor', owner: 'DJ Ruti' },
{ time: '21:00', title: 'Send-off', location: 'Garden entrance', owner: 'Families' }];