import { BaseService } from './baseService';
import bookLoans, { BookLoan } from '../data/bookLoans';
import users from '../data/users';
import api from '../api/axios';
import { FineService } from './fineService';
import { Fine } from '../data/fines';

// Create book loan service using the base service with fallback data
const bookLoanBaseService = new BaseService<BookLoan>('loaned', bookLoans);

export const BookLoanService = {
  // Get all loans
  getAll: () => bookLoanBaseService.getAll(),
  
  // Get loans by user
  getByUser: async (userId: string): Promise<BookLoan[]> => {
    try {
      const response = await api.get(`/api/loaned/${userId}`);
      return response.data;
    } catch (error) {
      console.error(`Error fetching loans for user ${userId}:`, error);
      // Filter from local data as fallback
      const loans = await bookLoanBaseService.getAll();
      const user = users.find(u => u.id === userId);
      if (user) {
        return loans.filter(loan => loan.userUserName === user.userName);
      }
      return [];
    }
  },
  
  // Add a fine to a loan
  addFine: (data: Omit<Fine, 'id' | 'createdAt' | 'updatedAt'>) => FineService.create(data)
};