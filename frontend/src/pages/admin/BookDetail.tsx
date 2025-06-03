// Trang thông tin chi tiết từng sách 
import { useParams } from "react-router-dom"; // Hook lấy params từ URL
import AdminNavbar from "../../components/AdminNavbar"; // Navbar cho trang admin
import books from "../../data/books"; // Data sách mẫu
import { useState, useEffect } from "react";
import { Author } from "../../data/authors";
import { Category } from "../../data/categories";
import { Publisher } from "../../data/publishers";
import { BookCopy } from "../../data/bookCopies";
import { BookCopyService } from "../../services/bookCopyService";
import { Book } from "../../data/books";
import bookService from "../../services/bookService";
import { STORAGE_KEY_PREFIX } from "../../services/baseService";

// Local storage keys
const AUTHORS_STORAGE_KEY = `${STORAGE_KEY_PREFIX}authors`;
const CATEGORIES_STORAGE_KEY = `${STORAGE_KEY_PREFIX}categories`;
const PUBLISHERS_STORAGE_KEY = `${STORAGE_KEY_PREFIX}publishers`;

export default function BookDetail() {
    // State for data
    const [book, setBook] = useState<Book>();
    const [bookCopies, setBookCopies] = useState<BookCopy[]>([]);
    const [authors, setAuthors] = useState<Author[]>([]);
    const [categories, setCategories] = useState<Category[]>([]);
    const [publishers, setPublishers] = useState<Publisher[]>([]);
    const [loading, setLoading] = useState(true);

    // Lấy tham số bookId từ URL
    const { bookId } = useParams();
    const bookIdNumber = bookId ? parseInt(bookId) : NaN;

    // Load data on component mount
    useEffect(() => {
        const loadData = async () => {
            if (isNaN(bookIdNumber)) {
                return;
            }

            try {
                setLoading(true);
                
                // Load book data
                const bookData = await bookService.getById(bookIdNumber);
                if (bookData) {
                    setBook(bookData);
                }
                
                // Load book copies
                const copies = await BookCopyService.getByBookId(bookIdNumber);
                setBookCopies(copies);
                
                // Load authors from local storage
                const storedAuthors = localStorage.getItem(AUTHORS_STORAGE_KEY);
                if (storedAuthors) {
                    setAuthors(JSON.parse(storedAuthors));
                }
                
                // Load categories from local storage
                const storedCategories = localStorage.getItem(CATEGORIES_STORAGE_KEY);
                if (storedCategories) {
                    setCategories(JSON.parse(storedCategories));
                }
                
                // Load publishers from local storage
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
    }, [bookIdNumber]);

    // Handle adding a new book copy
    const handleAddCopy = async () => {
        if (!book) return;
        
        try {
            const newCopy = await BookCopyService.create(book.id);
            setBookCopies(prev => [...prev, newCopy]);
        } catch (error) {
            console.error("Error creating book copy:", error);
            alert("Failed to create a new copy. Please try again.");
        }
    };

    // Handle deleting a book copy
    const handleDeleteCopy = async (copyId: number) => {
        if (window.confirm("Are you sure you want to delete this copy?")) {
            try {
                await BookCopyService.delete(copyId);
                setBookCopies(prev => prev.filter(copy => copy.id !== copyId));
            } catch (error) {
                console.error("Error deleting book copy:", error);
                alert("Failed to delete the copy. " + error.message);
            }
        }
    };

    // Nếu bookId không hợp lệ (không phải số), hiển thị lỗi
    if (isNaN(bookIdNumber)) {
        return <div className="p-4 text-red-500">Invalid book ID!</div>;
    }

    // Tìm sách trong data theo id lấy được
    const foundBook = books.find((b) => b.id === bookIdNumber);

    // Nếu không tìm thấy sách nào với id đó, hiển thị lỗi
    if (!foundBook) {
        return <div className="p-4 text-red-500">Book not found!</div>;
    }

    // Lấy tên tác giả dựa trên mảng authorIds của sách, nối thành chuỗi
    const authorNames = foundBook.authorIds
        .map((id) => authors.find((a) => a.id === id)?.name || "Unknown")
        .join(", ");

    // Lấy tên danh mục dựa trên mảng categoryIds của sách, nối thành chuỗi
    const categoryNames = foundBook.categoryIds
        .map((id) => categories.find((c) => c.id === id)?.name || "Unknown")
        .join(", ");

    // Lấy tên nhà xuất bản dựa trên publisherId của sách
    const publisherName =
        publishers.find((p) => p.id === foundBook.publisherId)?.name || "Unknown";

    // Lọc lấy các bản copy của cuốn sách này từ bookCopies
    const filteredCopies = bookCopies.filter(
        (copy) => copy.originalBookBookId === bookIdNumber
    );

    // If still loading data, show a loading indicator
    if (loading) {
        return (
            <>
                <AdminNavbar selected="books" />
                <div className="min-h-screen bg-purple-50 p-6">
                    <div className="text-center py-10">
                        <p className="text-purple-600">Loading book details...</p>
                    </div>
                </div>
            </>
        );
    }

    return (
        <>
            {/* Đặt title cho trang */}
            <title>Book Detail</title>

            {/* Thanh navbar admin, phần sách đang được chọn */}
            <AdminNavbar selected="books" />

            <div className="min-h-screen bg-purple-50 p-6">
                {/* Tiêu đề trang */}
                <h2 className="text-2xl font-bold text-purple-700 mb-4">Book Detail</h2>

                {/* Thông tin chi tiết sách */}
                <div className="bg-white rounded shadow-md p-4 mb-4">
                    <h3 className="text-xl font-semibold text-purple-800 mb-2">{foundBook.title}</h3>
                    <p>
                        <strong>Authors:</strong> {authorNames}
                    </p>
                    <p>
                        <strong>Categories:</strong> {categoryNames}
                    </p>
                    <p>
                        <strong>Publisher:</strong> {publisherName}
                    </p>
                    <p>
                        <strong>Description:</strong> {foundBook.description}
                    </p>
                </div>

                {/* Nút thêm bản copy mới */}
                <div className="flex justify-end mb-2">
                    <button
                        className="bg-green-500 text-white py-2 px-4 rounded hover:bg-green-600"
                        onClick={handleAddCopy}
                    >
                        + Add New Copy
                    </button>
                </div>

                {/* Bảng liệt kê các bản copy của sách */}
                <div className="overflow-x-auto bg-white shadow-lg rounded-2xl">
                    <table className="min-w-full text-center">
                        <thead className="bg-purple-100 text-purple-700">
                            <tr>
                                <th className="py-3 px-4">ID</th>
                                <th className="py-3 px-4">Status</th>
                                <th className="py-3 px-4">Actions</th>
                            </tr>
                        </thead>
                        <tbody>
                            {/* Nếu có bản copy thì map ra từng dòng */}
                            {filteredCopies.length > 0 ? (
                                filteredCopies.map((copy) => (
                                    <tr
                                        key={copy.id}
                                        className="border-t hover:bg-purple-50 transition"
                                    >
                                        <td className="py-3 px-4">{copy.id}</td>
                                        <td
                                            className={`py-3 px-4 font-semibold ${copy.status === "AVAILABLE"
                                                ? "text-green-600"
                                                : "text-red-600"
                                                }`}
                                        >
                                            {copy.status}
                                        </td>
                                        <td className="py-3 px-4 space-x-2">
                                            {/* Nút xoá */}
                                            <button 
                                                className="bg-red-500 text-white py-1 px-3 rounded hover:bg-red-600 text-sm"
                                                onClick={() => handleDeleteCopy(copy.id)}
                                            >
                                                Delete
                                            </button>
                                        </td>
                                    </tr>
                                ))
                            ) : (
                                // Nếu không có bản copy nào, hiển thị thông báo
                                <tr>
                                    <td colSpan={3} className="py-6 text-gray-500">
                                        No copies found.
                                    </td>
                                </tr>
                            )}
                        </tbody>
                    </table>
                </div>
            </div>
        </>
    );
}
