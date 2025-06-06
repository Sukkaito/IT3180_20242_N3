import { createContext } from 'react';

export interface AuthContextType {
  isAuthenticated: boolean;
  username?: string | null;
  userId?: string | null;
  isLoading: boolean;
  checkAuthStatus: () => Promise<void>;
  logout: () => Promise<void>;
}

// Create context with default values
export const AuthContext = createContext<AuthContextType>({
  isAuthenticated: false,
  username: null,
  userId: null,
  isLoading: true,
  checkAuthStatus: async () => {},
  logout: async () => {},
});
