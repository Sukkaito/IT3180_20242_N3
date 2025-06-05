import { useState, useEffect } from 'react';
import UserNavbar from '../../components/UserNavbar';
import { BookLoan, LoanStatus } from '../../data/bookLoans';
import { BookLoanService } from '../../services/bookLoanService';
import { BookRequestService } from '../../services/bookRequestService';
import authService from '../../services/authService';

export default function UserLoaned() {
    // State
    const [loans, setLoans] = useState<BookLoan[]>([]);
    const [filterStatus, setFilterStatus] = useState<string>('');
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState<string | null>(null);
    const [returningLoanId, setReturningLoanId] = useState<string | null>(null);
    const userId = authService.getCurrentUserId();
    
    // Load loans on component mount
    useEffect(() => {
        const loadLoans = async () => {
            try {
                setLoading(true);
                const userId = authService.getCurrentUserId();
                const userLoans = await BookLoanService.getByUser(userId);
                setLoans(userLoans);
                setError(null);
            } catch (err) {
                console.error('Error loading loans:', err);
                setError('Failed to load your loaned books');
            } finally {
                setLoading(false);
            }
        };
        
        loadLoans();
    }, []);
    
    // Filter loans based on status
    const filteredLoans = loans.filter(loan => 
        filterStatus ? loan.status === filterStatus : true
    );
    
    // Handle requesting to return a book
    const handleRequestReturn = async (loan: BookLoan) => {
        try {
            setReturningLoanId(loan.id);
            
            // Create return request
            await BookRequestService.create(userId, loan.bookCopyId);
            
            // Refresh loans
            const updatedLoans = await BookLoanService.getByUser(userId);
            setLoans(updatedLoans);
            
            alert('Return request submitted successfully');
        } catch (error) {
            console.error('Error requesting return:', error);
            alert(error.message || 'Failed to submit return request');
        } finally {
            setReturningLoanId(null);
        }
    };
    
    // Get status display class
    const getStatusClass = (status: LoanStatus) => {
        switch (status) {
            case 'BORROWED':
                return 'bg-green-100 text-green-800';
            case 'REQUEST_BORROWING':
                return 'bg-blue-100 text-blue-800';
            case 'REQUEST_RETURNING':
                return 'bg-yellow-100 text-yellow-800';
            case 'RETURNED':
                return 'bg-gray-100 text-gray-800';
            case 'REJECTED':
                return 'bg-red-100 text-red-800';
            case 'NONRETURNABLE':
                return 'bg-purple-100 text-purple-800';
            default:
                return 'bg-gray-100 text-gray-800';
        }
    };
    
    // Calculate days remaining or overdue
    const getDaysRemaining = (dueDate: string) => {
        const due = new Date(dueDate);
        const today = new Date();
        const diffTime = due.getTime() - today.getTime();
        const diffDays = Math.ceil(diffTime / (1000 * 60 * 60 * 24));
        
        if (diffDays < 0) {
            return <span className="text-red-600 font-semibold">{Math.abs(diffDays)} days overdue</span>;
        }
        
        return <span className="text-green-600 font-semibold">{diffDays} days remaining</span>;
    };
    
    // Show loading indicator
    if (loading) {
        return (
            <>
                <UserNavbar selected="loan" />
                <div className="min-h-screen bg-blue-50 p-6">
                    <div className="text-center py-10">
                        <p className="text-blue-600">Loading your loans...</p>
                    </div>
                </div>
            </>
        );
    }
    
    // Show error message
    if (error) {
        return (
            <>
                <UserNavbar selected="loan" />
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
            <title>My Loaned Books</title>
            <UserNavbar selected="loan" />
            
            <div className="min-h-screen bg-blue-50 p-6">
                <h2 className="text-2xl font-semibold text-blue-700 mb-4">My Loaned Books</h2>
                <p className="text-gray-700 mb-6">
                    View your current and past book loans.
                </p>
                
                {/* Filter bar */}
                <div className="bg-white rounded-lg shadow-md p-4 mb-6">
                    <div className="flex flex-col sm:flex-row justify-between items-center">
                        <div className="mb-3 sm:mb-0">
                            <span className="font-medium text-gray-700 mr-2">Filter by status:</span>
                            <select 
                                className="border border-gray-300 rounded px-3 py-1"
                                value={filterStatus}
                                onChange={(e) => setFilterStatus(e.target.value)}
                            >
                                <option value="">All</option>
                                <option value="BORROWED">Borrowed</option>
                                <option value="REQUEST_BORROWING">Request Borrowing</option>
                                <option value="REQUEST_RETURNING">Request Returning</option>
                                <option value="RETURNED">Returned</option>
                                <option value="REJECTED">Rejected</option>
                                <option value="NONRETURNABLE">Non-Returnable</option>
                            </select>
                        </div>
                        <div className="text-sm text-gray-600">
                            Showing {filteredLoans.length} of {loans.length} loans
                        </div>
                    </div>
                </div>
                
                {/* Loans list */}
                {filteredLoans.length === 0 ? (
                    <div className="bg-white rounded-lg shadow-md p-10 text-center">
                        <p className="text-gray-500">
                            {filterStatus 
                                ? `You have no loans with the status "${filterStatus}".` 
                                : "You haven't borrowed any books yet."}
                        </p>
                    </div>
                ) : (
                    <div className="space-y-4">
                        {filteredLoans.map(loan => (
                            <div key={loan.id} className="bg-white rounded-lg shadow-md p-6">
                                <div className="flex flex-col md:flex-row md:items-center justify-between">
                                    <div>
                                        <h3 className="text-lg font-semibold text-blue-800">{loan.bookCopyOriginalBookTitle}</h3>
                                        <p className="text-sm text-gray-600">Book Copy ID: {loan.bookCopyId}</p>
                                        <div className="flex flex-col sm:flex-row sm:space-x-4 mt-2">
                                            <p className="text-sm">
                                                <span className="font-medium">Loan Date:</span> {new Date(loan.loanDate).toLocaleDateString()}
                                            </p>
                                            <p className="text-sm">
                                                <span className="font-medium">Due Date:</span> {new Date(loan.dueDate).toLocaleDateString()}
                                            </p>
                                            {loan.actualReturnDate && (
                                                <p className="text-sm">
                                                    <span className="font-medium">Returned:</span> {new Date(loan.actualReturnDate).toLocaleDateString()}
                                                </p>
                                            )}
                                        </div>
                                        <div className="mt-2 flex items-center space-x-4">
                                            <span className={`px-3 py-1 rounded-full text-xs ${getStatusClass(loan.status as LoanStatus)}`}>
                                                {loan.status}
                                            </span>
                                            {loan.status === 'BORROWED' && (
                                                <span className="text-sm">
                                                    {getDaysRemaining(loan.dueDate)}
                                                </span>
                                            )}
                                        </div>
                                    </div>
                                    
                                    {loan.status === 'BORROWED' && (
                                        <div className="mt-4 md:mt-0">
                                            <button 
                                                className="bg-blue-500 text-white px-4 py-2 rounded hover:bg-blue-600 disabled:bg-blue-300"
                                                onClick={() => handleRequestReturn(loan)}
                                                disabled={returningLoanId === loan.id}
                                            >
                                                {returningLoanId === loan.id ? 'Processing...' : 'Request Return'}
                                            </button>
                                        </div>
                                    )}
                                </div>
                            </div>
                        ))}
                    </div>
                )}
            </div>
        </>
    );
}
