// Import các hook và component cần thiết
import { useState, useEffect } from "react";
import { useNavigate } from "react-router-dom";  // để chuyển trang khi xem chi tiết
import AdminNavbar from "../../components/AdminNavbar.tsx";
import AdminBook from "../../components/AdminBook.tsx";
import BookFormModal from "../../components/BookFormModal.tsx";
import { Book } from "../../data/books.ts";
import { Author } from "../../data/authors.ts";
import { Category } from "../../data/categories.ts";
import { Publisher } from "../../data/publishers.ts";
import { STORAGE_KEY_PREFIX } from "../../services/baseService.ts";
import bookService from "../../services/bookService.ts";

// Local storage keys
const AUTHORS_STORAGE_KEY = `${STORAGE_KEY_PREFIX}authors`;
const CATEGORIES_STORAGE_KEY = `${STORAGE_KEY_PREFIX}categories`;
const PUBLISHERS_STORAGE_KEY = `${STORAGE_KEY_PREFIX}publishers`;

export default function BookManage() {
    // State lưu chuỗi tìm kiếm
    const [search, setSearch] = useState("");
    // State quản lý trạng thái mở/đóng modal form thêm/sửa sách
    const [isModalOpen, setIsModalOpen] = useState(false);
    // State lưu sách đang được chỉnh sửa (null nếu thêm mới)
    const [editingBook, setEditingBook] = useState<Book | null>(null);
    // State danh sách sách
    const [books, setBooks] = useState<Book[]>([]);
    // State for authors, categories, publishers
    const [authors, setAuthors] = useState<Author[]>([]);
    const [categories, setCategories] = useState<Category[]>([]);
    const [publishers, setPublishers] = useState<Publisher[]>([]);
    // Loading state
    const [loading, setLoading] = useState(true);

    // Hook dùng để chuyển trang
    const navigate = useNavigate();

    // Load data on component mount
    useEffect(() => {
        const loadData = async () => {
            try {
                setLoading(true);
                
                // Load books using the service
                const booksData = await bookService.getAll();
                setBooks(booksData);
                
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
            } catch (error) {
                console.error("Error loading data:", error);
            } finally {
                setLoading(false);
            }
        };
        
        loadData();
    }, []);

    // Lọc danh sách sách dựa trên từ khóa tìm kiếm (title, author, category, publisher)
    const filteredBooks = books.filter((book) => {
        const lowerSearch = search.toLowerCase();

        const titleMatch = book.title.toLowerCase().includes(lowerSearch);

        // Tìm tên tác giả trong mảng authorIds của sách
        const authorMatch = book.authorIds.some(id =>
            authors.find(a => a.id === id)?.name.toLowerCase().includes(lowerSearch)
        );

        // Tìm tên danh mục trong mảng categoryIds
        const categoryMatch = book.categoryIds.some(id =>
            categories.find(c => c.id === id)?.name.toLowerCase().includes(lowerSearch)
        );

        // Tìm tên nhà xuất bản
        const publisherMatch = publishers.find(p => p.id === book.publisherId)?.name.toLowerCase().includes(lowerSearch);

        return titleMatch || authorMatch || categoryMatch || publisherMatch;
    });

    // Mở modal form thêm mới sách
    const handleAdd = () => {
        setEditingBook(null);    // reset dữ liệu đang chỉnh sửa (thêm mới)
        setIsModalOpen(true);    // mở modal
    };

    // Mở modal form chỉnh sửa sách
    const handleEdit = (book: Book) => {
        setEditingBook(book);    // gán sách cần sửa
        setIsModalOpen(true);    // mở modal
    };

    // Đóng modal và reset sách đang chỉnh sửa
    const handleCloseModal = () => {
        setIsModalOpen(false);
        setEditingBook(null);
    };

    // Chuyển sang trang xem chi tiết sách
    const handleView = (bookId: number) => {
        navigate(`/admin/manage/books/${bookId}`);
    };

    // Xóa sách theo ID, hỏi xác nhận trước
    const handleDelete = async (bookId: number) => {
        if (window.confirm("Are you sure you want to delete this book?")) {
            try {
                await bookService.delete(bookId);
                // Update the local state after successful deletion
                setBooks(prev => prev.filter(book => book.id !== bookId));
            } catch (error) {
                console.error("Error deleting book:", error);
                alert("Failed to delete the book. Please try again.");
            }
        }
    };

    // Handle form submission
    const handleSubmit = async (bookData: Partial<Book>) => {
        try {
            if (editingBook) {
                // Update existing book
                const updatedBook = await bookService.update(editingBook.id, bookData);
                setBooks(prev => prev.map(book => 
                    book.id === editingBook.id ? updatedBook : book
                ));
            } else {
                // Create new book
                const newBook = await bookService.create(bookData as Omit<Book, 'id'>);
                setBooks(prev => [...prev, newBook]);
            }
            handleCloseModal();
        } catch (error) {
            console.error("Error saving book:", error);
            alert("Failed to save the book. Please try again.");
        }
    };

    // Show loading indicator while data is being fetched
    if (loading) {
        return (
            <>
                <AdminNavbar selected="books" />
                <div className="min-h-screen bg-purple-50 p-6">
                    <div className="text-center py-10">
                        <p className="text-purple-600">Loading books data...</p>
                    </div>
                </div>
            </>
        );
    }

    return (
        <>
            <title>Manage Books</title>
            <AdminNavbar selected="books" />

            <div className="min-h-screen bg-purple-50">
                <div className="p-4">
                    <h2 className="text-2xl font-semibold text-purple-700 mb-2">Manage Books</h2>
                    <p className="text-gray-700 mb-4">
                        View, search, edit or delete books from the system.
                    </p>

                    <div className="flex flex-col sm:flex-row sm:items-center sm:justify-between mb-4 gap-2">
                        <input
                            type="text"
                            placeholder="Search by title, author, category or publisher..."
                            className="border border-purple-300 rounded px-3 py-2 w-full sm:w-1/2"
                            value={search}
                            onChange={(e) => setSearch(e.target.value)}
                        />
                        <button
                            className="bg-green-500 text-white py-2 px-4 rounded hover:bg-green-600"
                            onClick={handleAdd}
                        >
                            + Add New Book
                        </button>
                    </div>
                </div>

                <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 gap-4 px-4 pb-10">
                    {filteredBooks.length > 0 ? (
                        filteredBooks.map((book) => (
                            <AdminBook
                                key={book.id}
                                book={book}
                                onView={() => handleView(book.id)}
                                onEdit={() => handleEdit(book)}
                                onDelete={() => handleDelete(book.id)}
                            />
                        ))
                    ) : (
                        <div className="col-span-3 text-center py-8 text-gray-500">
                            No books found matching your search criteria.
                        </div>
                    )}
                </div>
            </div>

            <BookFormModal
                isOpen={isModalOpen}
                onClose={handleCloseModal}
                onSubmit={handleSubmit}
                initialData={editingBook}
                authors={authors}
                categories={categories}
                publishers={publishers}
            />
        </>
    );
}
