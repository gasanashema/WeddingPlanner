import { Guest, GuestSide, RsvpStatus, SeatingTable } from '../types/wedding';

export const guests: Guest[] = [
{ id: 'g1', name: 'Jean Bosco Nkurunziza', side: 'bride', group: 'Family', phone: '+250 788 301 442', rsvp: 'confirmed', table: 2, plusOnes: 1, invitationOpened: true },
{ id: 'g2', name: 'Divine Ingabire', side: 'bride', group: 'Friends', phone: '+250 787 145 900', rsvp: 'confirmed', table: 6, plusOnes: 0, invitationOpened: true },
{ id: 'g3', name: 'Patrick Niyonzima', side: 'groom', group: 'Colleagues', phone: '+250 783 551 207', rsvp: 'confirmed', table: 8, plusOnes: 0, invitationOpened: true },
{ id: 'g4', name: 'Grace Mutoni', side: 'bride', group: 'Church', phone: '+250 788 920 316', rsvp: 'pending', table: null, plusOnes: 0, invitationOpened: true },
{ id: 'g5', name: 'Emmanuel Hakizimana', side: 'groom', group: 'Family', phone: '+250 785 110 628', rsvp: 'confirmed', table: 3, plusOnes: 1, invitationOpened: true },
{ id: 'g6', name: 'Chantal Uwimana', side: 'bride', group: 'Family', phone: '+250 788 674 015', rsvp: 'confirmed', table: 2, plusOnes: 0, invitationOpened: true },
{ id: 'g7', name: 'Olivier Ndayisaba', side: 'groom', group: 'Friends', phone: '+250 784 320 771', rsvp: 'declined', table: null, plusOnes: 0, invitationOpened: true },
{ id: 'g8', name: 'Solange Umutoni', side: 'both', group: 'Friends', phone: '+250 788 002 459', rsvp: 'confirmed', table: 9, plusOnes: 0, invitationOpened: true },
{ id: 'g9', name: 'Fabrice Mugabo', side: 'groom', group: 'Friends', phone: '+250 783 667 184', rsvp: 'pending', table: null, plusOnes: 1, invitationOpened: false },
{ id: 'g10', name: 'Diane Iradukunda', side: 'bride', group: 'Colleagues', phone: '+250 787 889 032', rsvp: 'confirmed', table: 8, plusOnes: 0, invitationOpened: true },
{ id: 'g11', name: 'Innocent Bizimana', side: 'groom', group: 'Family', phone: '+250 785 443 296', rsvp: 'confirmed', table: 5, plusOnes: 1, invitationOpened: true },
{ id: 'g12', name: 'Josiane Nyirahabimana', side: 'bride', group: 'Neighbours', phone: '+250 788 215 670', rsvp: 'pending', table: null, plusOnes: 0, invitationOpened: false },
{ id: 'g13', name: 'Didier Kamanzi', side: 'groom', group: 'Colleagues', phone: '+250 784 908 113', rsvp: 'confirmed', table: 11, plusOnes: 0, invitationOpened: true },
{ id: 'g14', name: 'Clarisse Umuhoza', side: 'bride', group: 'Friends', phone: '+250 787 356 481', rsvp: 'confirmed', table: 6, plusOnes: 0, invitationOpened: true },
{ id: 'g15', name: 'Yvonne Mukeshimana', side: 'bride', group: 'Family', phone: '+250 788 761 204', rsvp: 'declined', table: null, plusOnes: 0, invitationOpened: true },
{ id: 'g16', name: 'Claude Rwigema', side: 'groom', group: 'Elders', phone: '+250 785 019 877', rsvp: 'confirmed', table: 12, plusOnes: 1, invitationOpened: true },
{ id: 'g17', name: 'Alice Uwera', side: 'both', group: 'Church', phone: '+250 788 534 902', rsvp: 'confirmed', table: 7, plusOnes: 0, invitationOpened: true },
{ id: 'g18', name: 'Thierry Gasana', side: 'groom', group: 'Friends', phone: '+250 783 270 645', rsvp: 'confirmed', table: 11, plusOnes: 0, invitationOpened: true },
{ id: 'g19', name: 'Sandrine Uwamahoro', side: 'bride', group: 'Friends', phone: '+250 787 612 358', rsvp: 'pending', table: null, plusOnes: 0, invitationOpened: true },
{ id: 'g20', name: 'Pacifique Irakoze', side: 'groom', group: 'Neighbours', phone: '+250 784 185 520', rsvp: 'confirmed', table: null, plusOnes: 0, invitationOpened: true },
{ id: 'g21', name: 'Angélique Nyiraneza', side: 'bride', group: 'Elders', phone: '+250 788 447 031', rsvp: 'confirmed', table: 12, plusOnes: 0, invitationOpened: true },
{ id: 'g22', name: 'Samuel Mutabazi', side: 'groom', group: 'Church', phone: '+250 785 736 219', rsvp: 'pending', table: null, plusOnes: 0, invitationOpened: false },
{ id: 'g23', name: 'Esther Kayitesi', side: 'bride', group: 'Family', phone: '+250 788 390 864', rsvp: 'confirmed', table: 4, plusOnes: 0, invitationOpened: true },
{ id: 'g24', name: 'Marie Claire Uwineza', side: 'both', group: 'Colleagues', phone: '+250 787 051 693', rsvp: 'confirmed', table: null, plusOnes: 0, invitationOpened: true }];


export const guestGroups = ['Family', 'Friends', 'Church', 'Colleagues', 'Neighbours', 'Elders'];

/** Guests beyond the first loaded page, counted server-side. */
export const guestBaseline: Record<GuestSide, Record<RsvpStatus, number>> = {
  bride: { confirmed: 86, pending: 32, declined: 10 },
  groom: { confirmed: 83, pending: 36, declined: 11 },
  both: { confirmed: 28, pending: 8, declined: 2 }
};

export const seatingTables: SeatingTable[] = [
{ id: 1, name: 'Head table', note: 'Couple & bridal party', capacity: 10, reserved: 10 },
{ id: 2, name: 'Table 2', note: "Aline's family", capacity: 10, reserved: 6 },
{ id: 3, name: 'Table 3', note: "Shema's family", capacity: 10, reserved: 7 },
{ id: 4, name: 'Table 4', note: "Aline's family", capacity: 10, reserved: 8 },
{ id: 5, name: 'Table 5', note: "Shema's family", capacity: 10, reserved: 8 },
{ id: 6, name: 'Table 6', note: "Aline's friends", capacity: 10, reserved: 6 },
{ id: 7, name: 'Table 7', note: 'Church community', capacity: 10, reserved: 7 },
{ id: 8, name: 'Table 8', note: 'Colleagues', capacity: 10, reserved: 6 },
{ id: 9, name: 'Table 9', note: 'Mutual friends', capacity: 10, reserved: 7 },
{ id: 10, name: 'Table 10', note: 'Neighbours', capacity: 10, reserved: 6 },
{ id: 11, name: 'Table 11', note: "Shema's friends", capacity: 10, reserved: 5 },
{ id: 12, name: 'Table 12', note: 'Elders', capacity: 10, reserved: 5 }];


export const invitationStats = { sent: 296, opened: 251 };