import users from '../data/users';

// Default user ID to use when no authentication is present
const DEFAULT_USER_ID = 'a7c84b31-e92f-4c7d-8456-98e2a521def0';

// User authentication service
const authService = {
  // Get the current user ID (will be replaced with JWT logic in the future)
  getCurrentUserId: (): string => {
    // This would normally check for JWT or session storage
    // For now, just return the default user ID
    return DEFAULT_USER_ID;
  },
  
  // Get the current username (will be replaced with JWT logic in the future)
  getCurrentUsername: (): string => {
    // Find the default user from user data
    const user = users.find(u => u.id === DEFAULT_USER_ID);
    return user?.userName || 'user';
  },
  
  // Check if current user has specific role
  hasRole: (role: string): boolean => {
    const user = users.find(u => u.id === DEFAULT_USER_ID);
    return user?.roleName === role;
  },
  
  // Check if user is authenticated
  isAuthenticated: (): boolean => {
    // This would normally check for valid JWT or session
    // For now, just return true
    return true;
  }
};

export default authService;
