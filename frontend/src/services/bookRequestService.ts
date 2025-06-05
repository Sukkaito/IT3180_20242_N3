import { BaseService } from './baseService';
import bookRequests, { BookRequest, BookRequestStatusEnum } from '../data/bookRequests';
import api from '../api/axios';

// Create book request service using the base service with fallback data
const bookRequestBaseService = new BaseService<BookRequest>('requests', bookRequests);

export const BookRequestService = {
  // Get all requests
  getAll: () => bookRequestBaseService.getAll(),
  
  // Get requests by user
  getByUser: async (userId: string): Promise<BookRequest[]> => {
    try {
      const response = await api.get(`/api/requests/user/${userId}`);
      return response.data;
    } catch (error) {
      console.error(`Error fetching requests for user ${userId}:`, error);
      // Filter from fallback data
      const requests = await bookRequestBaseService.getAll();
      return requests.filter(req => req.username.toLowerCase() === userId.toLowerCase());
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
  create: async (userId: string, bookCopyId: number): Promise<BookRequest> => {
    try {
      const response = await api.post(`/api/requests/${userId}/new/return?bookCopyId=${bookCopyId}`);
      return response.data;
    } catch (error) {
      console.error(`Error creating request for user ${userId}:`, error);
      throw error;
    }
  },

  // Cancel a request
  cancelRequest: async (requestId: string): Promise<void> => {
    try {
      await api.put(`/api/requests/cancel?requestId=${requestId}`);
    } catch (error) {
      console.error(`Error cancelling request ${requestId}:`, error);
      throw error;
    }
  },
};
