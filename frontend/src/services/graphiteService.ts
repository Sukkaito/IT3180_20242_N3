// graphiteService.ts

/**
 * Service for tracking page views using Graphite metrics
 */
export class GraphiteService {
    private static instance: GraphiteService;
    private counter: number = 0;
    private storageKey: string = 'graphite_page_views';

    private constructor() {
        // Load existing counter from localStorage if available
        const savedCount = localStorage.getItem(this.storageKey);
        if (savedCount) {
            this.counter = parseInt(savedCount, 10);
        }
    }

    /**
     * Get the singleton instance of GraphiteService
     */
    public static getInstance(): GraphiteService {
        if (!GraphiteService.instance) {
            GraphiteService.instance = new GraphiteService();
        }
        return GraphiteService.instance;
    }

    /**
     * Increment the page view counter by 1
     */
    public incrementPageView(): void {
        this.counter += 1;
        this.saveCounter();
        this.sendMetricToServer();
    }

    /**
     * Get the current page view count
     */
    public getPageViewCount(): number {
        return this.counter;
    }

    /**
     * Save counter to localStorage
     */
    private saveCounter(): void {
        localStorage.setItem(this.storageKey, this.counter.toString());
    }

    /**
     * Send metrics to server (implement actual API call here)
     */
    private sendMetricToServer(): void {
        try {
            fetch('http://localhost:8080/api/metrics/count/visit', {
            method: 'POST'
            }).catch(err => console.warn('Failed to send UDP metrics:', err));
        } catch (error) {
            console.error('Error sending metrics:', error);
        }
        
        console.log(`Page view metric sent to server: ${this.counter}`);
    }
}

// Export a default instance for easy import
const graphiteService = GraphiteService.getInstance();
export default graphiteService;