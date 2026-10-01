import { Person, Role } from '../types/wedding';

/** The demo is pinned to a fixed "today" so dates and countdowns stay realistic. */
export const TODAY = '2027-06-02';

export const wedding = {
  name: "Shema & Aline's Wedding",
  couple: 'Shema & Aline',
  date: '2027-08-24',
  city: 'Kigali',
  venue: 'Intare Gardens, Rebero',
  church: 'St. Michel Parish, Kiyovu',
  inviteLink: 'https://ubukwe.rw/i/shema-aline-2027',
  rsvpBy: '2027-07-30'
};

export const people: Record<Role, Person> = {
  bride: {
    role: 'bride',
    name: 'Aline Uwase',
    firstName: 'Aline',
    initials: 'AU',
    roleLabel: 'Bride',
    relation: 'Bride',
    side: 'bride',
    email: 'aline.uwase@gmail.com',
    phone: '+250 788 412 305'
  },
  groom: {
    role: 'groom',
    name: 'Shema Mugisha',
    firstName: 'Shema',
    initials: 'SM',
    roleLabel: 'Groom',
    relation: 'Groom',
    side: 'groom',
    email: 'shema.mugisha@gmail.com',
    phone: '+250 783 220 918'
  },
  'bride-support': {
    role: 'bride-support',
    name: 'Claudine Mukamana',
    firstName: 'Claudine',
    initials: 'CM',
    roleLabel: 'Bride-side Support',
    relation: "Aline's aunt",
    side: 'bride',
    email: 'claudine.mukamana@yahoo.fr',
    phone: '+250 788 604 117'
  },
  'groom-support': {
    role: 'groom-support',
    name: 'Eric Habimana',
    firstName: 'Eric',
    initials: 'EH',
    roleLabel: 'Groom-side Support',
    relation: "Shema's elder brother",
    side: 'groom',
    email: 'eric.habimana@gmail.com',
    phone: '+250 785 931 442'
  }
};

export const roles: Role[] = ['bride', 'groom', 'bride-support', 'groom-support'];