// Trang quản lý UsersUsers
import { useState } from "react";
import AdminNavbar from "../../components/AdminNavbar";
import users from "../../data/users";

export default function UserManage() {
    // State để lưu giá trị tìm kiếm người dùng
    const [search, setSearch] = useState("");

    // Lọc danh sách users dựa trên từ khóa search
    // Tìm theo name, username hoặc email, không phân biệt hoa thường
    const filteredUsers = users.filter((user) => {
        const lowerSearch = search.toLowerCase();
        return (
            user.name.toLowerCase().includes(lowerSearch) ||
            user.userName.toLowerCase().includes(lowerSearch) ||
            user.email.toLowerCase().includes(lowerSearch)
        );
    });

    // Hàm lấy tên vai trò từ roleId
    // roleId = 1 là Admin, các roleId khác là User
    const getRoleName = (roleId: number) => {
        return roleId === 1 ? "Admin" : "User";
    };

    return (
        <>
            <title>Manage Users</title>
            {/* Thanh navbar của admin, tab users được đánh dấu đang chọn */}
            <AdminNavbar selected="users" />

            <div className="min-h-screen bg-purple-50">
                <div className="p-4">
                    {/* Tiêu đề trang */}
                    <h2 className="text-2xl font-semibold text-purple-700 mb-2">Manage Users</h2>
                    {/* Mô tả ngắn về trang */}
                    <p className="text-gray-700 mb-4">
                        View, search, edit or delete users from the system.
                    </p>

                    {/* Input tìm kiếm người dùng */}
                    <div className="flex flex-col sm:flex-row sm:items-center sm:justify-between mb-4 gap-2">
                        <input
                            type="text"
                            placeholder="Search by name, username, or email..."
                            className="border border-purple-300 rounded px-3 py-2 w-full sm:w-1/2"
                            value={search}
                            onChange={(e) => setSearch(e.target.value)}
                        />
                    </div>
                </div>

                {/* Bảng hiển thị danh sách người dùng */}
                <div className="px-4 pb-10">
                    <div className="overflow-x-auto bg-white shadow-lg rounded-2xl">
                        <table className="min-w-full text-center">
                            <thead className="bg-purple-100 text-purple-700">
                                <tr>
                                    {/* Các cột của bảng */}
                                    <th className="py-3 px-4">Name</th>
                                    <th className="py-3 px-4">Username</th>
                                    <th className="py-3 px-4">Email</th>
                                    <th className="py-3 px-4">Password</th>
                                    <th className="py-3 px-4">Created At</th>
                                    <th className="py-3 px-4">Updated At</th>
                                    <th className="py-3 px-4">Role</th>
                                    <th className="py-3 px-4">Actions</th>
                                </tr>
                            </thead>
                            <tbody>
                                {/* Duyệt danh sách users đã lọc, hiển thị từng user */}
                                {filteredUsers.map((user) => (
                                    <tr
                                        key={user.id}
                                        className="border-t hover:bg-purple-50 transition"
                                    >
                                        <td className="py-3 px-4">{user.name}</td>
                                        <td className="py-3 px-4">{user.userName}</td>
                                        <td className="py-3 px-4">{user.email}</td>
                                        {/* Mật khẩu không hiển thị thực, chỉ show dấu chấm */}
                                        <td className="py-3 px-4 text-gray-400">••••••••</td>
                                        {/* Format ngày tạo */}
                                        <td className="py-3 px-4 text-sm">{new Date(user.createdAt).toLocaleString()}</td>
                                        {/* Format ngày cập nhật */}
                                        <td className="py-3 px-4 text-sm">{new Date(user.updatedAt).toLocaleString()}</td>
                                        {/* Hiển thị vai trò người dùng */}
                                        <td className="py-3 px-4 font-semibold">
                                            {getRoleName(user.roleId)}
                                        </td>
                                        {/* Hành động có thể thực hiện với user */}
                                        <td className="py-3 px-4 space-x-2">
                                            <button className="bg-red-500 text-white py-1 px-3 rounded hover:bg-red-600 text-sm">
                                                Delete
                                            </button>
                                        </td>
                                    </tr>
                                ))}
                                {/* Khi không có user nào phù hợp với tìm kiếm */}
                                {filteredUsers.length === 0 && (
                                    <tr>
                                        <td colSpan={8} className="py-6 text-gray-500">
                                            No users found.
                                        </td>
                                    </tr>
                                )}
                            </tbody>
                        </table>
                    </div>
                </div>
            </div>
        </>
    );
}
