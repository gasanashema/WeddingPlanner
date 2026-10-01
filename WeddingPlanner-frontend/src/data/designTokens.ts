export const colorTokens = [
{
  group: 'Primary — Wine',
  swatches: [
  { name: 'wine-50', hex: '#FAF2F3' },
  { name: 'wine-100', hex: '#F2E0E3' },
  { name: 'wine-500', hex: '#8C3A4A' },
  { name: 'wine-700', hex: '#5E2331' },
  { name: 'wine-800', hex: '#4B1B27' }]

},
{
  group: 'Accent — Champagne',
  swatches: [
  { name: 'gold-50', hex: '#FBF7EE' },
  { name: 'gold-200', hex: '#EADBB6' },
  { name: 'gold-300', hex: '#DBC38E' },
  { name: 'gold-500', hex: '#B08E4E' },
  { name: 'gold-700', hex: '#6D562C' }]

},
{
  group: 'Neutrals',
  swatches: [
  { name: 'ivory', hex: '#FAF7F2' },
  { name: 'surface', hex: '#FFFDFA' },
  { name: 'line', hex: '#E9E2D8' },
  { name: 'ink-500', hex: '#6B625D' },
  { name: 'ink', hex: '#1F1B1A' }]

},
{
  group: 'Feedback',
  swatches: [
  { name: 'success-600', hex: '#4E7A5B' },
  { name: 'warning-600', hex: '#B7791F' },
  { name: 'danger-600', hex: '#B0473D' }]

},
{
  group: 'Access areas',
  swatches: [
  { name: 'private (ink-700)', hex: '#3A3331' },
  { name: 'shared (wine-700)', hex: '#5E2331' },
  { name: 'bride-side', hex: '#7A5673' },
  { name: 'groom-side', hex: '#4A6478' }]

}];


export const typeScale = [
{ name: 'Display', spec: 'Playfair Display · 44/52 · 600', className: 'font-serif text-[44px] leading-[52px] font-semibold', sample: '68% planned' },
{ name: 'Page title', spec: 'Playfair Display · 32/40 · 600', className: 'font-serif text-[32px] leading-10 font-semibold', sample: 'Shared Wedding' },
{ name: 'Section heading', spec: 'Playfair Display · 20/28 · 600', className: 'font-serif text-xl font-semibold', sample: 'Upcoming deadlines' },
{ name: 'Card title', spec: 'Inter · 15/22 · 600', className: 'text-[15px] font-semibold', sample: 'Budget summary' },
{ name: 'Body', spec: 'Inter · 14/20 · 400', className: 'text-sm', sample: 'Intare Gardens is holding 24 August for you.' },
{ name: 'Label', spec: 'Inter · 12/16 · 500', className: 'text-xs font-medium text-ink-500', sample: 'Assigned to' },
{ name: 'Number', spec: 'Inter · 24/32 · 600 · tabular', className: 'text-2xl font-semibold tnum', sample: 'RWF 12,395,000' }];


export const spacingTokens = [4, 8, 12, 16, 24, 32, 48];
export const radiusTokens = [
{ name: 'sm', value: '4px', cls: 'rounded' },
{ name: 'md', value: '6px', cls: 'rounded-md' },
{ name: 'lg', value: '8px', cls: 'rounded-lg' }];