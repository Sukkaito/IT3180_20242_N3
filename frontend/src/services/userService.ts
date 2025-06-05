import { BaseService } from './baseService';
import userData, { User } from '../data/users';
import api from '../api/axios';

// Define user roles as constants
export const USER_ROLES = {
    ADMIN: 'ADMIN',
    STAFF: 'STAFF',
    USER: 'USER'
};

// Create base service for users
const userBaseService = new BaseService<User>('users', userData);

// Custom user service that handles different endpoints for staff and regular users
const userService = {
    // Get all users (admin only endpoint)
    getAllUsers: async (): Promise<User[]> => {
        try {
            const response = await api.get('/api/staff/users'); // Not working
            return response.data;
        } catch (error) {
            console.error('Error fetching all users:', error);
            // Return from base service if API fails (get users)
            return userBaseService.getAll();
        }
    },

    // Get staff members only
    getAllStaff: async (): Promise<User[]> => {
        try {
            const response = await api.get('/api/staff');
            return response.data;
        } catch (error) {
            console.error('Error fetching staff members:', error);
            // Get all users and filter staff
            const users = await userBaseService.getAll();
            return users.filter(user => 
                user.roleName === USER_ROLES.ADMIN || user.roleName === USER_ROLES.STAFF
            );
        }
    },

    // Get regular users only
    getAllRegularUsers: async (): Promise<User[]> => {
        try {
            const response = await api.get('/api/users');
            return response.data;
        } catch (error) {
            console.error('Error fetching regular users:', error);
            // Get all users and filter regular users
            const users = await userBaseService.getAll();
            return users.filter(user => user.roleName === USER_ROLES.USER);
        }
    },

    // Get user by ID (chooses correct endpoint based on role)
    getUserById: async (id: string): Promise<User | undefined> => {
        try {
            // Try to determine if this is a staff member first
            const users = await userBaseService.getAll();
            const existingUser = users.find(user => user.id === id);
            const isStaff = existingUser?.roleName === USER_ROLES.ADMIN || existingUser?.roleName === USER_ROLES.STAFF;
            
            // Use the appropriate endpoint
            const endpoint = isStaff ? `/api/staff/${id}` : `/api/users/${id}`;
            const response = await api.get(endpoint);
            return response.data;
        } catch (error) {
            console.error(`Error fetching user with id ${id}:`, error);
            // Return from local data as fallback
            const users = await userBaseService.getAll();
            return users.find(user => user.id === id);
        }
    },

    // Create a new user (regular user)
    createUser: async (userData: Omit<User, 'id' | 'createdAt' | 'updatedAt' | 'roleName'>): Promise<User> => {
        try {
            const response = await api.post('/api/users', {
                ...userData,
                roleName: USER_ROLES.USER // Default role for new users
            });
            return response.data;
        } catch (error) {
            console.error('Error creating user:', error);
            throw error;
        }
    },

    // Create a staff member (admin only)
    createStaff: async (userData: Omit<User, 'id' | 'createdAt' | 'updatedAt'> & { roleName: 'ADMIN' | 'STAFF' }): Promise<User> => {
        try {
            const response = await api.post('/api/staff', userData);
            return response.data;
        } catch (error) {
            console.error('Error creating staff member:', error);
            throw error;
        }
    },

    // Update user (excluding password, chooses correct endpoint based on role)
    updateUser: async (id: string, userData: Partial<Omit<User, 'id' | 'createdAt' | 'updatedAt' | 'password'>>): Promise<User> => {
        try {
            // Determine if this is a staff member or regular user
            const users = await userBaseService.getAll();
            const existingUser = users.find(user => user.id === id);
            const isStaff = existingUser?.roleName === USER_ROLES.ADMIN || existingUser?.roleName === USER_ROLES.STAFF;
            
            const endpoint = isStaff ? `/api/staff/${id}` : `/api/users/${id}`;
            const response = await api.put(endpoint, userData);
            return response.data;
        } catch (error) {
            console.error(`Error updating user with id ${id}:`, error);
            throw error;
        }
    },

    // Delete a user (admin only, chooses correct endpoint based on role)
    deleteUser: async (id: string): Promise<void> => {
        try {
            // Determine if this is a staff member or regular user
            const users = await userBaseService.getAll();
            const existingUser = users.find(user => user.id === id);
            const isStaff = existingUser?.roleName === USER_ROLES.ADMIN || existingUser?.roleName === USER_ROLES.STAFF;
            
            const endpoint = isStaff ? `/api/staff/${id}` : `/api/users/${id}`;
            await api.delete(endpoint);
        } catch (error) {
            console.error(`Error deleting user with id ${id}:`, error);
            throw error;
        }
    },

    // Update password (separate endpoint)
    updatePassword: async (id: string, newPassword: string): Promise<void> => {
        try {
            await api.post(`/api/users/${id}/password`, { password: newPassword });
        } catch (error) {
            console.error(`Error updating password for user ${id}:`, error);
            throw error;
        }
    },

    // Search users by name, username, or email
    searchUsers: async (searchTerm: string): Promise<User[]> => {
        try {
            const response = await api.get(`/api/staff/users/search?term=${searchTerm}`);
            return response.data;
        } catch (error) {
            console.error(`Error searching users with term "${searchTerm}":`, error);
            // Perform client-side search on local data
            const users = await userBaseService.getAll();
            const term = searchTerm.toLowerCase();
            return users.filter(user => 
                user.name.toLowerCase().includes(term) ||
                user.userName.toLowerCase().includes(term) ||
                user.email.toLowerCase().includes(term)
            );
        }
    }
};

export default userService;
