import React from 'react';
import { useAuth } from '../../contexts/AuthContext';
import { Login } from '../../pages/Login';

export const ProtectedRoute: React.FC<{ children: React.ReactNode }> = ({ children }) => {
  const { isAuthenticated, isLoading } = useAuth();

  if (isLoading) {
    return (
      <div className="min-h-screen bg-[#FBF9F5] flex items-center justify-center">
        <div className="text-center">
          <div className="inline-block w-8 h-8 border-4 border-[#581C26] border-t-transparent rounded-full animate-spin mb-3"></div>
          <p className="text-sm font-medium text-[#6E6663]">Loading Ubukwe...</p>
        </div>
      </div>
    );
  }

  if (!isAuthenticated) {
    return <Login />;
  }

  return <>{children}</>;
};
