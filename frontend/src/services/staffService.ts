import api from '../api/axios';
import { User } from '../data/users';
import userService from './userService';

// Staff service for admin-specific operations
const staffService = {
  // Get current staff profile
  getCurrentStaff: async (): Promise<User | undefined> => {
    try {
      const response = await api.get('/api/staff/me');
      return response.data;
    } catch (error) {
      console.error('Error fetching current staff profile:', error);
      
      // Fallback: simulate getting the current user (admin)
      // In a real app, this would come from auth context
      const users = await userService.getAllUsers();
      return users.find(user => user.roleName === 'ADMIN');
    }
  },
  
  // Update staff profile (excluding password)
  updateProfile: async (data: Partial<Omit<User, 'id' | 'roleName' | 'password'>>): Promise<User> => {
    try {
      const response = await api.put('/api/staff/me', data);
      return response.data;
    } catch (error) {
      console.error('Error updating staff profile:', error);
      throw error;
    }
  },
  
  // Change password
  changePassword: async (currentPassword: string, newPassword: string): Promise<void> => {
    try {
      await api.post('/api/staff/me/password', { 
        currentPassword, 
        newPassword 
      });
    } catch (error) {
      console.error('Error changing password:', error);
      throw error;
    }
  },
  
  // Get staff activity logs
  getActivityLogs: async (): Promise<any[]> => {
    try {
      const response = await api.get('/api/staff/me/logs');
      return response.data;
    } catch (error) {
      console.error('Error fetching activity logs:', error);
      // Return dummy data for fallback
      return [
        { id: 1, action: 'Book added', timestamp: new Date().toISOString(), details: 'Added "The Great Gatsby"' },
        { id: 2, action: 'Loan approved', timestamp: new Date(Date.now() - 24*60*60*1000).toISOString(), details: 'Approved loan request #1234' },
        { id: 3, action: 'User edited', timestamp: new Date(Date.now() - 2*24*60*60*1000).toISOString(), details: 'Updated user profile for johndoe' }
      ];
    }
  }
};

export default staffService;
