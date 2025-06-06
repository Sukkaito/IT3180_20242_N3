import { useState, useEffect } from 'react';
import UserNavbar from '../../components/UserNavbar';
import { Fine } from '../../data/fines';
import { FineService } from '../../services/fineService';
import authService from '../../services/authService';

export default function UserFines() {
    // State
    const [fines, setFines] = useState<Fine[]>([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState<string | null>(null);
    const [sortBy, setSortBy] = useState<'date' | 'amount'>('date');
    const [sortDirection, setSortDirection] = useState<'asc' | 'desc'>('desc');
    
    // Total fine amount
    const totalAmount = fines.reduce((sum, fine) => sum + fine.amount, 0);
    
    // Load fines on component mount
    useEffect(() => {
        const loadFines = async () => {
            try {
                setLoading(true);
                const userId = authService.getCurrentUserId();
                const userFines = await FineService.getByUser(userId);
                setFines(userFines);
                setError(null);
            } catch (err) {
                console.error('Error loading fines:', err);
                setError('Failed to load your fines');
            } finally {
                setLoading(false);
            }
        };
        
        loadFines();
    }, []);
    
    // Sort fines
    const sortedFines = [...fines].sort((a, b) => {
        if (sortBy === 'date') {
            const dateA = new Date(a.createdAt).getTime();
            const dateB = new Date(b.createdAt).getTime();
            return sortDirection === 'asc' ? dateA - dateB : dateB - dateA;
        } else {
            return sortDirection === 'asc' ? a.amount - b.amount : b.amount - a.amount;
        }
    });
    
    // Toggle sort direction
    const toggleSort = (field: 'date' | 'amount') => {
        if (sortBy === field) {
            setSortDirection(sortDirection === 'asc' ? 'desc' : 'asc');
        } else {
            setSortBy(field);
            setSortDirection('desc');
        }
    };
    
    // Show loading indicator
    if (loading) {
        return (
            <>
                <UserNavbar selected="fine" />
                <div className="min-h-screen bg-blue-50 p-6">
                    <div className="text-center py-10">
                        <p className="text-blue-600">Loading your fines...</p>
                    </div>
                </div>
            </>
        );
    }
    
    // Show error message
    if (error) {
        return (
            <>
                <UserNavbar selected="fine" />
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
            <title>My Fines</title>
            <UserNavbar selected="fine" />
            
            <div className="min-h-screen bg-blue-50 p-6">
                <h2 className="text-2xl font-semibold text-blue-700 mb-4">My Fines</h2>
                <p className="text-gray-700 mb-6">
                    View and manage your library fines.
                </p>
                
                {/* Summary card */}
                <div className="bg-white rounded-lg shadow-md p-6 mb-6">
                    <div className="flex flex-col md:flex-row justify-between items-center">
                        <div>
                            <h3 className="text-lg font-semibold text-blue-700">Summary</h3>
                            <p className="text-gray-600 mt-2">
                                You have {fines.length} {fines.length === 1 ? 'fine' : 'fines'} totaling:
                            </p>
                        </div>
                        <div className="mt-4 md:mt-0 text-3xl font-bold text-red-600">
                            {totalAmount.toLocaleString()} VND
                        </div>
                    </div>
                </div>
                
                {/* Fines list */}
                {fines.length === 0 ? (
                    <div className="bg-white rounded-lg shadow-md p-10 text-center">
                        <p className="text-gray-500">
                            You don't have any fines. Keep returning your books on time!
                        </p>
                    </div>
                ) : (
                    <div className="bg-white rounded-lg shadow-md overflow-hidden">
                        <table className="min-w-full">
                            <thead className="bg-gray-50">
                                <tr>
                                    <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                                        Book Loan ID
                                    </th>
                                    <th 
                                        className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider cursor-pointer"
                                        onClick={() => toggleSort('amount')}
                                    >
                                        Amount {sortBy === 'amount' && (sortDirection === 'asc' ? '↑' : '↓')}
                                    </th>
                                    <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                                        Description
                                    </th>
                                    <th 
                                        className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider cursor-pointer"
                                        onClick={() => toggleSort('date')}
                                    >
                                        Date {sortBy === 'date' && (sortDirection === 'asc' ? '↑' : '↓')}
                                    </th>
                                </tr>
                            </thead>
                            <tbody className="bg-white divide-y divide-gray-200">
                                {sortedFines.map(fine => (
                                    <tr key={fine.id} className="hover:bg-gray-50">
                                        <td className="px-6 py-4 whitespace-nowrap text-sm font-medium text-gray-900">
                                            {fine.bookLoanId}
                                        </td>
                                        <td className="px-6 py-4 whitespace-nowrap text-sm text-red-600 font-semibold">
                                            {fine.amount.toLocaleString()} VND
                                        </td>
                                        <td className="px-6 py-4 text-sm text-gray-500">
                                            {fine.description || 'No description'}
                                        </td>
                                        <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-500">
                                            {new Date(fine.createdAt).toLocaleDateString()}
                                        </td>
                                    </tr>
                                ))}
                            </tbody>
                        </table>
                    </div>
                )}
                
                {/* Payment information */}
                {fines.length > 0 && (
                    <div className="mt-6 bg-blue-100 rounded-lg shadow-md p-6">
                        <h3 className="text-lg font-semibold text-blue-700 mb-2">Payment Information</h3>
                        <p className="text-gray-700">
                            Please visit the library to pay your fines. We accept cash and card payments.
                        </p>
                        <p className="text-gray-700 mt-2">
                            Note: Unpaid fines may affect your ability to borrow books in the future.
                        </p>
                    </div>
                )}
            </div>
        </>
    );
}
