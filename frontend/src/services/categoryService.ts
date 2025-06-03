import { BaseService } from './baseService';
import { Category } from '../data/categories';

// Create category service using the base service
const categoryBaseService = new BaseService<Category>('categories');

// Category service object that wraps all API functions
export const categoryService = {
    getAll: () => categoryBaseService.getAll(),
    create: (categoryData: Partial<Category>) => categoryBaseService.create(categoryData),
    update: (id: string, categoryData: Partial<Category>) => categoryBaseService.update(id, categoryData),
    delete: (id: string) => categoryBaseService.delete(id)
};
