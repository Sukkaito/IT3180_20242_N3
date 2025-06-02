// Import các hook và component cần thiết
import { useState } from "react";
import { useNavigate } from "react-router-dom";  // để chuyển trang khi xem chi tiết
import AdminNavbar from "../../components/AdminNavbar.tsx";
import AdminBook from "../../components/AdminBook.tsx";
import BookFormModal from "../../components/BookFormModal.tsx";
import booksData from "../../data/books.ts";       // dữ liệu sách mẫu
import authors from "../../data/authors.ts";
import categories from "../../data/categories.ts";
import publishers from "../../data/publishers.ts";

export default function BookManage() {
    // State lưu chuỗi tìm kiếm
    const [search, setSearch] = useState("");
    // State quản lý trạng thái mở/đóng modal form thêm/sửa sách
    const [isModalOpen, setIsModalOpen] = useState(false);
    // State lưu sách đang được chỉnh sửa (null nếu thêm mới)
    const [editingBook, setEditingBook] = useState(null);
    // State danh sách sách, khởi tạo từ dữ liệu mẫu
    const [books, setBooks] = useState(booksData);

    // Hook dùng để chuyển trang
    const navigate = useNavigate();

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
    const handleEdit = (book) => {
        setEditingBook(book);    // gán sách cần sửa
        setIsModalOpen(true);    // mở modal
    };

    // Đóng modal và reset sách đang chỉnh sửa
    const handleCloseModal = () => {
        setIsModalOpen(false);
        setEditingBook(null);
    };

    // Chuyển sang trang xem chi tiết sách
    // Lưu ý URL đúng với đường dẫn có "manage"
    const handleView = (bookId) => {
        navigate(`/admin/manage/books/${bookId}`);
    };

    // Xóa sách theo ID, hỏi xác nhận trước
    const handleDelete = (bookId) => {
        if (window.confirm("Are you sure you want to delete this book?")) {
            setBooks(prev => prev.filter(book => book.id !== bookId));
        }
    };

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
                    {filteredBooks.map((book) => (
                        <AdminBook
                            key={book.id}
                            book={book}
                            onView={() => handleView(book.id)}
                            onEdit={() => handleEdit(book)}
                            onDelete={() => handleDelete(book.id)}
                        />
                    ))}
                </div>
            </div>

            <BookFormModal
                isOpen={isModalOpen}
                onClose={handleCloseModal}
                onSubmit={() => handleCloseModal()}
                initialData={editingBook}
            />
        </>
    );
}
