import { Navigate, Outlet, useLocation } from 'react-router-dom';
import { useAuth } from '../hooks/useAuth';
import { useEffect, useState } from 'react';

interface ProtectedRouteProps {
  redirectPath?: string;
  children?: React.ReactNode;
}

const ProtectedRoute = ({ 
  redirectPath = '/login', 
  children 
}: ProtectedRouteProps) => {
  const { isAuthenticated, isLoading, checkAuthStatus } = useAuth();
  const location = useLocation();
  const [showLoader, setShowLoader] = useState(false);

  useEffect(() => {
    // Force a re-check of authentication status when component mounts
    checkAuthStatus();
    
    // Only show the loader if the check takes more than 300ms
    // This prevents flickering for quick auth checks
    const timer = setTimeout(() => {
      if (isLoading) {
        setShowLoader(true);
      }
    }, 300);
    
    return () => clearTimeout(timer);
  }, [checkAuthStatus, isLoading]);

  // Only show loading state if it's taking some time
  if (isLoading && showLoader) {
    return (
      <div className="flex justify-center items-center h-screen">
        <div className="animate-spin rounded-full h-12 w-12 border-t-2 border-b-2 border-blue-500"></div>
      </div>
    );
  }

  console.log('Protected route check:', { isAuthenticated, path: location.pathname });

  // If not authenticated, redirect to login with the return URL
  if (!isAuthenticated) {
    return <Navigate 
      to={redirectPath} 
      state={{ from: location.pathname }} 
      replace 
    />;
  }

  // If authenticated, render the protected content
  return children ? <>{children}</> : <Outlet />;
};

export default ProtectedRoute;