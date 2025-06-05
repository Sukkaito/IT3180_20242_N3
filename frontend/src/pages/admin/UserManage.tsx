// Trang quản lý Users
import { useState, useEffect } from "react";
import AdminNavbar from "../../components/AdminNavbar";
import { User } from "../../data/users";
import userService, { USER_ROLES } from "../../services/userService";
import UserFormModal, { UserFormData } from "../../components/UserFormModal";

export default function UserManage() {
    // State for users data
    const [users, setUsers] = useState<User[]>([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState<string | null>(null);
    
    // State để lưu giá trị tìm kiếm người dùng
    const [search, setSearch] = useState("");
    
    // State for role filter
    const [roleFilter, setRoleFilter] = useState<string>("");
    
    // State for user form modal
    const [isModalOpen, setIsModalOpen] = useState(false);
    const [editingUser, setEditingUser] = useState<User | null>(null);

    // Load users on component mount
    useEffect(() => {
        const fetchUsers = async () => {
            try {
                setLoading(true);
                const data = await userService.getAllUsers();
                setUsers(data);
                setError(null);
            } catch (err) {
                console.error("Error fetching users:", err);
                setError("Failed to load users data");
            } finally {
                setLoading(false);
            }
        };

        fetchUsers();
    }, []);

    // Lọc danh sách users dựa trên từ khóa search và vai trò
    const filteredUsers = users.filter((user) => {
        const lowerSearch = search.toLowerCase();
        const matchesSearch = (
            user.name.toLowerCase().includes(lowerSearch) ||
            user.userName.toLowerCase().includes(lowerSearch) ||
            user.email.toLowerCase().includes(lowerSearch)
        );
        
        // Check role filter
        const matchesRole = roleFilter === "" || user.roleName === roleFilter;
        
        return matchesSearch && matchesRole;
    });

    // Handle opening the add user modal
    const handleAddUser = () => {
        setEditingUser(null);
        setIsModalOpen(true);
    };
    
    // Handle opening the edit user modal
    const handleEditUser = (user: User) => {
        setEditingUser(user);
        setIsModalOpen(true);
    };
    
    // Handle closing the user modal
    const handleCloseModal = () => {
        setIsModalOpen(false);
        setEditingUser(null);
    };
    
    // Handle form submission
    const handleUserFormSubmit = async (userData: UserFormData) => {
        try {
            if (userData.id) {
                // Update existing user
                const updatedUser = await userService.updateUser(userData.id, userData);
                setUsers(prev => prev.map(user => 
                    user.id === userData.id ? updatedUser : user
                ));
            } else {
                // Create new user or staff
                let newUser;
                if (userData.roleName === USER_ROLES.USER) {
                    newUser = await userService.createUser(userData);
                } else {
                    newUser = await userService.createStaff(userData as any);
                }
                setUsers(prev => [...prev, newUser]);
            }
            handleCloseModal();
        } catch (error) {
            console.error("Error saving user:", error);
            alert("Failed to save user. Please try again.");
        }
    };

    // Handle deleting a user
    const handleDeleteUser = async (userId: string) => {
        try {
            if (window.confirm("Are you sure you want to delete this user?")) {
                await userService.deleteUser(userId);
                // Update local state after successful deletion
                setUsers(prev => prev.filter(user => user.id !== userId));
            }
        } catch (error) {
            console.error("Error deleting user:", error);
            alert("Failed to delete user. Please try again.");
        }
    };

    // Show loading indicator
    if (loading) {
        return (
            <>
                <AdminNavbar selected="users" />
                <div className="min-h-screen bg-purple-50 p-6">
                    <div className="text-center py-10">
                        <p className="text-purple-600">Loading users data...</p>
                    </div>
                </div>
            </>
        );
    }

    // Show error message
    if (error) {
        return (
            <>
                <AdminNavbar selected="users" />
                <div className="min-h-screen bg-purple-50 p-6">
                    <div className="text-center py-10">
                        <p className="text-red-600">{error}</p>
                        <button 
                            className="mt-4 bg-purple-600 text-white py-2 px-4 rounded hover:bg-purple-700"
                            onClick={() => window.location.reload()}
                        >
                            Retry
                        </button>
                    </div>
                </div>
            </>
        );
    }

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

                    {/* Search and filter controls */}
                    <div className="flex flex-col sm:flex-row sm:items-center sm:justify-between mb-4 gap-2">
                        <input
                            type="text"
                            placeholder="Search by name, username, or email..."
                            className="border border-purple-300 rounded px-3 py-2 w-full sm:w-1/2"
                            value={search}
                            onChange={(e) => setSearch(e.target.value)}
                        />
                        
                        <div className="flex gap-2 w-full sm:w-auto">
                            {/* Role filter dropdown */}
                            <select
                                className="border border-purple-300 rounded px-3 py-2 w-full sm:w-auto"
                                value={roleFilter}
                                onChange={(e) => setRoleFilter(e.target.value)}
                            >
                                <option value="">All Roles</option>
                                <option value={USER_ROLES.ADMIN}>Admin</option>
                                <option value={USER_ROLES.STAFF}>Staff</option>
                                <option value={USER_ROLES.USER}>User</option>
                            </select>
                            
                            {/* Add User button */}
                            <button
                                className="bg-green-500 text-white py-2 px-4 rounded hover:bg-green-600 whitespace-nowrap"
                                onClick={handleAddUser}
                            >
                                + Add User
                            </button>
                        </div>
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
                                    <th className="py-3 px-4">Role</th>
                                    <th className="py-3 px-4">Created At</th>
                                    <th className="py-3 px-4">Updated At</th>
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
                                        {/* Hiển thị vai trò người dùng */}
                                        <td className="py-3 px-4 font-semibold">
                                            <span className={
                                                user.roleName === USER_ROLES.ADMIN ? "text-red-600" :
                                                user.roleName === USER_ROLES.STAFF ? "text-blue-600" :
                                                "text-green-600"
                                            }>
                                                {user.roleName}
                                            </span>
                                        </td>
                                        {/* Format ngày tạo */}
                                        <td className="py-3 px-4 text-sm">{new Date(user.createdAt).toLocaleString()}</td>
                                        {/* Format ngày cập nhật */}
                                        <td className="py-3 px-4 text-sm">{new Date(user.updatedAt).toLocaleString()}</td>
                                        {/* Hành động có thể thực hiện với user */}
                                        <td className="py-3 px-4 space-x-2">
                                            {/* Edit button */}
                                            <button 
                                                className="bg-yellow-500 text-white py-1 px-3 rounded hover:bg-yellow-600 text-sm"
                                                onClick={() => handleEditUser(user)}
                                            >
                                                Edit
                                            </button>
                                            
                                            {/* Only allow deletion of non-admin users */}
                                            {user.roleName !== USER_ROLES.ADMIN && (
                                                <button 
                                                    className="bg-red-500 text-white py-1 px-3 rounded hover:bg-red-600 text-sm"
                                                    onClick={() => handleDeleteUser(user.id)}
                                                >
                                                    Delete
                                                </button>
                                            )}
                                        </td>
                                    </tr>
                                ))}
                                {/* Khi không có user nào phù hợp với tìm kiếm */}
                                {filteredUsers.length === 0 && (
                                    <tr>
                                        <td colSpan={7} className="py-6 text-gray-500">
                                            No users found.
                                        </td>
                                    </tr>
                                )}
                            </tbody>
                        </table>
                    </div>
                </div>
            </div>
            
            {/* User Form Modal */}
            <UserFormModal
                isOpen={isModalOpen}
                onClose={handleCloseModal}
                onSubmit={handleUserFormSubmit}
                initialData={editingUser}
                isAdmin={true} // Allow creating staff users
            />
        </>
    );
}
