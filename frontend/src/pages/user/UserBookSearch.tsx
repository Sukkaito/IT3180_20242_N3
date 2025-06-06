import { useState, useEffect } from 'react';
import UserNavbar from '../../components/UserNavbar';
import { Book } from '../../data/books';
import { Author } from '../../data/authors';
import { Category } from '../../data/categories';
import { Publisher } from '../../data/publishers';
import bookService from '../../services/bookService';
import { STORAGE_KEY_PREFIX } from '../../services/baseService';
import BookBorrowModal from '../../components/BookBorrowModal';

// Local storage keys
const AUTHORS_STORAGE_KEY = `${STORAGE_KEY_PREFIX}authors`;
const CATEGORIES_STORAGE_KEY = `${STORAGE_KEY_PREFIX}categories`;
const PUBLISHERS_STORAGE_KEY = `${STORAGE_KEY_PREFIX}publishers`;

export default function UserBookSearch() {
    // State for search data
    const [searchTerm, setSearchTerm] = useState('');
    const [books, setBooks] = useState<Book[]>([]);
    const [filteredBooks, setFilteredBooks] = useState<Book[]>([]);
    const [selectedCategory, setSelectedCategory] = useState<string>('');
    const [selectedAuthor, setSelectedAuthor] = useState<string>('');
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState<string | null>(null);
    
    // State for related data
    const [authors, setAuthors] = useState<Author[]>([]);
    const [categories, setCategories] = useState<Category[]>([]);
    const [publishers, setPublishers] = useState<Publisher[]>([]);
    
    // State for requesting books
    const [requestingBookId, setRequestingBookId] = useState<number | null>(null);
    const [processingRequest, setProcessingRequest] = useState(false);
    
    // State for borrowing books
    const [isBorrowModalOpen, setIsBorrowModalOpen] = useState(false);
    const [selectedBook, setSelectedBook] = useState<Book | null>(null);
    
    // Load initial data
    useEffect(() => {
        const loadData = async () => {
            try {
                setLoading(true);
                
                // Load all books
                const booksData = await bookService.getAll();
                setBooks(booksData);
                setFilteredBooks(booksData);
                
                // Load authors from localStorage
                const storedAuthors = localStorage.getItem(AUTHORS_STORAGE_KEY);
                if (storedAuthors) {
                    setAuthors(JSON.parse(storedAuthors));
                }
                
                // Load categories from localStorage
                const storedCategories = localStorage.getItem(CATEGORIES_STORAGE_KEY);
                if (storedCategories) {
                    setCategories(JSON.parse(storedCategories));
                }
                
                // Load publishers from localStorage
                const storedPublishers = localStorage.getItem(PUBLISHERS_STORAGE_KEY);
                if (storedPublishers) {
                    setPublishers(JSON.parse(storedPublishers));
                }
                
                setError(null);
            } catch (err) {
                console.error('Error loading data:', err);
                setError('Failed to load books');
            } finally {
                setLoading(false);
            }
        };
        
        loadData();
    }, []);
    
    // Handle search when search term changes
    useEffect(() => {
        const searchBooks = async () => {
            if (!searchTerm.trim()) {
                // If search term is empty, reset to all books
                setFilteredBooks(books);
                return;
            }
            
            try {
                const results = await bookService.search(searchTerm);
                setFilteredBooks(results);
            } catch (error) {
                console.error('Error searching books:', error);
                // If search fails, keep current filtered books
            }
        };
        
        // Debounce search to avoid too many API calls
        const debounceTimer = setTimeout(searchBooks, 300);
        return () => clearTimeout(debounceTimer);
    }, [searchTerm, books]);
    
    // Apply category and author filters
    useEffect(() => {
        let results = [...books];
        
        // Apply search term filter (client-side filtering as backup)
        if (searchTerm.trim()) {
            const term = searchTerm.toLowerCase();
            results = results.filter(book => 
                book.title.toLowerCase().includes(term) || 
                book.description.toLowerCase().includes(term)
            );
        }
        
        // Apply category filter
        if (selectedCategory) {
            const categoryId = parseInt(selectedCategory);
            results = results.filter(book => book.categoryIds.includes(categoryId));
        }
        
        // Apply author filter
        if (selectedAuthor) {
            const authorId = parseInt(selectedAuthor);
            results = results.filter(book => book.authorIds.includes(authorId));
        }
        
        setFilteredBooks(results);
    }, [selectedCategory, selectedAuthor, books, searchTerm]);
        
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
    
    // Get author names for a book
    const getAuthorNames = (book: Book): string => {
        return book.authorIds
            .map(id => authors.find(a => a.id === id)?.name || 'Unknown')
            .join(', ');
    };
    
    // Get category names for a book
    const getCategoryNames = (book: Book): string => {
        return book.categoryIds
            .map(id => categories.find(c => c.id === id)?.name || 'Unknown')
            .join(', ');
    };
    
    // Get publisher name for a book
    const getPublisherName = (book: Book): string => {
        return publishers.find(p => p.id === book.publisherId)?.name || 'Unknown';
    };
    
    // Show loading indicator
    if (loading) {
        return (
            <>
                <UserNavbar selected="search" />
                <div className="min-h-screen bg-blue-50 p-6">
                    <div className="text-center py-10">
                        <p className="text-blue-600">Loading books...</p>
                    </div>
                </div>
            </>
        );
    }
    
    // Show error message
    if (error) {
        return (
            <>
                <UserNavbar selected="search" />
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
            <title>Search Books</title>
            <UserNavbar selected="search" />
            
            <div className="min-h-screen bg-blue-50 p-6">
                <h2 className="text-2xl font-semibold text-blue-700 mb-4">Search Books</h2>
                <p className="text-gray-700 mb-6">
                    Find books in our library and request to borrow them.
                </p>
                
                {/* Search and filter section */}
                <div className="bg-white rounded-lg shadow-md p-6 mb-6">
                    <div className="grid grid-cols-1 md:grid-cols-3 gap-4">
                        {/* Search input */}
                        <div>
                            <label className="block text-sm font-medium text-gray-700 mb-1">Search</label>
                            <input
                                type="text"
                                placeholder="Search by title or description..."
                                className="w-full border border-gray-300 rounded px-3 py-2"
                                value={searchTerm}
                                onChange={(e) => setSearchTerm(e.target.value)}
                            />
                        </div>
                        
                        {/* Category filter */}
                        <div>
                            <label className="block text-sm font-medium text-gray-700 mb-1">Category</label>
                            <select
                                className="w-full border border-gray-300 rounded px-3 py-2"
                                value={selectedCategory}
                                onChange={(e) => setSelectedCategory(e.target.value)}
                            >
                                <option value="">All Categories</option>
                                {categories.map(category => (
                                    <option key={category.id} value={category.id}>
                                        {category.name}
                                    </option>
                                ))}
                            </select>
                        </div>
                        
                        {/* Author filter */}
                        <div>
                            <label className="block text-sm font-medium text-gray-700 mb-1">Author</label>
                            <select
                                className="w-full border border-gray-300 rounded px-3 py-2"
                                value={selectedAuthor}
                                onChange={(e) => setSelectedAuthor(e.target.value)}
                            >
                                <option value="">All Authors</option>
                                {authors.map(author => (
                                    <option key={author.id} value={author.id}>
                                        {author.name}
                                    </option>
                                ))}
                            </select>
                        </div>
                    </div>
                </div>
                
                {/* Results section */}
                <div className="bg-white rounded-lg shadow-md p-6">
                    <h3 className="text-xl font-semibold text-blue-700 mb-4">
                        Results ({filteredBooks.length})
                    </h3>
                    
                    {filteredBooks.length === 0 ? (
                        <p className="text-gray-500 text-center py-10">
                            No books found matching your search criteria.
                        </p>
                    ) : (
                        <div className="space-y-6">
                            {filteredBooks.map(book => (
                                <div key={book.id} className="border-b pb-6">
                                    <div className="flex flex-col md:flex-row">
                                        {/* Book info */}
                                        <div className="flex-grow">
                                            <h4 className="text-lg font-semibold text-blue-800">{book.title}</h4>
                                            <p className="text-sm text-gray-600 mt-1">
                                                <span className="font-medium">Authors:</span> {getAuthorNames(book)}
                                            </p>
                                            <p className="text-sm text-gray-600">
                                                <span className="font-medium">Categories:</span> {getCategoryNames(book)}
                                            </p>
                                            <p className="text-sm text-gray-600">
                                                <span className="font-medium">Publisher:</span> {getPublisherName(book)}
                                            </p>
                                            <p className="text-gray-700 mt-2">
                                                {book.description || 'No description available.'}
                                            </p>
                                        </div>
                                        
                                        {/* Borrow button */}
                                        <div className="mt-4 md:mt-0 md:ml-4 flex items-start">
                                            <button 
                                                className="bg-blue-500 text-white px-4 py-2 rounded hover:bg-blue-600 disabled:bg-blue-300 whitespace-nowrap"
                                                onClick={() => handleBorrowBook(book)}
                                                disabled={processingRequest || requestingBookId === book.id}
                                            >
                                                Borrow Book
                                            </button>
                                        </div>
                                    </div>
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
