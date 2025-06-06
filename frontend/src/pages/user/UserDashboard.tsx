import { useState, useEffect } from 'react';
import { Link } from 'react-router-dom';
import UserNavbar from '../../components/UserNavbar';
import { BookLoan } from '../../data/bookLoans';
import { Fine } from '../../data/fines';
import { Book } from '../../data/books';
import { BookLoanService } from '../../services/bookLoanService';
import { FineService } from '../../services/fineService';
import bookService from '../../services/bookService';
import authService from '../../services/authService';
import BookBorrowModal from '../../components/BookBorrowModal';

export default function UserDashboard() {
    // State
    const [loans, setLoans] = useState<BookLoan[]>([]);
    const [fines, setFines] = useState<Fine[]>([]);
    const [books, setBooks] = useState<Book[]>([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState<string | null>(null);
    const [username, setUsername] = useState('');
    
    // State for borrowing books
    const [isBorrowModalOpen, setIsBorrowModalOpen] = useState(false);
    const [selectedBook, setSelectedBook] = useState<Book | null>(null);
    
    // Calculate summary data
    const activeLoans = loans.filter(loan => loan.status === 'BORROWED').length;
    const pendingRequests = loans.filter(loan => 
        loan.status === 'REQUEST_BORROWING' || loan.status === 'REQUEST_RETURNING'
    ).length;
    const totalFines = fines.reduce((sum, fine) => sum + fine.amount, 0);
    
    // Load data on component mount
    useEffect(() => {
        const loadData = async () => {
            try {
                setLoading(true);
                const userId = authService.getCurrentUserId();
                const username = authService.getCurrentUsername();
                setUsername(username);
                
                // Load user's loans
                const loansData = await BookLoanService.getByUser(userId);
                setLoans(loansData);
                
                // Load user's fines
                const finesData = await FineService.getByUser(userId);
                setFines(finesData);
                
                // Load recommended books (latest books)
                const booksData = await bookService.getAll();
                // Show only the first 4 books
                setBooks(booksData.slice(0, 4));
                
                setError(null);
            } catch (err) {
                console.error('Error loading dashboard data:', err);
                setError('Failed to load dashboard data');
            } finally {
                setLoading(false);
            }
        };
        
        loadData();
    }, []);
    
    // Handle opening borrow modal
    const handleBorrowBook = (book: Book) => {
        setSelectedBook(book);
        setIsBorrowModalOpen(true);
    };
    
    // Handle successful borrowing
    const handleBorrowSuccess = () => {
        // You could update the UI or fetch latest data if needed
        alert('Book borrowing request successful!');
    };
    
    // Find soon-to-be-due books (within 7 days)
    const soonDueBooks = loans
        .filter(loan => {
            if (loan.status !== 'BORROWED') return false;
            const dueDate = new Date(loan.dueDate);
            const today = new Date();
            const diffTime = dueDate.getTime() - today.getTime();
            const diffDays = Math.ceil(diffTime / (1000 * 60 * 60 * 24));
            return diffDays >= 0 && diffDays <= 7;
        })
        .sort((a, b) => new Date(a.dueDate).getTime() - new Date(b.dueDate).getTime());
    
    // Show loading indicator
    if (loading) {
        return (
            <>
                <UserNavbar selected="home" />
                <div className="min-h-screen bg-blue-50 p-6">
                    <div className="text-center py-10">
                        <p className="text-blue-600">Loading dashboard...</p>
                    </div>
                </div>
            </>
        );
    }
    
    // Show error message
    if (error) {
        return (
            <>
                <UserNavbar selected="home" />
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
            <title>Library Dashboard</title>
            <UserNavbar selected="home" />
            
            <div className="min-h-screen bg-blue-50 p-6">
                {/* Welcome section */}
                <div className="bg-white rounded-lg shadow-md p-6 mb-6">
                    <h2 className="text-2xl font-semibold text-blue-700">Welcome, {username}!</h2>
                    <p className="text-gray-600 mt-2">
                        Here's an overview of your library account.
                    </p>
                </div>
                
                {/* Summary cards */}
                <div className="grid grid-cols-1 md:grid-cols-3 gap-6 mb-6">
                    {/* Active loans */}
                    <div className="bg-white rounded-lg shadow-md p-6">
                        <div className="flex justify-between items-center">
                            <div>
                                <h3 className="text-lg font-semibold text-blue-700">Active Loans</h3>
                                <p className="text-3xl font-bold text-blue-800 mt-2">{activeLoans}</p>
                            </div>
                            <div className="bg-blue-100 p-3 rounded-full">
                                <svg xmlns="http://www.w3.org/2000/svg" className="h-8 w-8 text-blue-600" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                                    <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M12 6.253v13m0-13C10.832 5.477 9.246 5 7.5 5S4.168 5.477 3 6.253v13C4.168 18.477 5.754 18 7.5 18s3.332.477 4.5 1.253m0-13C13.168 5.477 14.754 5 16.5 5c1.747 0 3.332.477 4.5 1.253v13C19.832 18.477 18.247 18 16.5 18c-1.746 0-3.332.477-4.5 1.253" />
                                </svg>
                            </div>
                        </div>
                        <Link to="/user/loaned" className="block mt-4 text-blue-600 hover:underline">View all loans →</Link>
                    </div>
                    
                    {/* Pending requests */}
                    <div className="bg-white rounded-lg shadow-md p-6">
                        <div className="flex justify-between items-center">
                            <div>
                                <h3 className="text-lg font-semibold text-blue-700">Pending Requests</h3>
                                <p className="text-3xl font-bold text-blue-800 mt-2">{pendingRequests}</p>
                            </div>
                            <div className="bg-yellow-100 p-3 rounded-full">
                                <svg xmlns="http://www.w3.org/2000/svg" className="h-8 w-8 text-yellow-600" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                                    <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M12 8v4l3 3m6-3a9 9 0 11-18 0 9 9 0 0118 0z" />
                                </svg>
                            </div>
                        </div>
                        <Link to="/user/requests" className="block mt-4 text-blue-600 hover:underline">View all requests →</Link>
                    </div>
                    
                    {/* Fines */}
                    <div className="bg-white rounded-lg shadow-md p-6">
                        <div className="flex justify-between items-center">
                            <div>
                                <h3 className="text-lg font-semibold text-blue-700">Outstanding Fines</h3>
                                <p className="text-3xl font-bold text-red-600 mt-2">{totalFines.toLocaleString()} VND</p>
                            </div>
                            <div className="bg-red-100 p-3 rounded-full">
                                <svg xmlns="http://www.w3.org/2000/svg" className="h-8 w-8 text-red-600" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                                    <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M12 8c-1.657 0-3 .895-3 2s1.343 2 3 2 3 .895 3 2-1.343 2-3 2m0-8c1.11 0 2.08.402 2.599 1M12 8V7m0 1v8m0 0v1m0-1c-1.11 0-2.08-.402-2.599-1M21 12a9 9 0 11-18 0 9 9 0 0118 0z" />
                                </svg>
                            </div>
                        </div>
                        <Link to="/user/fines" className="block mt-4 text-blue-600 hover:underline">View all fines →</Link>
                    </div>
                </div>
                
                {/* Quick actions */}
                <div className="bg-white rounded-lg shadow-md p-6 mb-6">
                    <h3 className="text-lg font-semibold text-blue-700 mb-4">Quick Actions</h3>
                    <div className="grid grid-cols-1 sm:grid-cols-2 md:grid-cols-3 gap-4">
                        <Link to="/user/search" className="bg-blue-100 hover:bg-blue-200 p-4 rounded-lg flex items-center gap-3 transition">
                            <svg xmlns="http://www.w3.org/2000/svg" className="h-6 w-6 text-blue-700" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                                <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M21 21l-6-6m2-5a7 7 0 11-14 0 7 7 0 0114 0z" />
                            </svg>
                            <span className="font-medium">Search Books</span>
                        </Link>
                        <Link to="/user/profile" className="bg-green-100 hover:bg-green-200 p-4 rounded-lg flex items-center gap-3 transition">
                            <svg xmlns="http://www.w3.org/2000/svg" className="h-6 w-6 text-green-700" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                                <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M16 7a4 4 0 11-8 0 4 4 0 018 0zM12 14a7 7 0 00-7 7h14a7 7 0 00-7-7z" />
                            </svg>
                            <span className="font-medium">Update Profile</span>
                        </Link>
                        <Link to="/user/loaned" className="bg-yellow-100 hover:bg-yellow-200 p-4 rounded-lg flex items-center gap-3 transition">
                            <svg xmlns="http://www.w3.org/2000/svg" className="h-6 w-6 text-yellow-700" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                                <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M9 5H7a2 2 0 00-2 2v12a2 2 0 002 2h10a2 2 0 002-2V7a2 2 0 00-2-2h-2M9 5a2 2 0 002 2h2a2 2 0 002-2M9 5a2 2 0 012-2h2a2 2 0 012 2" />
                            </svg>
                            <span className="font-medium">Manage Loans</span>
                        </Link>
                    </div>
                </div>
                
                {/* Due soon section */}
                {soonDueBooks.length > 0 && (
                    <div className="bg-white rounded-lg shadow-md p-6 mb-6">
                        <h3 className="text-lg font-semibold text-orange-600 mb-4">Books Due Soon</h3>
                        <div className="space-y-4">
                            {soonDueBooks.map(loan => {
                                const dueDate = new Date(loan.dueDate);
                                const today = new Date();
                                const diffTime = dueDate.getTime() - today.getTime();
                                const diffDays = Math.ceil(diffTime / (1000 * 60 * 60 * 24));
                                
                                return (
                                    <div key={loan.id} className="flex justify-between items-center border-b pb-4">
                                        <div>
                                            <p className="font-medium">{loan.bookCopyOriginalBookTitle}</p>
                                            <p className="text-sm text-gray-600">Due: {new Date(loan.dueDate).toLocaleDateString()}</p>
                                        </div>
                                        <div className={`px-3 py-1 rounded-full text-sm font-medium ${
                                            diffDays <= 2 ? 'bg-red-100 text-red-800' : 'bg-orange-100 text-orange-800'
                                        }`}>
                                            {diffDays} {diffDays === 1 ? 'day' : 'days'} left
                                        </div>
                                    </div>
                                );
                            })}
                        </div>
                    </div>
                )}
                
                {/* Recommended Books section */}
                <div className="bg-white rounded-lg shadow-md p-6 mb-6">
                    <div className="flex justify-between items-center mb-4">
                        <h3 className="text-lg font-semibold text-blue-700">Books You Might Like</h3>
                        <Link to="/user/search" className="text-blue-600 hover:underline text-sm">
                            View all books →
                        </Link>
                    </div>
                    
                    {books.length === 0 ? (
                        <p className="text-gray-500 text-center py-4">No book recommendations available.</p>
                    ) : (
                        <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-4">
                            {books.map(book => (
                                <div key={book.id} className="border rounded-lg p-4 hover:shadow-md transition">
                                    <h4 className="font-medium text-blue-800 mb-1">{book.title}</h4>
                                    <p className="text-sm text-gray-600 mb-2 line-clamp-2">
                                        {book.description || 'No description available.'}
                                    </p>
                                    <button 
                                        className="w-full mt-2 bg-blue-500 text-white py-1.5 px-3 rounded hover:bg-blue-600 transition flex items-center justify-center"
                                        onClick={() => handleBorrowBook(book)}
                                    >
                                        <svg xmlns="http://www.w3.org/2000/svg" className="h-4 w-4 mr-1" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                                            <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M12 6v6m0 0v6m0-6h6m-6 0H6" />
                                        </svg>
                                        Borrow
                                    </button>
                                </div>
                            ))}
                        </div>
                    )}
                </div>
            </div>
            
            {/* Borrow Modal */}
            <BookBorrowModal 
                isOpen={isBorrowModalOpen}
                onClose={() => setIsBorrowModalOpen(false)}
                book={selectedBook}
                onSuccess={handleBorrowSuccess}
            />
        </>
    );
}
