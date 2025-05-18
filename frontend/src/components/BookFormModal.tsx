import { useState, useEffect } from "react";

// Định nghĩa kiểu dữ liệu cho form sách
export interface BookFormData {
    id?: number; // Có id thì là edit, không thì là add mới
    title: string;
    description: string;
    authorIds: number[];
    categoryIds: number[];
    publisherId: number | null;
}

// Props cho modal form
interface BookFormModalProps {
    isOpen: boolean;                      // Modal đang mở hay không
    onClose: () => void;                 // Hàm đóng modal
    onSubmit: (data: BookFormData) => void; // Gửi dữ liệu khi submit
    initialData?: BookFormData;          // Dữ liệu sách hiện tại nếu đang edit
}

export default function BookFormModal({ isOpen, onClose, onSubmit, initialData }: BookFormModalProps) {
    // Khởi tạo state với dữ liệu ban đầu nếu có (sửa sách), còn không thì rỗng (thêm mới)
    const [title, setTitle] = useState(initialData?.title || "");
    const [description, setDescription] = useState(initialData?.description || "");
    const [authorIds, setAuthorIds] = useState<number[]>(initialData?.authorIds || []);
    const [categoryIds, setCategoryIds] = useState<number[]>(initialData?.categoryIds || []);
    const [publisherId, setPublisherId] = useState<number | null>(initialData?.publisherId || null);

    // Cập nhật lại state khi mở form hoặc khi dữ liệu sách thay đổi
    useEffect(() => {
        if (isOpen) {
            setTitle(initialData?.title || "");
            setDescription(initialData?.description || "");
            setAuthorIds(initialData?.authorIds || []);
            setCategoryIds(initialData?.categoryIds || []);
            setPublisherId(initialData?.publisherId ?? null);
        }
    }, [initialData, isOpen]);

    // Nếu không mở thì không render gì cả
    if (!isOpen) return null;

    // Xử lý khi submit form
    const handleSubmit = (e: React.FormEvent) => {
        e.preventDefault(); // Ngăn reload trang
        onSubmit({
            id: initialData?.id,
            title,
            description,
            authorIds,
            categoryIds,
            publisherId,
        });
    };

    return (
        // Modal overlay
        <div className="fixed inset-0 bg-black bg-opacity-50 flex justify-center items-center z-50">
            {/* Form container */}
            <form
                onSubmit={handleSubmit}
                className="bg-white rounded-lg shadow-lg p-6 w-full max-w-lg space-y-4"
            >
                {/* Tiêu đề modal */}
                <h3 className="text-xl font-semibold text-purple-700 mb-4">
                    {initialData ? "Edit Book" : "Add New Book"}
                </h3>

                {/* Nhập tiêu đề sách */}
                <input
                    type="text"
                    placeholder="Title"
                    className="w-full border border-gray-300 rounded px-3 py-2"
                    value={title}
                    onChange={(e) => setTitle(e.target.value)}
                    required
                />

                {/* Nhập mô tả */}
                <textarea
                    placeholder="Description"
                    className="w-full border border-gray-300 rounded px-3 py-2"
                    value={description}
                    onChange={(e) => setDescription(e.target.value)}
                />

                {/* Nhập Author IDs (dạng 1,2,3) */}
                <input
                    type="text"
                    placeholder="Author IDs"
                    className="w-full border border-gray-300 rounded px-3 py-2"
                    value={authorIds.join(",")}
                    onChange={(e) =>
                        setAuthorIds(
                            e.target.value
                                .split(",")
                                .map((id) => parseInt(id.trim()))
                                .filter((id) => !isNaN(id))
                        )
                    }
                />

                {/* Nhập Category IDs */}
                <input
                    type="text"
                    placeholder="Category IDs"
                    className="w-full border border-gray-300 rounded px-3 py-2"
                    value={categoryIds.join(",")}
                    onChange={(e) =>
                        setCategoryIds(
                            e.target.value
                                .split(",")
                                .map((id) => parseInt(id.trim()))
                                .filter((id) => !isNaN(id))
                        )
                    }
                />

                {/* Nhập Publisher ID */}
                <input
                    type="number"
                    placeholder="Publisher ID"
                    className="w-full border border-gray-300 rounded px-3 py-2"
                    value={publisherId ?? ""}
                    onChange={(e) => {
                        const val = e.target.value;
                        setPublisherId(val === "" ? null : parseInt(val));
                    }}
                />

                {/* Nút Cancel và Save */}
                <div className="flex justify-end space-x-3">
                    <button
                        type="button"
                        className="px-4 py-2 bg-gray-300 rounded hover:bg-gray-400"
                        onClick={onClose}
                    >
                        Cancel
                    </button>
                    <button
                        type="submit"
                        className="px-4 py-2 bg-purple-600 text-white rounded hover:bg-purple-700"
                    >
                        Save
                    </button>
                </div>
            </form>
        </div>
    );
}
