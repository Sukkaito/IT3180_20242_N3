import api from '../api/axios';

// Default user ID to use when no authentication is present
const DEFAULT_USER_ID = 'a7c84b31-e92f-4c7d-8456-98e2a521def0';

// Cache for authentication status
let authCache = {
  isAuthenticated: false,
  timestamp: 0
};

// Maximum age of cached auth status in milliseconds (1 minute)
const AUTH_CACHE_MAX_AGE = 60000;

// Helper function to get cookie value by name
export const getCookie = (name: string): string | null => {
  const cookieString = document.cookie;
  const cookies = cookieString.split('; ');
  const cookie = cookies.find(c => c.startsWith(`${name}=`));
  return cookie ? cookie.split('=')[1] : null;
};

// User authentication service
const authService = {
  // Get the current user ID (will be replaced with JWT logic in the future)
  getCurrentUserId: (): string | null => {
  try {

    const userId = getCookie('USERID');
    
    if (!userId) {
      // Fall back to default user if no token exists
      return null;
    }
  
    return userId
  } catch (error) {
    console.error('Error retrieving user ID from token:', error);
    // Fall back to default user on error
    return DEFAULT_USER_ID;
  }
},
  
  // Get the current username (will be replaced with JWT logic in the future)
  getCurrentUsername: (): string => {
    try {
      const username = getCookie('USERNAME');
      if (username) {
        // If username exists in cookie, return it
        return username;
      }

      // Fall back to default if token format is invalid
      return 'Guest';
    } catch (error) {
      console.error('Error retrieving username from token:', error);
      // Fall back to default user on error
      return 'Guest';
    }
  },
  
  // Check if user is authenticated
  isAuthenticated: async (): Promise<boolean> => {
    // Use cached value if still valid
    const now = Date.now();
    if (authCache.timestamp > 0 && now - authCache.timestamp < AUTH_CACHE_MAX_AGE) {
      console.log('Using cached authentication status');
      return authCache.isAuthenticated;
    }
    
    try {
      console.log('Sending authentication verification request...');
      const response = await api.post('/api/auth/verify', {});
      
      console.log('Authentication verification response:', response);
      
      // Update cache
      authCache = {
        isAuthenticated: response.status === 200,
        timestamp: now
      };
      
      return authCache.isAuthenticated;
    } catch (error) {
      console.error('Authentication verification failed:', error);
      
      // Update cache for failed auth
      authCache = {
        isAuthenticated: false,
        timestamp: now
      };
      
      return false;
    }
  },

  login: async (username: string, password: string): Promise<boolean> => {
    try {
      // Make API call to the login endpoint
      const response = await api.post('/api/auth/login', {
        username,
        password
      });
      
      // If login successful (status 2xx), the server will set the cookie automatically
      // via the Set-Cookie header, so we don't need to manually set it
      if (response.status >= 200 && response.status < 300) {
        sessionStorage.setItem('AUTHORIZATION', response.data);
        // Clear auth cache on successful login
        authCache = { isAuthenticated: true, timestamp: Date.now() };
        return true;
      }
      return false;
    } catch (error) {
      console.error('Login failed:', error);
      return false;
    }
  },
  
  logout: async (): Promise<void> => {
    // Clear the authentication token cookie
    try {
      await api.post('/api/auth/logout', {}, {
        withCredentials: true // Ensure cookies are sent with the request
      });

      document.cookie = '';
      sessionStorage.removeItem('AUTHORIZATION');
      // Clear auth cache on logout
      authCache = { isAuthenticated: false, timestamp: Date.now() };
    } catch (error) {
      console.error('Logout failed:', error);
    }

  },

  register: async (name: string, username: string, password: string, email: string): Promise<{ success: boolean; message?: string }> => {
    try {
      // Make API call to the register endpoint
      const response = await api.post('/api/auth/register', {
        name,
        username,
        password,
        email
      });
      
      // If registration successful (status 2xx)
      if (response.status >= 200 && response.status < 300) {
        return { success: true };
      }
      return { success: false, message: 'Registration failed' };
    } catch (error: any) {
      console.error('Registration failed:', error);
      const errorMessage = error.response?.data?.message || 'Registration failed';
      return { success: false, message: errorMessage };
    }
  },
};

export default authService;
