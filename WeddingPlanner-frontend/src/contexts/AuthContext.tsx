import React, { createContext, useContext, useState, useEffect, ReactNode } from 'react';
import { authApi, UserDto, LoginPayload, RegisterPayload } from '../api/authApi';

interface AuthContextType {
  user: UserDto | null;
  token: string | null;
  isAuthenticated: boolean;
  isLoading: boolean;
  login: (payload: LoginPayload) => Promise<void>;
  register: (payload: RegisterPayload) => Promise<void>;
  logout: () => void;
}

const AuthContext = createContext<AuthContextType | undefined>(undefined);

export const AuthProvider: React.FC<{ children: ReactNode }> = ({ children }) => {
  const [user, setUser] = useState<UserDto | null>(null);
  const [token, setToken] = useState<string | null>(localStorage.getItem('wedplan_token'));
  const [isLoading, setIsLoading] = useState<boolean>(true);

  useEffect(() => {
    const initAuth = async () => {
      if (token) {
        try {
          const res = await authApi.getMe();
          setUser(res.data);
        } catch (err) {
          console.error('Failed to fetch authenticated user:', err);
          logout();
        }
      }
      setIsLoading(false);
    };

    initAuth();
  }, [token]);

  const login = async (payload: LoginPayload) => {
    const res = await authApi.login(payload);
    localStorage.setItem('wedplan_token', res.data.token);
    setToken(res.data.token);
    setUser(res.data.user);
  };

  const register = async (payload: RegisterPayload) => {
    const res = await authApi.register(payload);
    localStorage.setItem('wedplan_token', res.data.token);
    setToken(res.data.token);
    setUser(res.data.user);
  };

  const logout = () => {
    localStorage.removeItem('wedplan_token');
    setToken(null);
    setUser(null);
  };

  return (
    <AuthContext.Provider
      value={{
        user,
        token,
        isAuthenticated: !!user,
        isLoading,
        login,
        register,
        logout,
      }}
    >
      {children}
    </AuthContext.Provider>
  );
};

export const useAuth = () => {
  const context = useContext(AuthContext);
  if (!context) {
    throw new Error('useAuth must be used within an AuthProvider');
  }
  return context;
};
