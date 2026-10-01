import { HomeItem, HomeTemplateItem, Side } from '../types/wedding';

export const homeItems: HomeItem[] = [
{ id: 'h1', name: 'Dinner plate set (24 pcs)', category: 'Plates', side: 'bride', quantity: 1, budget: 95000, deadline: '2027-05-18', assignee: 'bride-support', completed: true, notes: 'Ivory ceramic, bought at Simba.' },
{ id: 'h2', name: 'Spoons & cutlery set', category: 'Spoons', side: 'bride', quantity: 2, budget: 90000, deadline: '2027-05-18', assignee: 'bride-support', completed: true, notes: '' },
{ id: 'h3', name: 'Cups & drinking glasses', category: 'Cups', side: 'bride', quantity: 24, budget: 70000, deadline: '2027-06-20', assignee: 'bride', completed: false, notes: '' },
{ id: 'h4', name: 'Cooking pots set', category: 'Kitchen items', side: 'bride', quantity: 1, budget: 150000, deadline: '2027-06-28', assignee: 'bride-support', completed: false, notes: 'Stainless steel, 5 sizes.' },
{ id: 'h5', name: 'Thermos flasks', category: 'Kitchen items', side: 'bride', quantity: 3, budget: 45000, deadline: '2027-07-05', assignee: 'bride-support', completed: false, notes: 'For tea and porridge.' },
{ id: 'h6', name: 'Bed sheets (queen)', category: 'Bed sheets', side: 'bride', quantity: 4, budget: 140000, deadline: '2027-05-29', assignee: 'bride', completed: true, notes: '' },
{ id: 'h7', name: 'Living room curtains', category: 'Curtains', side: 'bride', quantity: 3, budget: 120000, deadline: '2027-05-29', assignee: 'bride', completed: true, notes: '' },
{ id: 'h8', name: 'Towels & bath set', category: 'Household items', side: 'bride', quantity: 6, budget: 60000, deadline: '2027-07-10', assignee: 'bride', completed: false, notes: '' },
{ id: 'h9', name: 'Mosquito nets', category: 'Household items', side: 'bride', quantity: 3, budget: 30000, deadline: '2027-07-10', assignee: 'bride-support', completed: false, notes: '' },
{ id: 'h10', name: 'Ibiseke (traditional baskets)', category: 'Other', side: 'bride', quantity: 4, budget: 80000, deadline: '2027-08-01', assignee: 'bride-support', completed: false, notes: 'Decorative — for the living room.' },

{ id: 'h11', name: 'House rent — Kimironko (3 months)', category: 'House rent', side: 'groom', quantity: 1, budget: 900000, deadline: '2027-05-01', assignee: 'groom', completed: true, notes: '2-bedroom, near Kimironko market.' },
{ id: 'h12', name: 'Refrigerator (double door)', category: 'Refrigerator', side: 'groom', quantity: 1, budget: 780000, deadline: '2027-05-24', assignee: 'groom', completed: true, notes: '' },
{ id: 'h13', name: 'Smart TV 55"', category: 'TV', side: 'groom', quantity: 1, budget: 650000, deadline: '2027-07-15', assignee: 'groom', completed: false, notes: '' },
{ id: 'h14', name: '5-seater sofa set', category: 'Sofa', side: 'groom', quantity: 1, budget: 1150000, deadline: '2027-05-28', assignee: 'groom-support', completed: true, notes: 'Made by a carpenter in Gisozi.' },
{ id: 'h15', name: 'Queen bed with mattress', category: 'Bed', side: 'groom', quantity: 1, budget: 620000, deadline: '2027-07-01', assignee: 'groom-support', completed: false, notes: '' },
{ id: 'h16', name: 'Dining table (6 chairs)', category: 'Dining table', side: 'groom', quantity: 1, budget: 480000, deadline: '2027-07-01', assignee: 'groom-support', completed: false, notes: '' },
{ id: 'h17', name: 'Gas cooker & cylinder', category: 'Household appliances', side: 'groom', quantity: 1, budget: 320000, deadline: '2027-07-20', assignee: 'groom', completed: false, notes: '' },
{ id: 'h18', name: 'Water dispenser', category: 'Household appliances', side: 'groom', quantity: 1, budget: 150000, deadline: '2027-07-20', assignee: 'groom', completed: false, notes: '' },
{ id: 'h19', name: 'Home Wi-Fi installation', category: 'Other', side: 'groom', quantity: 1, budget: 60000, deadline: '2027-08-10', assignee: 'groom', completed: false, notes: '' }];


export const homeTemplates: Record<Side, HomeTemplateItem[]> = {
  bride: [
  { name: 'Serving trays', category: 'Kitchen items', budget: 35000 },
  { name: 'Table cloths', category: 'Household items', budget: 40000 },
  { name: 'Blankets', category: 'Bed sheets', budget: 90000 },
  { name: 'Iron & ironing board', category: 'Household items', budget: 55000 },
  { name: 'Water jugs', category: 'Cups', budget: 20000 }],

  groom: [
  { name: 'Washing machine', category: 'Household appliances', budget: 550000 },
  { name: 'Microwave', category: 'Household appliances', budget: 140000 },
  { name: 'Wardrobe', category: 'Bed', budget: 380000 },
  { name: 'Water tank (1,000 L)', category: 'Household appliances', budget: 220000 },
  { name: 'Inverter / power backup', category: 'Household appliances', budget: 450000 }]

};

export const homeCategories: Record<Side, string[]> = {
  bride: ['Plates', 'Spoons', 'Cups', 'Kitchen items', 'Bed sheets', 'Curtains', 'Household items', 'Other'],
  groom: ['House rent', 'Refrigerator', 'TV', 'Sofa', 'Bed', 'Dining table', 'Household appliances', 'Other']
};