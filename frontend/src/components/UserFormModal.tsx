import { useState, useEffect } from "react";
import { USER_ROLES } from "../services/userService";

// User form data interface
export interface UserFormData {
    id?: string;
    name: string;
    userName: string;
    email: string;
    password?: string;
    roleName: string;
}

// Props for the user form modal
interface UserFormModalProps {
    isOpen: boolean;
    onClose: () => void;
    onSubmit: (data: UserFormData) => void;
    initialData?: UserFormData | null;
    isAdmin: boolean; // Determines if admin can create staff members
}

export default function UserFormModal({ 
    isOpen, 
    onClose, 
    onSubmit, 
    initialData,
    isAdmin 
}: UserFormModalProps) {
    // Form state
    const [name, setName] = useState(initialData?.name || "");
    const [userName, setUserName] = useState(initialData?.userName || "");
    const [email, setEmail] = useState(initialData?.email || "");
    const [password, setPassword] = useState("");
    const [roleName, setRoleName] = useState(initialData?.roleName || USER_ROLES.USER);

    // Reset form fields when modal opens or initialData changes
    useEffect(() => {
        if (isOpen) {
            setName(initialData?.name || "");
            setUserName(initialData?.userName || "");
            setEmail(initialData?.email || "");
            setPassword(""); // Always reset password field
            setRoleName(initialData?.roleName || USER_ROLES.USER);
        }
    }, [initialData, isOpen]);

    // Don't render anything if modal is not open
    if (!isOpen) return null;

    // Handle form submission
    const handleSubmit = (e: React.FormEvent) => {
        e.preventDefault();
        
        // Create the user data object
        const userData: UserFormData = {
            id: initialData?.id,
            name,
            userName,
            email,
            roleName
        };
        
        // Only include password if it's set (for new users or password changes)
        if (password) {
            userData.password = password;
        }
        
        onSubmit(userData);
    };

    return (
        // Modal overlay
        <div className="fixed inset-0 bg-black bg-opacity-50 flex justify-center items-center z-50">
            {/* Form container */}
            <form
                onSubmit={handleSubmit}
                className="bg-white rounded-lg shadow-lg p-6 w-full max-w-lg space-y-4"
            >
                {/* Modal title */}
                <h3 className="text-xl font-semibold text-purple-700 mb-4">
                    {initialData ? "Edit User" : "Add New User"}
                </h3>

                {/* Name input */}
                <div>
                    <label className="block text-sm font-medium text-gray-700 mb-1">Full Name</label>
                    <input
                        type="text"
                        placeholder="Full Name"
                        className="w-full border border-gray-300 rounded px-3 py-2"
                        value={name}
                        onChange={(e) => setName(e.target.value)}
                        required
                    />
                </div>

                {/* Username input */}
                <div>
                    <label className="block text-sm font-medium text-gray-700 mb-1">Username</label>
                    <input
                        type="text"
                        placeholder="Username"
                        className="w-full border border-gray-300 rounded px-3 py-2"
                        value={userName}
                        onChange={(e) => setUserName(e.target.value)}
                        required
                    />
                </div>

                {/* Email input */}
                <div>
                    <label className="block text-sm font-medium text-gray-700 mb-1">Email</label>
                    <input
                        type="email"
                        placeholder="Email"
                        className="w-full border border-gray-300 rounded px-3 py-2"
                        value={email}
                        onChange={(e) => setEmail(e.target.value)}
                        required
                    />
                </div>

                {/* Password input - only required for new users */}
                <div>
                    <label className="block text-sm font-medium text-gray-700 mb-1">
                        Password {initialData ? "(leave blank to keep current)" : ""}
                    </label>
                    <input
                        type="password"
                        placeholder={initialData ? "New Password (optional)" : "Password"}
                        className="w-full border border-gray-300 rounded px-3 py-2"
                        value={password}
                        onChange={(e) => setPassword(e.target.value)}
                        required={!initialData} // Only required for new users
                    />
                </div>

                {/* Role selection - only shown for admin users */}
                {isAdmin && (
                    <div>
                        <label className="block text-sm font-medium text-gray-700 mb-1">Role</label>
                        <select
                            className="w-full border border-gray-300 rounded px-3 py-2"
                            value={roleName}
                            onChange={(e) => setRoleName(e.target.value)}
                        >
                            <option value={USER_ROLES.USER}>User</option>
                            <option value={USER_ROLES.STAFF}>Staff</option>
                            <option value={USER_ROLES.ADMIN}>Admin</option>
                        </select>
                    </div>
                )}

                {/* Form buttons */}
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
                        disabled={!name.trim() || !userName.trim() || !email.trim() || (!initialData && !password)}
                    >
                        Save
                    </button>
                </div>
            </form>
        </div>
    );
}
