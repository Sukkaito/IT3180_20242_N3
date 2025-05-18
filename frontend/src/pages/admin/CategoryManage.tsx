// Trang quản lý thể loại (Categories)
// tương tự như trang quản lý tác giả
import { useState } from "react";
import AdminNavbar from "../../components/AdminNavbar.tsx";
import AdminCategory from "../../components/AdminCategory.tsx";
import categories from "../../data/categories.ts";

export default function CategoryManage() {
    const [search, setSearch] = useState("");
    const [newCategoryName, setNewCategoryName] = useState("");

    const filteredCategories = categories.filter((category) => {
        const lowerSearch = search.toLowerCase();
        const idMatch = category.id.toString().includes(lowerSearch);
        const nameMatch = category.name.toLowerCase().includes(lowerSearch);
        return idMatch || nameMatch;
    });

    const handleAddCategory = (name: string) => {
        if (!name.trim()) return;
        // TODO: Gọi API backend thêm category mới
        console.log("Add category:", name);
        setNewCategoryName("");
    };

    return (
        <>
            <title>Manage Categories</title>
            <AdminNavbar selected="categories" />

            <div className="min-h-screen bg-purple-50">
                <div className="p-4">
                    <h2 className="text-2xl font-semibold text-purple-700 mb-2">Manage Categories</h2>
                    <p className="text-gray-700 mb-4">
                        View, search, edit or delete categories from the system. Use the filter or add a new category as needed.
                    </p>

                    <div className="flex flex-col sm:flex-row sm:items-center sm:justify-between mb-4 gap-2">
                        <input
                            type="text"
                            placeholder="Search by ID or Name..."
                            className="border border-purple-300 rounded px-3 py-2 w-full sm:w-1/2"
                            value={search}
                            onChange={(e) => setSearch(e.target.value)}
                        />

                        <div className="flex gap-2 w-full sm:w-auto">
                            <input
                                type="text"
                                placeholder="New category name"
                                className="border border-purple-300 rounded px-3 py-2 flex-grow"
                                value={newCategoryName}
                                onChange={(e) => setNewCategoryName(e.target.value)}
                                onKeyDown={(e) => {
                                    if (e.key === "Enter") {
                                        e.preventDefault();
                                        handleAddCategory(newCategoryName);
                                    }
                                }}
                            />
                            <button
                                className="bg-green-500 text-white py-2 px-4 rounded hover:bg-green-600"
                                onClick={() => handleAddCategory(newCategoryName)}
                            >
                                + Add New Category
                            </button>
                        </div>
                    </div>
                </div>

                <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 gap-4 px-4 pb-10">
                    {filteredCategories.map((category) => (
                        <AdminCategory key={category.id} category={category} />
                    ))}
                </div>
            </div>
        </>
    );
}
