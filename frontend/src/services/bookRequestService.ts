import { BaseService } from './baseService';
import bookRequests, { BookRequest, BookRequestStatusEnum } from '../data/bookRequests';
import api from '../api/axios';

// Create book request service using the base service with fallback data
const bookRequestBaseService = new BaseService<BookRequest>('requests', bookRequests);

export const BookRequestService = {
  // Get all requests
  getAll: () => bookRequestBaseService.getAll(),
  
  // Get requests by user
  getByUser: async (username: string): Promise<BookRequest[]> => {
    try {
      const response = await api.get(`/api/requests/user/${username}`);
      return response.data;
    } catch (error) {
      console.error(`Error fetching requests for user ${username}:`, error);
      // Filter from fallback data
      const requests = await bookRequestBaseService.getAll();
      return requests.filter(req => req.username.toLowerCase() === username.toLowerCase());
    }
  },
  
  // Get requests by status
  getByStatus: async (status: BookRequestStatusEnum): Promise<BookRequest[]> => {
    try {
      const response = await api.get(`/api/requests/status/${status}`);
      return response.data;
    } catch (error) {
      console.error(`Error fetching requests with status ${status}:`, error);
      // Filter from fallback data
      const requests = await bookRequestBaseService.getAll();
      return requests.filter(req => req.status === status);
    }
  },
  
  // Process a request (approve or reject)
  processRequest: async (requestId: string, approve: boolean): Promise<BookRequest> => {
    try {
      const response = await api.post(`/api/requests/process/${requestId}/${approve}`);
      return response.data;
    } catch (error) {
      console.error(`Error processing request ${requestId}:`, error);
      throw error;
    }
  },
  
  // Create a new request
  create: async (data: Omit<BookRequest, 'id' | 'status' | 'createdAt' | 'updatedAt'>): Promise<BookRequest> => {
    return bookRequestBaseService.create({
      ...data,
      status: BookRequestStatusEnum.PENDING,
    } as Partial<BookRequest>);
  }
};
