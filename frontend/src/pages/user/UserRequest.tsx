import { useState, useEffect } from 'react';
import UserNavbar from "../../components/UserNavbar";
import { BookRequest, BookRequestStatusEnum, BookRequestTypeEnum } from '../../data/bookRequests';
import { BookRequestService } from '../../services/bookRequestService';
import authService from '../../services/authService';

export default function UserRequest() {
  // State for requests data
  const [requests, setRequests] = useState<BookRequest[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);
  
  // Filter states
  const [filterType, setFilterType] = useState<string>('');
  const [filterStatus, setFilterStatus] = useState<string>('');

  // Add state for tracking cancellation
  const [cancellingId, setCancellingId] = useState<string | null>(null);

  // Load user's requests on component mount
  useEffect(() => {
    const loadRequests = async () => {
      try {
        setLoading(true);
        const userId = authService.getCurrentUserId();
        const userRequests = await BookRequestService.getByUser(userId);
        setRequests(userRequests);
        setError(null);
      } catch (err) {
        console.error('Error loading requests:', err);
        setError('Failed to load your requests');
      } finally {
        setLoading(false);
      }
    };
    
    loadRequests();
  }, []);

  // Filter requests based on type and status
  const filteredRequests = requests.filter(request => {
    const matchesType = filterType ? request.type === filterType : true;
    const matchesStatus = filterStatus ? request.status === filterStatus : true;
    return matchesType && matchesStatus;
  });

  // Get status display class
  const getStatusClass = (status: BookRequestStatusEnum) => {
    switch (status) {
      case BookRequestStatusEnum.ACCEPTED:
        return 'bg-green-100 text-green-800';
      case BookRequestStatusEnum.DENIED:
        return 'bg-red-100 text-red-800';
      case BookRequestStatusEnum.PENDING:
        return 'bg-yellow-100 text-yellow-800';
      default:
        return 'bg-gray-100 text-gray-800';
    }
  };

  // Get type display class
  const getTypeClass = (type: BookRequestTypeEnum) => {
    switch (type) {
      case BookRequestTypeEnum.BORROWING:
        return 'bg-blue-100 text-blue-800';
      case BookRequestTypeEnum.RETURNING:
        return 'bg-purple-100 text-purple-800';
      default:
        return 'bg-gray-100 text-gray-800';
    }
  };

  // Handle request cancellation
  const handleCancelRequest = async (requestId: string) => {
    if (window.confirm("Are you sure you want to cancel this request?")) {
      try {
        setCancellingId(requestId);
        await BookRequestService.cancelRequest(requestId);
        
        // Refresh the requests list after cancellation
        const username = authService.getCurrentUserId();
        const updatedRequests = await BookRequestService.getByUser(username);
        setRequests(updatedRequests);
        
      } catch (error) {
        console.error("Error cancelling request:", error);
        alert("Failed to cancel the request. Please try again.");
      } finally {
        setCancellingId(null);
      }
    }
  };

  // Show loading indicator
  if (loading) {
    return (
      <>
        <title>My Requests</title>
        <UserNavbar selected="request"/>
        <div className="min-h-screen bg-blue-50 p-6">
          <div className="text-center py-10">
            <p className="text-blue-600">Loading your requests...</p>
          </div>
        </div>
      </>
    );
  }

  // Show error message
  if (error) {
    return (
      <>
        <title>My Requests</title>
        <UserNavbar selected="request"/>
        <div className="min-h-screen bg-blue-50 p-6">
          <div className="text-center py-10">
            <p className="text-red-600">{error}</p>
            <button 
              className="mt-4 bg-blue-600 text-white py-2 px-4 rounded hover:bg-blue-700"
              onClick={() => window.location.reload()}
            >
              Retry
            </button>
          </div>
        </div>
      </>
    );
  }

  return (
    <>
      <title>My Requests</title>
      <UserNavbar selected="request"/>
      
      <div className="min-h-screen bg-blue-50 p-6">
        <h2 className="text-2xl font-semibold text-blue-700 mb-4">My Requests</h2>
        <p className="text-gray-700 mb-6">
          View and track the status of your book borrowing and returning requests.
        </p>

        {/* Filter controls */}
        <div className="bg-white rounded-lg shadow-md p-4 mb-6">
          <div className="flex flex-col sm:flex-row items-center justify-between">
            <div className="flex flex-col sm:flex-row items-center space-y-3 sm:space-y-0 sm:space-x-4 mb-3 sm:mb-0">
              <div>
                <span className="font-medium text-gray-700 mr-2">Type:</span>
                <select 
                  className="border border-gray-300 rounded px-3 py-1"
                  value={filterType}
                  onChange={(e) => setFilterType(e.target.value)}
                >
                  <option value="">All</option>
                  <option value={BookRequestTypeEnum.BORROWING}>Borrowing</option>
                  <option value={BookRequestTypeEnum.RETURNING}>Returning</option>
                </select>
              </div>
              <div>
                <span className="font-medium text-gray-700 mr-2">Status:</span>
                <select 
                  className="border border-gray-300 rounded px-3 py-1"
                  value={filterStatus}
                  onChange={(e) => setFilterStatus(e.target.value)}
                >
                  <option value="">All</option>
                  <option value={BookRequestStatusEnum.ACCEPTED}>Accepted</option>
                  <option value={BookRequestStatusEnum.DENIED}>Denied</option>
                  <option value={BookRequestStatusEnum.PENDING}>Pending</option>
                </select>
              </div>
            </div>
            <div className="text-sm text-gray-600">
              Showing {filteredRequests.length} of {requests.length} requests
            </div>
          </div>
        </div>

        {/* Requests list */}
        {filteredRequests.length === 0 ? (
          <div className="bg-white rounded-lg shadow-md p-10 text-center">
            <p className="text-gray-500">
              You have no requests{filterType || filterStatus ? ' matching the selected filters' : ''}.
            </p>
          </div>
        ) : (
          <div className="space-y-4">
            {filteredRequests.map(request => (
              <div key={request.id} className="bg-white rounded-lg shadow-md p-6">
                <div className="flex flex-col md:flex-row justify-between items-start">
                  <div>
                    <div className="flex flex-col sm:flex-row sm:items-center sm:space-x-4 mb-4">
                      <span className={`px-3 py-1 rounded-full text-xs font-medium ${getTypeClass(request.type as BookRequestTypeEnum)}`}>
                        {request.type}
                      </span>
                      <span className={`px-3 py-1 rounded-full text-xs font-medium ${getStatusClass(request.status as BookRequestStatusEnum)}`}>
                        {request.status}
                      </span>
                    </div>
                    <p className="text-sm">
                      <span className="font-medium">Book Loan ID:</span> {request.bookLoanId}
                    </p>
                    {request.bookName && (
                      <p className="text-sm">
                        <span className="font-medium">Book:</span> {request.bookName}
                      </p>
                    )}
                    <p className="text-sm">
                      <span className="font-medium">Requested:</span> {new Date(request.createdAt).toLocaleString()}
                    </p>
                    
                    {request.updatedAt !== request.createdAt && (
                      <p className="text-sm">
                        <span className="font-medium">Last Updated:</span> {new Date(request.updatedAt).toLocaleString()}
                      </p>
                    )}
                  </div>
                  <div className="mt-4 md:mt-0">
                    {/* Add cancel button for pending requests */}
                    {request.status === BookRequestStatusEnum.PENDING && (
                      <button
                        className="bg-red-500 hover:bg-red-600 text-white px-4 py-2 rounded shadow disabled:bg-red-300"
                        onClick={() => handleCancelRequest(request.id)}
                        disabled={cancellingId === request.id}
                      >
                        {cancellingId === request.id ? 'Cancelling...' : 'Cancel Request'}
                      </button>
                    )}
                    
                    {/* Status guidance messages */}
                    {request.status === BookRequestStatusEnum.PENDING && (
                      <div className="px-4 py-2 bg-yellow-50 border border-yellow-200 rounded text-sm text-yellow-800 mt-2">
                        This request is pending approval from the library staff.
                      </div>
                    )}
                    {request.status === BookRequestStatusEnum.ACCEPTED && (
                      <div className="px-4 py-2 bg-green-50 border border-green-200 rounded text-sm text-green-800">
                        {request.type === BookRequestTypeEnum.BORROWING 
                          ? 'Your borrowing request has been approved. You can pick up the book at the library.'
                          : 'Your return request has been approved. Please return the book to the library.'}
                      </div>
                    )}
                    {request.status === BookRequestStatusEnum.DENIED && (
                      <div className="px-4 py-2 bg-red-50 border border-red-200 rounded text-sm text-red-800">
                        Your request has been denied. Please contact the library for more information.
                      </div>
                    )}
                  </div>
                </div>
              </div>
            ))}
          </div>
        )}
      </div>
    </>
  );
}
