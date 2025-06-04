// Trang quản lý khoản phạt (Fines) 
import { useState } from "react";
import AdminNavbar from "../../components/AdminNavbar";
import fines from "../../data/fines";  // Dữ liệu phạt
import users from "../../data/users";  // Dữ liệu người dùng

export default function FineManage() {
    // State lưu chuỗi tìm kiếm
    const [search, setSearch] = useState("");
    // State lưu id của bản ghi đang được chỉnh sửa (null nếu không có)
    const [editId, setEditId] = useState<number | null>(null);
    // State lưu số tiền phạt đang chỉnh sửa
    const [editAmount, setEditAmount] = useState<number>(0);

    // Kết hợp dữ liệu phạt với tên người dùng tương ứng theo user_id
    const enrichedFines = fines.map((fine) => {
        const user = users.find((u) => u.id === fine.user_id);
        return {
            ...fine,
            username: user?.userName || "Unknown", // Nếu không tìm thấy user thì hiển thị "Unknown"
        };
    });

    // Lọc dữ liệu fines dựa trên search (theo username hoặc book_loan_id)
    const filteredFines = enrichedFines.filter((fine) => {
        const lowerSearch = search.toLowerCase();
        return (
            fine.username.toLowerCase().includes(lowerSearch) ||
            String(fine.book_loan_id).includes(lowerSearch)
        );
    });

    // Hàm xử lý lưu chỉnh sửa tiền phạt (hiện chỉ log ra console)
    const handleSave = (id: number) => {
        console.log("Save fine", id, "new amount:", editAmount);
        // TODO: tại đây có thể gọi API cập nhật backend hoặc cập nhật state nếu cần

        setEditId(null); // Sau khi lưu thì thoát chế độ chỉnh sửa
    };

    // Hủy bỏ chỉnh sửa, quay về trạng thái xem bình thường
    const handleCancel = () => {
        setEditId(null);
    };

    // Bắt đầu chế độ chỉnh sửa cho dòng fine được chọn
    const startEdit = (fine: typeof fines[0]) => {
        setEditId(fine.id);       // Đánh dấu dòng đang sửa
        setEditAmount(fine.amount); // Khởi tạo giá trị editAmount bằng giá trị hiện tại
    };

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
                            placeholder="Search by Username or Book Loan ID..."
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
                                    <th className="py-3 px-4">Created At</th>
                                    <th className="py-3 px-4">Updated At</th>
                                    <th className="py-3 px-4">Action</th>
                                </tr>
                            </thead>
                            <tbody>
                                {filteredFines.length === 0 ? (
                                    // Hiển thị khi không có kết quả tìm kiếm
                                    <tr>
                                        <td colSpan={6} className="py-6 text-gray-500">
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
                                            <td className="py-3 px-4">{fine.book_loan_id}</td>
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
                                                        onKeyDown={(e) => {
                                                            // Phím Enter sẽ lưu, Escape sẽ hủy
                                                            if (e.key === "Enter") {
                                                                handleSave(fine.id);
                                                            }
                                                            if (e.key === "Escape") {
                                                                handleCancel();
                                                            }
                                                        }}
                                                        autoFocus // Tự động focus input khi hiển thị
                                                    />
                                                ) : (
                                                    // Nếu không chỉnh sửa thì hiển thị tiền phạt dạng text
                                                    fine.amount.toLocaleString() + " VND"
                                                )}
                                            </td>
                                            <td className="py-3 px-4 text-sm text-gray-600">
                                                {/* Định dạng ngày tạo */}
                                                {new Date(fine.createdAt).toLocaleString()}
                                            </td>
                                            <td className="py-3 px-4 text-sm text-gray-600">
                                                {/* Định dạng ngày cập nhật */}
                                                {new Date(fine.updatedAt).toLocaleString()}
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
                                                        <button className="bg-red-500 text-white px-3 py-1 rounded hover:bg-red-600">
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
