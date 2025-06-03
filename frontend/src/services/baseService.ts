import api from '../api/axios';

// Constant for localStorage key prefix to ensure consistency
export const STORAGE_KEY_PREFIX = "library_";

export interface BaseEntity {
  id: string | number;
}

export class BaseService<T extends BaseEntity> {
  private apiEndpoint: string;
  private fallbackData?: T[];
  private localStorageKey: string;

  constructor(apiEndpoint: string, fallbackData?: T[]) {
    this.apiEndpoint = apiEndpoint;
    this.fallbackData = fallbackData;
    // Use standardized key format: "library_[endpoint]"
    this.localStorageKey = `${STORAGE_KEY_PREFIX}${this.apiEndpoint}`;
  }

  // Save data to localStorage
  private saveToLocalStorage(data: T[]): void {
    try {
      localStorage.setItem(this.localStorageKey, JSON.stringify(data));
    } catch (error) {
      console.error(`Error storing ${this.apiEndpoint} in localStorage:`, error);
    }
  }

  // Get data from localStorage
  private getFromLocalStorage(): T[] | null {
    try {
      const data = localStorage.getItem(this.localStorageKey);
      return data ? JSON.parse(data) : null;
    } catch (error) {
      console.error(`Error retrieving ${this.apiEndpoint} from localStorage:`, error);
      return null;
    }
  }

  async getAll(): Promise<T[]> {
    try {
      // Try to fetch from API first
      const response = await api.get(`/api/${this.apiEndpoint}`);
      const data = response.data;
      
      // Store successful response to localStorage
      this.saveToLocalStorage(data);
      
      return data;
    } catch (error) {
      console.error(`Error fetching ${this.apiEndpoint}:`, error);
      
      // Try to get from localStorage if API fails
      const cachedData = this.getFromLocalStorage();
      if (cachedData && cachedData.length > 0) {
        console.log(`Retrieved ${this.apiEndpoint} from localStorage (key: ${this.localStorageKey})`);
        return cachedData;
      }
      
      // Use fallback data if provided and nothing useful in localStorage
      if (this.fallbackData && this.fallbackData.length > 0) {
        console.log(`Using fallback data for ${this.apiEndpoint}`);
        // Store fallback data to localStorage for future use
        this.saveToLocalStorage(this.fallbackData);
        return this.fallbackData;
      }
      
      // If everything fails, return empty array instead of throwing
      console.log(`No data available for ${this.apiEndpoint}, returning empty array`);
      return [];
    }
  }

  async create(data: Partial<T>): Promise<T> {
    try {
      const response = await api.post(`/api/${this.apiEndpoint}`, data);
      const newItem = response.data;
      
      // Update localStorage with new item
      const existingData = this.getFromLocalStorage() || [];
      this.saveToLocalStorage([...existingData, newItem]);
      
      return newItem;
    } catch (error) {
      console.error(`Error creating ${this.apiEndpoint}:`, error);
      throw error;
    }
  }

  async update(id: string | number, data: Partial<T>): Promise<T> {
    try {
      const response = await api.put(`/api/${this.apiEndpoint}/update/${id}`, data);
      const updatedItem = response.data;
      
      // Update the item in localStorage
      const existingData = this.getFromLocalStorage() || [];
      const updatedData = existingData.map(item => 
        item.id === id ? { ...item, ...updatedItem } : item
      );
      this.saveToLocalStorage(updatedData);
      
      return updatedItem;
    } catch (error) {
      console.error(`Error updating ${this.apiEndpoint} with id ${id}:`, error);
      throw error;
    }
  }

  async delete(id: string | number): Promise<void> {
    try {
      await api.delete(`/api/${this.apiEndpoint}/delete/${id}`);
      
      // Remove the item from localStorage
      const existingData = this.getFromLocalStorage() || [];
      const filteredData = existingData.filter(item => item.id !== id);
      this.saveToLocalStorage(filteredData);
    } catch (error) {
      console.error(`Error deleting ${this.apiEndpoint} with id ${id}:`, error);
      throw error;
    }
  }

  // Clear localStorage for this entity
  clearCache(): void {
    try {
      localStorage.removeItem(this.localStorageKey);
      console.log(`Cleared ${this.apiEndpoint} cache from localStorage`);
    } catch (error) {
      console.error(`Error clearing ${this.apiEndpoint} cache:`, error);
    }
  }
}
