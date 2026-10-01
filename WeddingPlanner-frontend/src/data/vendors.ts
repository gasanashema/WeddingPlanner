import { Vendor, VendorCategory } from '../types/wedding';

export const vendorCategories: VendorCategory[] = [
'Venue',
'Caterer',
'Decorator',
'Photographer',
'Videographer',
'DJ / Entertainment',
'Makeup',
'Transport',
'Other'];


export const vendors: Vendor[] = [
{ id: 'v1', name: 'Intare Gardens', category: 'Venue', contactName: 'Josué Ntwari', phone: '+250 788 300 120', email: 'events@intaregardens.rw', cost: 4500000, paid: 1350000, paymentStatus: 'deposit', bookingStatus: 'pending', notes: 'Holding 24 Aug until 15 June. Includes chairs, tents and parking for 80 cars.' },
{ id: 'v2', name: 'Umusambi Catering', category: 'Caterer', contactName: 'Béatrice Mukandori', phone: '+250 788 511 904', email: 'hello@umusambi.rw', cost: 4800000, paid: 60000, paymentStatus: 'unpaid', bookingStatus: 'shortlisted', notes: 'RWF 15,000 per guest. Tasting done — loved the brochettes and isombe.' },
{ id: 'v3', name: 'Imena Décor', category: 'Decorator', contactName: 'Sonia Ishimwe', phone: '+250 787 420 663', email: 'sonia@imenadecor.rw', cost: 2600000, paid: 1000000, paymentStatus: 'deposit', bookingStatus: 'booked', notes: 'Wine & champagne palette. Final layout review on 18 June.' },
{ id: 'v4', name: 'Lens of Kigali', category: 'Photographer', contactName: 'Kevin Manzi', phone: '+250 783 902 118', email: 'book@lensofkigali.com', cost: 900000, paid: 450000, paymentStatus: 'deposit', bookingStatus: 'booked', notes: 'Full day + album. Photo session at Nyandungu Eco Park.' },
{ id: 'v5', name: 'Isimbi Films', category: 'Videographer', contactName: 'Aimable Rukundo', phone: '+250 785 667 241', email: 'isimbifilms@gmail.com', cost: 900000, paid: 400000, paymentStatus: 'deposit', bookingStatus: 'booked', notes: 'Highlight reel within 3 weeks.' },
{ id: 'v6', name: 'MC Gatete & DJ Ruti', category: 'DJ / Entertainment', contactName: 'Gatete Emile', phone: '+250 788 145 330', email: 'mcgatete@gmail.com', cost: 600000, paid: 300000, paymentStatus: 'deposit', bookingStatus: 'booked', notes: 'Bilingual MC. Playlist draft due 1 August.' },
{ id: 'v7', name: 'Keza Beauty Lounge', category: 'Makeup', contactName: 'Keza Umwali', phone: '+250 787 209 556', email: 'keza@kezabeauty.rw', cost: 350000, paid: 350000, paymentStatus: 'paid', bookingStatus: 'booked', notes: 'Bride + 6 bridesmaids. Trial on 8 June.' },
{ id: 'v8', name: 'Rwanda Luxe Rides', category: 'Transport', contactName: 'Frank Mugenzi', phone: '+250 784 330 871', email: 'bookings@luxerides.rw', cost: 600000, paid: 0, paymentStatus: 'unpaid', bookingStatus: 'pending', notes: 'Two cars for the couple + one coaster for bridal party.' },
{ id: 'v9', name: 'Inganzo Intore Troupe', category: 'Other', contactName: 'Jean Paul Gakwaya', phone: '+250 785 008 412', email: 'inganzo.troupe@gmail.com', cost: 350000, paid: 0, paymentStatus: 'unpaid', bookingStatus: 'pending', notes: 'Traditional dance for Gusaba and reception entrance.' }];