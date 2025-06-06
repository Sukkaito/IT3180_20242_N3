import axios, { AxiosError, AxiosResponse } from 'axios';

// Define types for error response
interface ErrorResponse {
  status: number;
  message: string;
  data?: any;
}

const api = axios.create({
  baseURL: import.meta.env.VITE_API_URL,
  timeout: 5000,
});

// Response interceptor for handling responses and errors
// api.interceptors.response.use(
//   (response: AxiosResponse) => {
//     return response;
//   }
//   ,
//   (error: AxiosError) => {
//     const { response } = error;
//     const errorResponse: ErrorResponse = {
//       status: response?.status || 500,
//       message: 'An unexpected error occurred',
//       data: response?.data,
//     };

//     // Extract error message from backend response if available
//     if (response?.data) {
//       // Adjust these properties based on your backend error response structure
//       // Common patterns include: response.data.message, response.data.error, etc.
//       if (typeof response.data === 'string') {
//         errorResponse.message = response.data;
//       } else if (response.data.message) {
//         errorResponse.message = response.data.message;
//       } else if (response.data.error) {
//         errorResponse.message = response.data.error;
//       } else if (response.data.errors && Array.isArray(response.data.errors)) {
//         // Handle validation errors that might be in an array
//         errorResponse.message = response.data.errors[0] || 'Validation error';
//       }
//     } else if (!navigator.onLine) {
//       errorResponse.message = 'No internet connection';
//     }

//     // Fallback messages based on status code if no specific message was found
//     if (errorResponse.message === 'An unexpected error occurred') {
//       switch (errorResponse.status) {
//         case 400:
//           errorResponse.message = 'Bad request';
//           break;
//         case 401:
//           errorResponse.message = 'Unauthorized - Please log in again';
//           break;
//         case 403:
//           errorResponse.message = 'Forbidden - You do not have permission';
//           break;
//         case 404:
//           errorResponse.message = 'Resource not found';
//           break;
//         case 422:
//           errorResponse.message = 'Validation error';
//           break;
//         case 500:
//           errorResponse.message = 'Server error - Please try again later';
//           break;
//         case 503:
//           errorResponse.message = 'Service unavailable - Please try again later';
//           break;
//       }
//     }

//     console.error(`API Error: ${errorResponse.status} - ${errorResponse.message}`);
    
//     // Return rejected promise with standardized error format
//     return Promise.reject(errorResponse);
//   }
// );

// Request interceptor for adding auth headers and credentials
api.interceptors.request.use(
  (config) => {
    // Add withCredentials to every request to ensure cookies are sent
    config.withCredentials = true;
    const authToken = sessionStorage.getItem('AUTHORIZATION');
    if (authToken) {
      config.headers.Authorization = `Basic ${authToken}`;
      console.log('Authorization header set:', config.headers.Authorization);
    }
    
    console.log('Request configured with withCredentials: true');
    return config;
  },
  (error) => {
    console.error('Request error:', error);
    console.log('Request error details:', {
      message: error.message 
    });
    return Promise.reject(error);
  }
);

export default api;

// Helper function to handle API errors in components
export const handleApiError = (error: any): { error: boolean; message: string } => {
  if (error && typeof error === 'object' && 'message' in error) {
    return { error: true, message: error.message };
  }
  return { error: true, message: 'An unexpected error occurred' };
};