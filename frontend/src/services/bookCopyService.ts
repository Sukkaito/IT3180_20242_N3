import { BaseService } from './baseService';
import bookCopies, { BookCopy } from '../data/bookCopies';
import api from '../api/axios';

// Create book copy service using the base service with fallback data
const bookCopyBaseService = new BaseService<BookCopy>('book-copy', bookCopies);

export const BookCopyService = {
  // Get all book copies
  getAll: () => bookCopyBaseService.getAll(),
  
  // Get book copies by original book id
  getByBookId: async (bookId: number): Promise<BookCopy[]> => {
    try {
      const response = await api.get(`/api/book-copy/book/${bookId}`);
      return response.data;
    } catch (error) {
      console.error(`Error fetching copies for book ${bookId}:`, error);
      // Filter from fallback data
      const copies = await bookCopyBaseService.getAll();
      return copies.filter(copy => copy.originalBookBookId === bookId);
    }
  },
  
  // Create a new book copy
  create: async (bookId: number): Promise<BookCopy> => {
    try {
      const response = await api.post('/api/book-copy', { originalBookBookId: bookId });
      return response.data;
    } catch (error) {
      console.error('Error creating book copy:', error);
      throw error;
    }
  },
  
  // Delete a book copy
  delete: async (id: number): Promise<void> => {
    return bookCopyBaseService.delete(id);
  }
};
