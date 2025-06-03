import { useEffect } from 'react';
import { useLocation } from 'react-router-dom';
import graphiteService from '../services/graphiteService'; // Adjust the import path as needed

const UserRouteTracker: React.FC = () => {
    const location = useLocation();
    
    useEffect(() => {
        // Check if the current path starts with "/user"
        if (location.pathname.startsWith('/user')) {
            // Increment page view count
            graphiteService.incrementPageView();
        }
    }, [location.pathname]); // Re-run when the path changes
    
    // This component doesn't render anything
    return null;
};

export default UserRouteTracker;