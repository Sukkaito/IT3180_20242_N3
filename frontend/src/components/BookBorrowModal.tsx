import { useState, useEffect } from "react";
import { Book } from "../data/books";
import { BookCopy } from "../data/bookCopies";
import { BookCopyService } from "../services/bookCopyService";
import { BookRequestService } from "../services/bookRequestService";
import { SubscriptionService } from "../services/subscriptionService";
import authService from "../services/authService";

interface BookBorrowModalProps {
  isOpen: boolean;
  onClose: () => void;
  book: Book | null;
  onSuccess: () => void;
}

export default function BookBorrowModal({ 
  isOpen, 
  onClose, 
  book, 
  onSuccess 
}: BookBorrowModalProps) {
  const [copies, setCopies] = useState<BookCopy[]>([]);
  const [selectedCopyId, setSelectedCopyId] = useState<number | null>(null);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState<string | null>(null);
  const [success, setSuccess] = useState<string | null>(null);
  const [subscribing, setSubscribing] = useState(false);

  // Load available copies when book changes
  useEffect(() => {
    const loadCopies = async () => {
        if (!book) return;
        
        try {
        setLoading(true);
        setError(null);
        const bookCopies = await BookCopyService.getByBookId(book.id);
        // Filter only available copies
        const availableCopies = bookCopies.filter(copy => copy.status === "AVAILABLE");
        setCopies(availableCopies);
        
        // Auto-select the first copy if available
        if (availableCopies.length > 0) {
            setSelectedCopyId(availableCopies[0].id);
        }
        } catch (err) {
        console.error("Error loading book copies:", err);
        setError("Failed to load available copies. Please try again.");
        } finally {
        setLoading(false);
        }
    };

    if (isOpen && book) {
      loadCopies();
    } else {
      // Reset state when modal closes
      setCopies([]);
      setSelectedCopyId(null);
      setError(null);
      setSuccess(null);
    }
  }, [isOpen, book]);

  // Handle borrow specific copy
  const handleBorrowSpecificCopy = async () => {
    if (!book || !selectedCopyId) {
      setError("Please select a copy to borrow");
      return;
    }

    try {
      setLoading(true);
      setError(null);
      const userId = authService.getCurrentUserId();
      const response = await BookRequestService.createBorrowRequest(userId, selectedCopyId);
      setSuccess("Borrow request submitted successfully!");
      
      // After a short delay, close the modal and refresh the parent component
      setTimeout(() => {
        onSuccess();
        onClose();
      }, 1500);
    } catch (err: any) {
      console.error("Error creating borrow request:", err);
      
      // Extract error message from response if available
      let errorMessage = "Failed to create borrow request.";
      
      if (err.response) {
        // The request was made and the server responded with a status code
        // that falls out of the range of 2xx
        if (err.response.data && err.response.data.message) {
          errorMessage = err.response.data.message;
        } else if (err.response.data) {
          // If data exists but no message property
          errorMessage = typeof err.response.data === 'string' 
            ? err.response.data 
            : JSON.stringify(err.response.data);
        } else {
          errorMessage = `Server error: ${err.response.status}`;
        }
      } else if (err.message) {
        // The request was made but no response was received
        errorMessage = err.message;
      }
      
      setError(errorMessage);
    } finally {
      setLoading(false);
    }
  };

  // Handle borrow random copy
  const handleBorrowRandomCopy = async () => {
    if (!book) {
      setError("Book information is missing");
      return;
    }

    try {
      setLoading(true);
      setError(null);
      const userId = authService.getCurrentUserId();
      await BookRequestService.createRandomBorrowRequest(userId, book.id);
      setSuccess("Random copy borrow request submitted successfully!");
      
      // After a short delay, close the modal and refresh the parent component
      setTimeout(() => {
        onSuccess();
        onClose();
      }, 1500);
    } catch (err: any) {
      console.error("Error creating random borrow request:", err);
      
      // Extract error message from response if available
      let errorMessage = "Failed to create random borrow request.";
      
      if (err.response) {
        // The request was made and the server responded with a status code
        // that falls out of the range of 2xx
        if (err.response.data && err.response.data.message) {
          errorMessage = err.response.data.message;
        } else if (err.response.data) {
          // If data exists but no message property
          errorMessage = typeof err.response.data === 'string' 
            ? err.response.data 
            : JSON.stringify(err.response.data);
        } else {
          errorMessage = `Server error: ${err.response.status}`;
        }
      } else if (err.message) {
        // The request was made but no response was received
        errorMessage = err.message;
      }
      
      setError(errorMessage);
    } finally {
      setLoading(false);
    }
  };

  // Handle subscribe to book
  const handleSubscribeToBook = async () => {
    if (!book) {
      setError("Book information is missing");
      return;
    }

    try {
      setLoading(true);
      setSubscribing(true);
      setError(null);
      const userId = authService.getCurrentUserId();
      
      // Subscribe to the book itself, which means subscribing to all its copies
      await SubscriptionService.subscribeToBook(userId, book.id);
      setSuccess("You have subscribed to this book. You will be notified when any copy becomes available.");
      
      // After a short delay, close the modal and refresh the parent component
      setTimeout(() => {
        onSuccess();
        onClose();
      }, 1500);
    } catch (err: any) {
      console.error("Error subscribing to book:", err);
      
      // Extract error message from response if available
      let errorMessage = "Failed to subscribe to book.";
      
      if (err.response) {
        // The request was made and the server responded with a status code
        // that falls out of the range of 2xx
        if (err.response.data && err.response.data.message) {
          errorMessage = err.response.data.message;
        } else if (err.response.data) {
          // If data exists but no message property
          errorMessage = typeof err.response.data === 'string' 
            ? err.response.data 
            : JSON.stringify(err.response.data);
        } else {
          errorMessage = `Server error: ${err.response.status}`;
        }
      } else if (err.message) {
        // The request was made but no response was received
        errorMessage = err.message;
      }
      
      setError(errorMessage);
    } finally {
      setLoading(false);
      setSubscribing(false);
    }
  };

  if (!isOpen) return null;

  return (
    <div className="fixed inset-0 bg-black bg-opacity-50 flex justify-center items-center z-50">
      <div className="bg-white rounded-lg shadow-lg p-6 w-full max-w-md">
        <h3 className="text-xl font-semibold text-blue-700 mb-4">
          Borrow Book: {book?.title}
        </h3>

        {/* Status messages */}
        {error && (
          <div className="mb-4 p-3 bg-red-100 text-red-700 rounded">
            {error}
          </div>
        )}
        
        {success && (
          <div className="mb-4 p-3 bg-green-100 text-green-700 rounded">
            {success}
          </div>
        )}

        {loading ? (
          <div className="text-center py-4">
            <p className="text-blue-600">Loading...</p>
          </div>
        ) : (
          <>
            {/* Display the available copies */}
            {copies.length > 0 ? (
              <div className="mb-4">
                <label className="block text-sm font-medium text-gray-700 mb-2">
                  Select a copy to borrow:
                </label>
                <select
                  className="w-full border border-gray-300 rounded px-3 py-2"
                  value={selectedCopyId || ""}
                  onChange={(e) => setSelectedCopyId(Number(e.target.value))}
                >
                  {copies.map((copy) => (
                    <option key={copy.id} value={copy.id}>
                      Copy ID: {copy.id} - {copy.status}
                    </option>
                  ))}
                </select>
              </div>
            ) : (
              <div className="mb-4 p-3 bg-yellow-100 text-yellow-700 rounded">
                <p>No available copies found for this book.</p>
                <button
                  className="mt-2 px-4 py-2 bg-yellow-500 text-white rounded hover:bg-yellow-600 w-full"
                  onClick={handleSubscribeToBook}
                  disabled={subscribing}
                >
                  {subscribing ? "Subscribing..." : "Subscribe to be notified when any copy becomes available"}
                </button>
              </div>
            )}

            {/* Action buttons */}
            <div className="flex justify-between space-x-3 pt-4">
              <button
                type="button"
                className="px-4 py-2 bg-gray-300 rounded hover:bg-gray-400"
                onClick={onClose}
                disabled={loading}
              >
                Cancel
              </button>
              
              <div className="space-x-2">
                {copies.length > 0 && (
                  <button
                    type="button"
                    className="px-4 py-2 bg-blue-600 text-white rounded hover:bg-blue-700"
                    onClick={handleBorrowSpecificCopy}
                    disabled={loading || !selectedCopyId}
                  >
                    Borrow Selected Copy
                  </button>
                )}
                
                <button
                  type="button"
                  className="px-4 py-2 bg-green-600 text-white rounded hover:bg-green-700"
                  onClick={handleBorrowRandomCopy}
                  disabled={loading || copies.length === 0}
                >
                  Borrow Random Copy
                </button>
              </div>
            </div>
          </>
        )}
      </div>
    </div>
  );
}
