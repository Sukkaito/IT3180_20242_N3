// Trang quản lý thể loại (Categories)
// tương tự như trang quản lý tác giả
import { useState, useEffect } from "react";
import AdminNavbar from "../../components/AdminNavbar.tsx";
import AdminCategory from "../../components/AdminCategory.tsx";
import { Category } from "../../data/categories.ts";
import { categoryService } from "../../services/categoryService.ts";

// Local storage key
const CATEGORIES_STORAGE_KEY = "library_categories";

export default function CategoryManage() {
    const [search, setSearch] = useState("");
    const [newCategoryName, setNewCategoryName] = useState("");
    const [categories, setCategories] = useState<Category[]>([]);
    const [loading, setLoading] = useState(true);

    // Load categories from API or local storage
    useEffect(() => {
        const fetchCategories = async () => {
            try {
                setLoading(true);
                // Try to load from local storage first
                const storedCategories = localStorage.getItem(CATEGORIES_STORAGE_KEY);
                
                if (storedCategories) {
                    // If we have data in local storage, use it first (for instant loading)
                    setCategories(JSON.parse(storedCategories));
                    setLoading(false);
                }
                
                // Then try to fetch from API (for up-to-date data)
                try {
                    const data = await categoryService.getAll();
                    setCategories(data);
                    // Save to local storage
                    localStorage.setItem(CATEGORIES_STORAGE_KEY, JSON.stringify(data));
                } catch (error) {
                    console.error("Error fetching categories from API:", error);
                    // If we already loaded from localStorage, we can continue with that data
                    if (!storedCategories) {
                        throw error; // Re-throw if we don't have any data
                    }
                }
            } catch (error) {
                console.error("Error loading categories:", error);
            } finally {
                setLoading(false);
            }
        };

        fetchCategories();

        // Set up the interval for refreshing - optional
        const intervalId = setInterval(() => {
            fetchCategories();
        }, 60000); // Refresh every minute

        // Clean up the interval on component unmount
        return () => clearInterval(intervalId);
    }, []);

    const filteredCategories = categories.filter((category) => {
        const lowerSearch = search.toLowerCase();
        const idMatch = category.id.toString().includes(lowerSearch);
        const nameMatch = category.name.toLowerCase().includes(lowerSearch);
        return idMatch || nameMatch;
    });

    const handleAddCategory = async (name: string) => {
        if (!name.trim()) return;
        
        try {
            setLoading(true);
            // Create a new category object with temporary ID
            const tempId = Date.now(); // Use timestamp as temporary ID
            const newCategory: Category = {
                id: tempId,
                name: name.trim()
            };
            
            // Optimistically update UI
            const updatedCategories = [...categories, newCategory];
            setCategories(updatedCategories);
            localStorage.setItem(CATEGORIES_STORAGE_KEY, JSON.stringify(updatedCategories));
            
            // Reset input field
            setNewCategoryName("");
            
            // Make API call
            try {
                await categoryService.create({ name: name.trim() });
                console.log("Add category:", name);
                
                // Fetch the updated list with proper IDs from server
                const freshData = await categoryService.getAll();
                setCategories(freshData);
                localStorage.setItem(CATEGORIES_STORAGE_KEY, JSON.stringify(freshData));
            } catch (error) {
                console.error("Error adding category:", error);
                // Revert optimistic update on error
                const revertedCategories = categories.filter(c => c.id !== tempId);
                setCategories(revertedCategories);
                localStorage.setItem(CATEGORIES_STORAGE_KEY, JSON.stringify(revertedCategories));
            }
        } finally {
            setLoading(false);
        }
    };

    const handleUpdateCategory = async (id: number, newName: string) => {
        if (!newName.trim()) return;
        
        try {
            setLoading(true);
            // Optimistically update UI
            const updatedCategories = categories.map(cat => 
                cat.id === id ? { ...cat, name: newName.trim() } : cat
            );
            setCategories(updatedCategories);
            localStorage.setItem(CATEGORIES_STORAGE_KEY, JSON.stringify(updatedCategories));
            
            // Make API call
            try {
                await categoryService.update(id.toString(), { name: newName.trim() });
                console.log("Update category ID:", id, "with new name:", newName);
            } catch (error) {
                console.error("Error updating category:", error);
                // On error, revert the optimistic update
                const revertedCategories = await categoryService.getAll();
                setCategories(revertedCategories);
                localStorage.setItem(CATEGORIES_STORAGE_KEY, JSON.stringify(revertedCategories));
            }
        } finally {
            setLoading(false);
        }
    };

    const handleDeleteCategory = async (id: number) => {
        try {
            setLoading(true);
            // Store the original category list in case we need to revert
            const originalCategories = [...categories];
            
            // Optimistically update UI
            const updatedCategories = categories.filter(cat => cat.id !== id);
            setCategories(updatedCategories);
            localStorage.setItem(CATEGORIES_STORAGE_KEY, JSON.stringify(updatedCategories));
            
            // Make API call
            try {
                await categoryService.delete(id.toString());
                console.log("Delete category ID:", id);
            } catch (error) {
                console.error("Error deleting category:", error);
                // Revert on error
                setCategories(originalCategories);
                localStorage.setItem(CATEGORIES_STORAGE_KEY, JSON.stringify(originalCategories));
            }
        } finally {
            setLoading(false);
        }
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

                {/* Loading state */}
                {loading && (
                    <div className="text-center py-4">
                        <p className="text-purple-600">Loading categories...</p>
                    </div>
                )}

                <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 gap-4 px-4 pb-10">
                    {filteredCategories.map((category) => (
                        <AdminCategory 
                            key={category.id} 
                            category={category} 
                            onUpdate={handleUpdateCategory}
                            onDelete={handleDeleteCategory}
                        />
                    ))}
                    
                    {!loading && filteredCategories.length === 0 && (
                        <div className="col-span-full text-center py-4">
                            <p className="text-gray-500">No categories found</p>
                        </div>
                    )}
                </div>
            </div>
        </>
    );
}
