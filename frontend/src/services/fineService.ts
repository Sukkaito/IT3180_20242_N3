import { BaseService } from './baseService';
import finesData, { Fine } from '../data/fines';
import api from '../api/axios';

// Create fine service using the base service with fallback data
const fineBaseService = new BaseService<Fine>('fines', finesData);

export const FineService = {
  // Get all fines
  getAll: () => fineBaseService.getAll(),
  
  // Get a fine by ID
  getById: async (id: string): Promise<Fine | undefined> => {
    try {
      const response = await api.get(`/api/fines/${id}`);
      return response.data;
    } catch (error) {
      console.error(`Error fetching fine with id ${id}:`, error);
      // Return from fallback data
      const fines = await fineBaseService.getAll();
      return fines.find(fine => fine.id === id);
    }
  },
  
  // Get fines by user
  getByUser: async (userName: string): Promise<Fine[]> => {
    try {
      const response = await api.get(`/api/fines/user/${userName}`);
      return response.data;
    } catch (error) {
      console.error(`Error fetching fines for user ${userName}:`, error);
      // Filter from fallback data
      const fines = await fineBaseService.getAll();
      return fines.filter(fine => fine.username.toLowerCase() === userName.toLowerCase());
    }
  },
  
  // Get fines by book loan
  getByBookLoan: async (bookLoanId: string): Promise<Fine[]> => {
    try {
      const response = await api.get(`/api/fines/loan/${bookLoanId}`);
      return response.data;
    } catch (error) {
      console.error(`Error fetching fines for book loan ${bookLoanId}:`, error);
      // Filter from fallback data
      const fines = await fineBaseService.getAll();
      return fines.filter(fine => fine.bookLoanId === bookLoanId); // Using bookLoanId consistently
    }
  },
  
  // Create a new fine
  create: async (data: Omit<Fine, 'id' | 'createdAt' | 'updatedAt'>): Promise<Fine> => {
    return fineBaseService.create(data as Partial<Fine>);
  },
  
  // Update a fine
  update: async (id: string, data: Partial<Fine>): Promise<Fine> => {
    return fineBaseService.update(id, data);
  },
  
  // Delete a fine
  delete: async (id: string): Promise<void> => {
    return fineBaseService.delete(id);
  }
};
