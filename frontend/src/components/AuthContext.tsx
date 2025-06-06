import { useState, useEffect, ReactNode, useCallback, useRef } from 'react';
import { AuthContext } from '../context/AuthContext';
import authService from '../services/authService';

export const AuthProvider = ({ children }: { children: ReactNode }) => {
  const [isAuthenticated, setIsAuthenticated] = useState<boolean>(false);
  const [isLoading, setIsLoading] = useState<boolean>(true);
  const [username, setUsername] = useState<string | null>(null);
  const [userId, setUserId] = useState<string | null>(null);
  
  // Use a ref instead of state to track last check time to avoid re-renders
  const lastCheckedRef = useRef<number>(0);

  const checkAuthStatus = useCallback(async (force = false) => {
    // Skip redundant checks within a short time period unless forced
    const now = Date.now();
    if (!force && lastCheckedRef.current > 0 && now - lastCheckedRef.current < 60000) {
      console.log('Using cached auth status, last checked', new Date(lastCheckedRef.current).toLocaleTimeString());
      return;
    }
    
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
      lastCheckedRef.current = now;
    } catch (error) {
      console.error('Error checking auth status:', error);
      setIsAuthenticated(false);
      setUsername(null);
      setUserId(null);
    } finally {
      setIsLoading(false);
    }
  }, []); // Empty dependency array - function won't be recreated

  useEffect(() => {
    // Only check once on initial mount
    checkAuthStatus();
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, []);

  const logout = async () => {
    await authService.logout();
    setIsAuthenticated(false);
    setUsername(null);
    setUserId(null);
    lastCheckedRef.current = 0; // Reset last checked time
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