import api from '../api/axios';
import booksData, { Book } from '../data/books';

// Custom Book service class that extends BaseEntity but doesn't use localStorage
class BookService {
  private fallbackData: Book[];

  constructor(fallbackData: Book[]) {
    this.fallbackData = fallbackData;
  }

  async getAll(): Promise<Book[]> {
    try {
      const response = await api.get('/api/books');
      return response.data;
    } catch (error) {
      console.error('Error fetching books:', error);
      // Return fallback data if API fails
      if (this.fallbackData && this.fallbackData.length > 0) {
        console.log('Using fallback data for books');
        return this.fallbackData;
      }
      // If no fallback data, return empty array
      return [];
    }
  }

  async getById(id: number): Promise<Book | undefined> {
    try {
      const response = await api.get(`/api/books/${id}`);
      return response.data;
    } catch (error) {
      console.error(`Error fetching book with id ${id}:`, error);
      // Return the book from fallback data
      return this.fallbackData.find(book => book.id === id);
    }
  }

  async create(bookData: Omit<Book, 'id'>): Promise<Book> {
    try {
      const response = await api.post('/api/books', bookData);
      return response.data;
    } catch (error) {
      console.error('Error creating book:', error);
      throw error;
    }
  }

  async update(id: number, bookData: Partial<Book>): Promise<Book> {
    try {
      const response = await api.put(`/api/books/update/${id}`, bookData);
      return response.data;
    } catch (error) {
      console.error(`Error updating book with id ${id}:`, error);
      throw error;
    }
  }

  async delete(id: number): Promise<void> {
    try {
      await api.delete(`/api/books/delete/${id}`);
    } catch (error) {
      console.error(`Error deleting book with id ${id}:`, error);
      throw error;
    }
  }

  // Search books by title
  async search(title: string): Promise<Book[]> {
    try {
      const response = await api.get(`/api/books/search/title/${title}`);
      return response.data;
    } catch (error) {
      console.error(`Error searching books with term "${title}":`, error);
      // Perform client-side search on fallback data
      const term = title.toLowerCase();
      return this.fallbackData.filter(book => 
        book.title.toLowerCase().includes(term) || 
        book.description.toLowerCase().includes(term)
      );
    }
  }

  // Get books by author id
  async getByAuthor(authorId: number): Promise<Book[]> {
    try {
      const response = await api.get(`/api/books/search/author/${authorId}`);
      return response.data;
    } catch (error) {
      console.error(`Error fetching books by author ${authorId}:`, error);
      // Filter fallback data
      return this.fallbackData.filter(book => 
        book.authorIds.includes(authorId)
      );
    }
  }

  // Get books by category id
  async getByCategory(categoryId: number): Promise<Book[]> {
    try {
      const response = await api.get(`/api/books/search/category/${categoryId}`);
      return response.data;
    } catch (error) {
      console.error(`Error fetching books by category ${categoryId}:`, error);
      // Filter fallback data
      return this.fallbackData.filter(book => 
        book.categoryIds.includes(categoryId)
      );
    }
  }

  // Get books by publisher id
  async getByPublisher(publisherId: number): Promise<Book[]> {
    try {
      const response = await api.get(`/api/books/search/publisher/${publisherId}`);
      return response.data;
    } catch (error) {
      console.error(`Error fetching books by publisher ${publisherId}:`, error);
      // Filter fallback data
      return this.fallbackData.filter(book => 
        book.publisherId === publisherId
      );
    }
  }
}

// Create and export book service instance
const bookService = new BookService(booksData);
export default bookService;
