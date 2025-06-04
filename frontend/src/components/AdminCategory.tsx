// Tương tự AdminAuthor 
import { useState } from "react";
import { Category as CategoryType } from "../data/categories";

interface AdminCategoryProps {
    category: CategoryType;
    onUpdate: (id: number, newName: string) => void; // callback khi lưu tên mới
}

export default function AdminCategory({ category, onUpdate }: AdminCategoryProps) {
    const [isEditing, setIsEditing] = useState(false);
    const [editName, setEditName] = useState(category.name);

    const handleSave = () => {
        if (editName.trim() && editName !== category.name) {
            onUpdate(category.id, editName.trim());
        }
        setIsEditing(false);
    };

    const handleCancel = () => {
        setEditName(category.name);
        setIsEditing(false);
    };

    return (
        <div className="bg-white shadow-md rounded-lg p-4 flex flex-col justify-between h-full">
            <div>
                <h3 className="text-lg font-semibold text-purple-700">ID: {category.id}</h3>

                {isEditing ? (
                    <input
                        type="text"
                        className="border border-purple-300 rounded px-2 py-1 mt-1 w-full"
                        value={editName}
                        onChange={(e) => setEditName(e.target.value)}
                        onKeyDown={(e) => {
                            if (e.key === "Enter") {
                                e.preventDefault();
                                handleSave();
                            }
                            if (e.key === "Escape") {
                                e.preventDefault();
                                handleCancel();
                            }
                        }}
                        autoFocus
                    />
                ) : (
                    <p className="text-lg font-semibold text-purple-700 mt-1">Name: {category.name}</p>
                )}
            </div>

            <div className="mt-4 flex space-x-2">
                <button className="bg-blue-500 text-white py-1 px-3 rounded hover:bg-blue-600">View</button>

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

                <button className="bg-red-500 text-white py-1 px-3 rounded hover:bg-red-600">Delete</button>
            </div>
        </div>
    );
}
