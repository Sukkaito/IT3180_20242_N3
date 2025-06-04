// Trang thông tin chi tiết từng sách 
import { useParams } from "react-router-dom"; // Hook lấy params từ URL
import AdminNavbar from "../../components/AdminNavbar"; // Navbar cho trang admin
import books from "../../data/books"; // Data sách mẫu
import authors from "../../data/authors"; // Data tác giả mẫu
import categories from "../../data/categories"; // Data danh mục sách mẫu
import publishers from "../../data/publishers"; // Data nhà xuất bản mẫu
import bookCopies from "../../data/bookCopies"; // Data bản sao sách mẫu
import { useState } from "react";

export default function BookDetail() {
    // Lấy tham số bookId từ URL (vd: /books/123 thì bookId = "123")
    const { bookId } = useParams();

    // Chuyển bookId sang số để dễ xử lý
    const bookIdNumber = bookId ? parseInt(bookId) : NaN;

    // Nếu bookId không hợp lệ (không phải số), hiển thị lỗi
    if (isNaN(bookIdNumber)) {
        return <div className="p-4 text-red-500">Invalid book ID!</div>;
    }

    // Tìm sách trong data theo id lấy được
    const book = books.find((b) => b.id === bookIdNumber);

    // Nếu không tìm thấy sách nào với id đó, hiển thị lỗi
    if (!book) {
        return <div className="p-4 text-red-500">Book not found!</div>;
    }

    // Lấy tên tác giả dựa trên mảng authorIds của sách, nối thành chuỗi
    const authorNames = book.authorIds
        .map((id) => authors.find((a) => a.id === id)?.name || "Unknown")
        .join(", ");

    // Lấy tên danh mục dựa trên mảng categoryIds của sách, nối thành chuỗi
    const categoryNames = book.categoryIds
        .map((id) => categories.find((c) => c.id === id)?.name || "Unknown")
        .join(", ");

    // Lấy tên nhà xuất bản dựa trên publisherId của sách
    const publisherName =
        publishers.find((p) => p.id === book.publisherId)?.name || "Unknown";

    // Lọc lấy các bản copy của cuốn sách này từ bookCopies (dựa theo original_book_book_id)
    const filteredCopies = bookCopies.filter(
        (copy) => copy.original_book_book_id === bookIdNumber
    );

    // Xử lý sự kiện khi nhấn nút "Add New Copy"
    const handleAddCopy = () => {
        alert("Add New Copy button clicked");
        // TODO: cài đặt thêm chức năng tạo bản copy mới ở đây
    };

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
                    <h3 className="text-xl font-semibold text-purple-800 mb-2">{book.title}</h3>
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
                        <strong>Description:</strong> {book.description}
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
                                            {/* Nút xoá (hiện chưa có xử lý) */}
                                            <button className="bg-red-500 text-white py-1 px-3 rounded hover:bg-red-600 text-sm">
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
