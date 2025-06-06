import { useState, useEffect, ReactNode } from 'react';
import { AuthContext } from '../context/AuthContext';
import authService from '../services/authService';

export const AuthProvider = ({ children }: { children: ReactNode }) => {
  const [isAuthenticated, setIsAuthenticated] = useState<boolean>(false);
  const [isLoading, setIsLoading] = useState<boolean>(true);
  const [username, setUsername] = useState<string | null>(null);
  const [userId, setUserId] = useState<string | null>(null);

  const checkAuthStatus = async () => {
    setIsLoading(true);
    try {
      console.log('Checking authentication status...');
      const authenticated = await authService.isAuthenticated();
      console.log('Authentication check result:', authenticated);
      setIsAuthenticated(authenticated);
      
      if (authenticated) {
        const username = authService.getCurrentUsername();
        const userId = authService.getCurrentUserId();
        console.log('Authenticated user:', { username, userId });
        setUsername(username);
        setUserId(userId);
      } else {
        console.log('User is not authenticated');
        setUsername(null);
        setUserId(null);
      }
    } catch (error) {
      console.error('Error checking auth status:', error);
      setIsAuthenticated(false);
      setUsername(null);
      setUserId(null);
    } finally {
      setIsLoading(false);
    }
  };

  useEffect(() => {
    checkAuthStatus();
  }, []);

  const logout = async () => {
    await authService.logout();
    setIsAuthenticated(false);
    setUsername(null);
    setUserId(null);
  };

  return (
    <AuthContext.Provider value={{ 
      isAuthenticated, 
      username,
      userId,
      isLoading,
      checkAuthStatus,
      logout
    }}>
      {children}
    </AuthContext.Provider>
  );
};