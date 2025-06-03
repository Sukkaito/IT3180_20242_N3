import api from '../api/axios';

// Interface for metrics data
export interface MetricsData {
  books: number;
  categories: number;
  authors: number;
  publishers: number;
  users: number;
  loans: number;
  requests: number;
}

// Default fallback metrics
const defaultMetrics: MetricsData = {
  books: 1240,
  categories: 45,
  authors: 78,
  publishers: 28,
  users: 550,
  loans: 3570,
  requests: 125
};

export const DashboardService = {
  // Get count metrics for dashboard
  getMetrics: async (): Promise<MetricsData> => {
    try {
      // Create an object to store all metrics
      const metrics: Partial<MetricsData> = {};
      
      // Array of metric types to fetch
      const metricTypes = ['books', 'categories', 'authors', 'publishers', 'users', 'loans', 'requests'];
      
      // Fetch metrics in parallel
      const results = await Promise.allSettled(
        metricTypes.map(type => 
          api.get(`/api/metrics/count/${type}`)
            .then(response => ({ type, count: response.data }))
        )
      );
      
      // Process results
      results.forEach(result => {
        if (result.status === 'fulfilled') {
          const { type, count } = result.value;
          metrics[type as keyof MetricsData] = count;
        }
      });
      
      // Fill in any missing metrics with defaults
      return {
        books: metrics.books ?? defaultMetrics.books,
        categories: metrics.categories ?? defaultMetrics.categories,
        authors: metrics.authors ?? defaultMetrics.authors,
        publishers: metrics.publishers ?? defaultMetrics.publishers,
        users: metrics.users ?? defaultMetrics.users,
        loans: metrics.loans ?? defaultMetrics.loans,
        requests: metrics.requests ?? defaultMetrics.requests
      };
    } catch (error) {
      console.error('Error fetching metrics:', error);
      // Return default metrics if API fails
      return defaultMetrics;
    }
  },
  
//   // Get trending titles
//   getTrendingTitles: async () => {
//     try {
//       const response = await api.get('/api/metrics/trending/titles');
//       return response.data;
//     } catch (error) {
//       console.error('Error fetching trending titles:', error);
//       // Return mock data as fallback
//       return Array(5).fill(0).map((_, i) => ({
//         id: i + 1,
//         title: `Title ${i + 1}`,
//         views: 1000 - i * 100
//       }));
//     }
//   },
  
//   // Get trending categories
//   getTrendingCategories: async () => {
//     try {
//       const response = await api.get('/api/metrics/trending/categories');
//       return response.data;
//     } catch (error) {
//       console.error('Error fetching trending categories:', error);
//       // Return mock data as fallback
//       return Array(5).fill(0).map((_, i) => ({
//         id: i + 1,
//         name: `Category ${i + 1}`,
//         views: 500 - i * 50
//       }));
//     }
//   }
};
