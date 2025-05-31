// Component này được sử dụng để hiển thị thông tin của một tác giả trong trang quản lý.
import { useState } from "react";
import { Author as AuthorType } from "../data/authors";

// Định nghĩa kiểu dữ liệu cho props truyền vào
interface AdminAuthorProps {
    author: AuthorType;                                // Dữ liệu tác giả (id, name)
    onUpdate: (id: number, newName: string) => void;   // Hàm callback để cập nhật tên tác giả
}

export default function AdminAuthor({ author, onUpdate }: AdminAuthorProps) {
    // Trạng thái hiển thị form sửa tên
    const [isEditing, setIsEditing] = useState(false);

    // Trạng thái lưu giá trị tên khi đang chỉnh sửa
    const [editName, setEditName] = useState(author.name);

    // Hàm xử lý khi nhấn nút Save
    const handleSave = () => {
        // Nếu tên mới không rỗng và khác tên cũ thì gọi hàm cập nhật
        if (editName.trim() && editName !== author.name) {
            onUpdate(author.id, editName.trim());
        }
        // Tắt chế độ chỉnh sửa
        setIsEditing(false);
    };

    // Hàm xử lý khi nhấn Cancel: hủy chỉnh sửa, khôi phục tên cũ
    const handleCancel = () => {
        setEditName(author.name);
        setIsEditing(false);
    };

    return (
        // Container chính của thẻ tác giả
        <div className="bg-white shadow-md rounded-lg p-4 flex flex-col justify-between h-full">
            <div>
                {/* Hiển thị ID của tác giả */}
                <h3 className="text-lg font-semibold text-purple-700">ID: {author.id}</h3>

                {/* Nếu đang chỉnh sửa thì hiển thị input, ngược lại hiển thị tên */}
                {isEditing ? (
                    <input
                        type="text"
                        className="border border-purple-300 rounded px-2 py-1 mt-1 w-full"
                        value={editName}
                        onChange={(e) => setEditName(e.target.value)}
                        onKeyDown={(e) => {
                            // Nhấn Enter để lưu
                            if (e.key === "Enter") {
                                e.preventDefault();
                                handleSave();
                            }
                            // Nhấn Escape để hủy
                            if (e.key === "Escape") {
                                e.preventDefault();
                                handleCancel();
                            }
                        }}
                        autoFocus // Tự động focus vào input khi mở chế độ edit
                    />
                ) : (
                    <p className="text-lg font-semibold text-purple-700 mt-1">Name: {author.name}</p>
                )}
            </div>

            {/* Các nút chức năng */}
            <div className="mt-4 flex space-x-2">
                {/* Nút View (chưa có hành động thực sự) */}
                <button className="bg-blue-500 text-white py-1 px-3 rounded hover:bg-blue-600">View</button>

                {/* Nếu đang edit thì hiển thị nút Save/Cancel, ngược lại hiển thị nút Edit */}
                {isEditing ? (
                    <>
                        <button
                            onClick={handleSave}
                            className="bg-green-500 text-white py-1 px-3 rounded hover:bg-green-600"
                        >
                            Save
                        </button>
                        <button
                            onClick={handleCancel}
                            className="bg-gray-400 text-white py-1 px-3 rounded hover:bg-gray-500"
                        >
                            Cancel
                        </button>
                    </>
                ) : (
                    <button
                        onClick={() => setIsEditing(true)}
                        className="bg-yellow-500 text-white py-1 px-3 rounded hover:bg-yellow-600"
                    >
                        Edit
                    </button>
                )}

                {/* Nút Delete (hiện chưa có sự kiện xử lý) */}
                <button className="bg-red-500 text-white py-1 px-3 rounded hover:bg-red-600">Delete</button>
            </div>
        </div>
    );
}
