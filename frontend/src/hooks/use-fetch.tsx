import { useState, useEffect, useCallback } from 'react';

// Generic fetch function (private to this file)
const fetchData = async <T,> (
  endpoint: string,
  setData?: React.Dispatch<React.SetStateAction<T | null>>,
  setError?: React.Dispatch<React.SetStateAction<string | null>>,
  options?: { headers?: HeadersInit }
): Promise<T | null> => {
  try {
    const defaultHeaders = { 'Content-Type': 'application/json' };
    const headers = { ...defaultHeaders, ...options?.headers };

    const response = await fetch("http://localhost:8080" + endpoint, { headers });
    if (!response.ok) {
      throw new Error("HTTP error " + response.status);
    }

    const data: T = await response.json();
    if (setData) setData(data);
    if (setError) setError(null);
    return data;
  }
  catch (error) {
    if (setData) setData(null);
    if (setError) setError("Error fetching data: " + error);
    console.error("Error fetching data:", error);
    return null;
  }
};

// Hook for one-time data fetching
export const useFetchData = <T,>(endpoint: string) => {
  const [data, setData] = useState<T | null>(null);
  const [loading, setLoading] = useState<boolean>(true);
  const [error, setError] = useState<string | null>(null);

  useEffect(() => {
    setLoading(true);
    fetchData<T>(endpoint, setData, setError)
      .finally(() => setLoading(false));
  }, [endpoint]);

  return { data, loading, error };
};

// Hook for polling data at intervals
export const usePollingData = <T,>(
  endpoint: string, 
  interval: number = 60000,
  options? : { headers?: HeadersInit }
) => {
  const [data, setData] = useState<T | null>(null);
  const [loading, setLoading] = useState<boolean>(true);
  const [error, setError] = useState<string | null>(null);

  useEffect(() => {
    setLoading(true);
    
    // Initial fetch
    fetchData<T>(endpoint, setData, setError, options)
      .finally(() => setLoading(false));
    
    // Set up polling interval
    const intervalId = setInterval(() => {
      fetchData<T>(endpoint, setData, setError, options);
    }, interval);
    
    // Clean up
    return () => clearInterval(intervalId);
  }, [endpoint, interval, options]);

  return { data, loading, error };
};

// Manual fetch trigger hook
export const useManualFetch = <T,>(
  options?: { headers?: HeadersInit }
) => {
  const [data, setData] = useState<T | null>(null);
  const [loading, setLoading] = useState<boolean>(false);
  const [error, setError] = useState<string | null>(null);

  const execute = useCallback(async (endpoint: string) => {
    setLoading(true);
    const result = await fetchData<T>(endpoint, setData, setError, options);
    setLoading(false);
    return result;
  }, [options]);

  return { data, loading, error, execute };
};

// Default export
export default useFetchData;