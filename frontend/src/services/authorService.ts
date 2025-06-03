import { BaseService } from './baseService';
import authors, { Author } from '../data/authors.ts';

// Create author service using the base service with fallback data
const authorBaseService = new BaseService<Author>('authors', authors);

export const AuthorService = {
  getAll: () => authorBaseService.getAll(),
  
  add: async (name: string): Promise<Author> => {
    return authorBaseService.create({ name });
  },
  
  update: async (id: number, name: string): Promise<Author> => {
    return authorBaseService.update(id, { name });
  },
  
  delete: (id: number) => authorBaseService.delete(id)
};