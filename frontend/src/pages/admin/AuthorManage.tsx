// Trang quản lý tác giả (Authors)
import { useState } from "react";
import AdminNavbar from "../../components/AdminNavbar.tsx";
import AdminAuthor from "../../components/AdminAuthor.tsx";
import authors from "../../data/authors.ts";

// Trang quản lý tác giả (Authors)
export default function AuthorManage() {
    // State cho ô tìm kiếm
    const [search, setSearch] = useState("");
    // State cho input thêm tên tác giả mới
    const [newAuthorName, setNewAuthorName] = useState("");

    // Lọc danh sách tác giả theo từ khóa tìm kiếm (ID hoặc tên)
    const filteredAuthors = authors.filter((author) => {
        const lowerSearch = search.toLowerCase();
        const idMatch = author.id.toString().includes(lowerSearch);
        const nameMatch = author.name.toLowerCase().includes(lowerSearch);
        return idMatch || nameMatch;
    });

    // Xử lý khi người dùng thêm tác giả mới
    const handleAddAuthor = (name: string) => {
        if (!name.trim()) return; // Bỏ qua nếu tên rỗng
        // TODO: Gọi API backend để thêm author mới
        console.log("Add author:", name);
        setNewAuthorName(""); // Reset input sau khi thêm
    };

    return (
        <>
            {/* Set title cho trang */}
            <title>Manage Authors</title>

            {/* Navbar cho trang admin, đánh dấu tab "authors" được chọn */}
            <AdminNavbar selected="authors" />

            <div className="min-h-screen bg-purple-50">
                <div className="p-4">
                    {/* Tiêu đề và mô tả trang */}
                    <h2 className="text-2xl font-semibold text-purple-700 mb-2">Manage Authors</h2>
                    <p className="text-gray-700 mb-4">
                        View, search, edit or delete authors from the system. Use the filter or add a new author as needed.
                    </p>

                    {/* Thanh tìm kiếm và form thêm tác giả */}
                    <div className="flex flex-col sm:flex-row sm:items-center sm:justify-between mb-4 gap-2">
                        {/* Input tìm kiếm */}
                        <input
                            type="text"
                            placeholder="Search by ID or Name..."
                            className="border border-purple-300 rounded px-3 py-2 w-full sm:w-1/2"
                            value={search}
                            onChange={(e) => setSearch(e.target.value)}
                        />

                        {/* Input + nút thêm tác giả mới */}
                        <div className="flex gap-2 w-full sm:w-auto">
                            <input
                                type="text"
                                placeholder="New author name"
                                className="border border-purple-300 rounded px-3 py-2 flex-grow"
                                value={newAuthorName}
                                onChange={(e) => setNewAuthorName(e.target.value)}
                                onKeyDown={(e) => {
                                    if (e.key === "Enter") {
                                        e.preventDefault();
                                        handleAddAuthor(newAuthorName);
                                    }
                                }}
                            />
                            <button
                                className="bg-green-500 text-white py-2 px-4 rounded hover:bg-green-600"
                                onClick={() => handleAddAuthor(newAuthorName)}
                            >
                                + Add New Author
                            </button>
                        </div>
                    </div>
                </div>

                {/* Hiển thị danh sách tác giả đã lọc */}
                <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 gap-4 px-4 pb-10">
                    {filteredAuthors.map((author) => (
                        // Gọi component con để hiển thị từng tác giả
                        <AdminAuthor key={author.id} author={author} />
                    ))}
                </div>
            </div>
        </>
    );
}
