// Trang quản lý khoản phạt (Fines) 
import { useState, useEffect } from "react";
import AdminNavbar from "../../components/AdminNavbar";
import { Fine } from "../../data/fines";
import { FineService } from "../../services/fineService";

export default function FineManage() {
    // State lưu chuỗi tìm kiếm
    const [search, setSearch] = useState("");
    // State lưu id của bản ghi đang được chỉnh sửa (null nếu không có)
    const [editId, setEditId] = useState<string | null>(null);
    // State lưu số tiền phạt đang chỉnh sửa
    const [editAmount, setEditAmount] = useState<number>(0);
    // State lưu mô tả phạt đang chỉnh sửa
    const [editDescription, setEditDescription] = useState<string>("");
    // State to store fines
    const [fines, setFines] = useState<Fine[]>([]);
    // Loading state
    const [loading, setLoading] = useState(true);

    // Load fines on component mount
    useEffect(() => {
        const loadFines = async () => {
            try {
                setLoading(true);
                const data = await FineService.getAll();
                setFines(data);
            } catch (error) {
                console.error("Error loading fines:", error);
            } finally {
                setLoading(false);
            }
        };
        
        loadFines();
    }, []);

    // Lọc dữ liệu fines dựa trên search (theo username hoặc bookLoanId)
    const filteredFines = fines.filter((fine) => {
        const lowerSearch = search.toLowerCase();
        return (
            (fine.username?.toLowerCase() || "").includes(lowerSearch) ||
            (fine.bookLoanId || "").includes(lowerSearch) || // Changed from book_loan_id to bookLoanId
            (fine.description && fine.description.toLowerCase().includes(lowerSearch))
        );
    });

    // Hàm xử lý lưu chỉnh sửa tiền phạt
    const handleSave = async (id: string) => {
        try {
            if (editAmount < 0) {
                alert("Fine amount cannot be negative");
                return;
            }
            
            const updatedFine = await FineService.update(id, {
                amount: editAmount,
                description: editDescription
            });
            
            // Update the fines list with the updated fine
            setFines(prev => prev.map(fine => 
                fine.id === id ? updatedFine : fine
            ));
            
            // Reset edit state
            setEditId(null);
        } catch (error) {
            console.error("Error updating fine:", error);
            alert("Failed to update fine. Please try again.");
        }
    };

    // Hủy bỏ chỉnh sửa, quay về trạng thái xem bình thường
    const handleCancel = () => {
        setEditId(null);
    };

    // Bắt đầu chế độ chỉnh sửa cho dòng fine được chọn
    const startEdit = (fine: Fine) => {
        setEditId(fine.id);                 // Đánh dấu dòng đang sửa
        setEditAmount(fine.amount);         // Khởi tạo giá trị editAmount bằng giá trị hiện tại
        setEditDescription(fine.description || ""); // Set description
    };

    // Handle fine deletion
    const handleDelete = async (id: string) => {
        if (window.confirm("Are you sure you want to delete this fine?")) {
            try {
                await FineService.delete(id);
                // Update the fines list
                setFines(prev => prev.filter(fine => fine.id !== id));
            } catch (error) {
                console.error("Error deleting fine:", error);
                alert("Failed to delete fine. Please try again.");
            }
        }
    };

    // Show loading indicator
    if (loading) {
        return (
            <>
                <AdminNavbar selected="fines" />
                <div className="min-h-screen bg-purple-50 p-6">
                    <div className="text-center py-10">
                        <p className="text-purple-600">Loading fines data...</p>
                    </div>
                </div>
            </>
        );
    }

    return (
        <>
            <title>Manage Fines</title>
            <AdminNavbar selected="fines" /> {/* Thanh điều hướng bên trên */}

            <div className="min-h-screen bg-purple-50">
                <div className="p-4">
                    <h2 className="text-2xl font-semibold text-purple-700 mb-2">
                        Manage Fines
                    </h2>
                    <p className="text-gray-700 mb-4">
                        View, search and manage fines in the system.
                    </p>

                    {/* Input tìm kiếm theo tên user hoặc mã book loan */}
                    <div className="flex flex-col sm:flex-row sm:items-center sm:justify-between mb-4 gap-2">
                        <input
                            type="text"
                            placeholder="Search by Username, Book Loan ID or Description..."
                            className="border border-purple-300 rounded px-3 py-2 w-full sm:w-1/3"
                            value={search}
                            onChange={(e) => setSearch(e.target.value)} // Cập nhật state search khi nhập
                        />
                    </div>
                </div>

                {/* Bảng hiển thị danh sách phạt */}
                <div className="px-4 pb-10">
                    <div className="overflow-x-auto bg-white shadow-lg rounded-2xl">
                        <table className="min-w-full text-center">
                            <thead className="bg-purple-100 text-purple-700">
                                <tr>
                                    <th className="py-3 px-4">Username</th>
                                    <th className="py-3 px-4">Book Loan ID</th>
                                    <th className="py-3 px-4">Amount</th>
                                    <th className="py-3 px-4">Description</th>
                                    <th className="py-3 px-4">Created At</th>
                                    <th className="py-3 px-4">Updated At</th>
                                    <th className="py-3 px-4">Action</th>
                                </tr>
                            </thead>
                            <tbody>
                                {filteredFines.length === 0 ? (
                                    // Hiển thị khi không có kết quả tìm kiếm
                                    <tr>
                                        <td colSpan={7} className="py-6 text-gray-500">
                                            No fines found.
                                        </td>
                                    </tr>
                                ) : (
                                    // Duyệt mảng fines đã lọc và hiển thị từng dòng
                                    filteredFines.map((fine) => (
                                        <tr
                                            key={fine.id}
                                            className="border-t hover:bg-purple-50 transition"
                                        >
                                            <td className="py-3 px-4">{fine.username}</td>
                                            <td className="py-3 px-4">{fine.bookLoanId}</td> {/* Changed from book_loan_id to bookLoanId */}
                                            <td className="py-3 px-4">
                                                {editId === fine.id ? (
                                                    // Nếu đang chỉnh sửa dòng này thì hiển thị input
                                                    <input
                                                        type="number"
                                                        className="border border-purple-300 rounded px-2 py-1 w-24"
                                                        value={editAmount}
                                                        onChange={(e) =>
                                                            setEditAmount(Number(e.target.value))
                                                        }
                                                        min={0}
                                                        autoFocus // Tự động focus input khi hiển thị
                                                    />
                                                ) : (
                                                    // Nếu không chỉnh sửa thì hiển thị tiền phạt dạng text
                                                    fine.amount.toLocaleString() + " VND"
                                                )}
                                            </td>
                                            <td className="py-3 px-4">
                                                {editId === fine.id ? (
                                                    <input
                                                        type="text"
                                                        className="border border-purple-300 rounded px-2 py-1 w-40"
                                                        value={editDescription}
                                                        onChange={(e) => setEditDescription(e.target.value)}
                                                        placeholder="Enter description"
                                                    />
                                                ) : (
                                                    fine.description || "—"
                                                )}
                                            </td>
                                            <td className="py-3 px-4 text-sm text-gray-600">
                                                {/* Định dạng ngày tạo */}
                                                {new Date(fine.createdAt).toLocaleDateString()}
                                            </td>
                                            <td className="py-3 px-4 text-sm text-gray-600">
                                                {/* Định dạng ngày cập nhật */}
                                                {new Date(fine.updatedAt).toLocaleDateString()}
                                            </td>
                                            <td className="py-3 px-4 space-x-2">
                                                {editId === fine.id ? (
                                                    // Nếu đang chỉnh sửa, hiện 2 nút Save và Cancel
                                                    <>
                                                        <button
                                                            onClick={() => handleSave(fine.id)}
                                                            className="bg-green-500 text-white px-3 py-1 rounded hover:bg-green-600"
                                                        >
                                                            Save
                                                        </button>
                                                        <button
                                                            onClick={handleCancel}
                                                            className="bg-gray-400 text-white px-3 py-1 rounded hover:bg-gray-500"
                                                        >
                                                            Cancel
                                                        </button>
                                                    </>
                                                ) : (
                                                    // Nếu không đang chỉnh sửa, hiện 2 nút Edit và Delete
                                                    <>
                                                        <button
                                                            onClick={() => startEdit(fine)}
                                                            className="bg-yellow-500 text-white px-3 py-1 rounded hover:bg-yellow-600"
                                                        >
                                                            Edit
                                                        </button>
                                                        <button 
                                                            onClick={() => handleDelete(fine.id)}
                                                            className="bg-red-500 text-white px-3 py-1 rounded hover:bg-red-600"
                                                        >
                                                            Delete
                                                        </button>
                                                    </>
                                                )}
                                            </td>
                                        </tr>
                                    ))
                                )}
                            </tbody>
                        </table>
                    </div>
                </div>
            </div>
        </>
    );
}
