import React from 'react';
import { BrowserRouter, Route, Routes } from 'react-router-dom';
import { Toaster } from 'sonner';
import { RoleProvider } from './contexts/RoleContext';
import { WeddingDataProvider } from './contexts/WeddingDataContext';
import { QuickAddProvider } from './contexts/QuickAddContext';
import { AppLayout } from './components/layout/AppLayout';
import { Overview } from './pages/Overview';
import { MyPlanning } from './pages/MyPlanning';
import { SharedWedding } from './pages/SharedWedding';
import { Tasks } from './pages/Tasks';
import { Budget } from './pages/Budget';
import { Guests } from './pages/Guests';
import { Seating } from './pages/Seating';
import { Timeline } from './pages/Timeline';
import { Vendors } from './pages/Vendors';
import { HomePreparation } from './pages/HomePreparation';
import { Invitations } from './pages/Invitations';
import { DesignSystem } from './pages/DesignSystem';

interface AppProps {
  /** Which of the four wedding accounts is signed in. */
  viewAs?: 'bride' | 'groom' | 'bride-support' | 'groom-support';
}

export function App({ viewAs = 'bride' }: AppProps) {
  return (
    <RoleProvider role={viewAs}>
      <WeddingDataProvider>
        <BrowserRouter>
          <QuickAddProvider>
            <Routes>
              <Route element={<AppLayout />}>
                <Route path="/" element={<Overview />} />
                <Route path="/my-planning" element={<MyPlanning />} />
                <Route path="/shared" element={<SharedWedding />} />
                <Route path="/tasks" element={<Tasks />} />
                <Route path="/budget" element={<Budget />} />
                <Route path="/guests" element={<Guests />} />
                <Route path="/seating" element={<Seating />} />
                <Route path="/timeline" element={<Timeline />} />
                <Route path="/vendors" element={<Vendors />} />
                <Route path="/home-preparation" element={<HomePreparation />} />
                <Route path="/invitations" element={<Invitations />} />
                <Route path="/design-system" element={<DesignSystem />} />
                <Route path="*" element={<Overview />} />
              </Route>
            </Routes>
          </QuickAddProvider>
        </BrowserRouter>
        <Toaster
          position="bottom-right"
          toastOptions={{
            style: { fontFamily: 'Inter, sans-serif', borderRadius: 8, border: '1px solid #E9E2D8', color: '#1F1B1A' }
          }} />
        
      </WeddingDataProvider>
    </RoleProvider>);

}