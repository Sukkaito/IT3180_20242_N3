import { useState, useEffect } from "react";
import { Author } from "../data/authors";
import { Category } from "../data/categories";
import { Publisher } from "../data/publishers";

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
    onClose: () => void;                  // Hàm đóng modal
    onSubmit: (data: BookFormData) => void; // Gửi dữ liệu khi submit
    initialData?: BookFormData | null;    // Dữ liệu sách hiện tại nếu đang edit
    authors: Author[];                    // Danh sách tác giả để chọn
    categories: Category[];               // Danh sách danh mục để chọn
    publishers: Publisher[];              // Danh sách nhà xuất bản để chọn
}

export default function BookFormModal({ 
    isOpen, 
    onClose, 
    onSubmit, 
    initialData,
    authors,
    categories,
    publishers 
}: BookFormModalProps) {
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

    // Handle author selection (multi-select)
    const handleAuthorChange = (e: React.ChangeEvent<HTMLSelectElement>) => {
        const selectedOptions = Array.from(e.target.selectedOptions, option => parseInt(option.value));
        setAuthorIds(selectedOptions);
    };

    // Handle category selection (multi-select)
    const handleCategoryChange = (e: React.ChangeEvent<HTMLSelectElement>) => {
        const selectedOptions = Array.from(e.target.selectedOptions, option => parseInt(option.value));
        setCategoryIds(selectedOptions);
    };

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
                className="bg-white rounded-lg shadow-lg p-6 w-full max-w-lg space-y-4 max-h-[90vh] overflow-y-auto"
            >
                {/* Tiêu đề modal */}
                <h3 className="text-xl font-semibold text-purple-700 mb-4">
                    {initialData ? "Edit Book" : "Add New Book"}
                </h3>

                {/* Nhập tiêu đề sách */}
                <div>
                    <label className="block text-sm font-medium text-gray-700 mb-1">Title</label>
                    <input
                        type="text"
                        className="w-full border border-gray-300 rounded px-3 py-2"
                        value={title}
                        onChange={(e) => setTitle(e.target.value)}
                        required
                    />
                </div>

                {/* Nhập mô tả */}
                <div>
                    <label className="block text-sm font-medium text-gray-700 mb-1">Description</label>
                    <textarea
                        className="w-full border border-gray-300 rounded px-3 py-2"
                        value={description}
                        onChange={(e) => setDescription(e.target.value)}
                        rows={3}
                    />
                </div>

                {/* Chọn tác giả (multi-select) */}
                <div>
                    <label className="block text-sm font-medium text-gray-700 mb-1">Authors</label>
                    <select 
                        multiple
                        className="w-full border border-gray-300 rounded px-3 py-2"
                        value={authorIds.map(String)}
                        onChange={handleAuthorChange}
                        size={4}
                    >
                        {authors.map(author => (
                            <option key={author.id} value={author.id}>
                                {author.name}
                            </option>
                        ))}
                    </select>
                    <p className="text-xs text-gray-500 mt-1">Hold Ctrl/Cmd to select multiple authors</p>
                </div>

                {/* Chọn danh mục (multi-select) */}
                <div>
                    <label className="block text-sm font-medium text-gray-700 mb-1">Categories</label>
                    <select 
                        multiple
                        className="w-full border border-gray-300 rounded px-3 py-2"
                        value={categoryIds.map(String)}
                        onChange={handleCategoryChange}
                        size={4}
                    >
                        {categories.map(category => (
                            <option key={category.id} value={category.id}>
                                {category.name}
                            </option>
                        ))}
                    </select>
                    <p className="text-xs text-gray-500 mt-1">Hold Ctrl/Cmd to select multiple categories</p>
                </div>

                {/* Chọn nhà xuất bản (single select) */}
                <div>
                    <label className="block text-sm font-medium text-gray-700 mb-1">Publisher</label>
                    <select
                        className="w-full border border-gray-300 rounded px-3 py-2"
                        value={publisherId ?? ""}
                        onChange={(e) => setPublisherId(e.target.value ? parseInt(e.target.value) : null)}
                    >
                        <option value="">Select a publisher</option>
                        {publishers.map(publisher => (
                            <option key={publisher.id} value={publisher.id}>
                                {publisher.name}
                            </option>
                        ))}
                    </select>
                </div>

                {/* Nút Cancel và Save */}
                <div className="flex justify-end space-x-3 pt-4">
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
                        disabled={!title.trim()}
                    >
                        Save
                    </button>
                </div>
            </form>
        </div>
    );
}
