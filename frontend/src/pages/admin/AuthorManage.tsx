// Trang quản lý tác giả (Authors)
import { useState, useEffect } from "react";
import AdminNavbar from "../../components/AdminNavbar.tsx";
import AdminAuthor from "../../components/AdminAuthor.tsx";
import { Author } from "../../data/authors.ts";
import { AuthorService } from "../../services/authorService.ts";

// Local storage key
const AUTHORS_STORAGE_KEY = "library_authors";

// Trang quản lý tác giả (Authors)
export default function AuthorManage() {
    // State cho ô tìm kiếm
    const [search, setSearch] = useState("");
    // State cho input thêm tên tác giả mới
    const [newAuthorName, setNewAuthorName] = useState("");
    // State for authors
    const [authors, setAuthors] = useState<Author[]>([]);
    // Loading state
    const [loading, setLoading] = useState(true);

    // Load authors from API or local storage
    useEffect(() => {
        const fetchAuthors = async () => {
            try {
                setLoading(true);
                // Try to load from local storage first
                const storedAuthors = localStorage.getItem(AUTHORS_STORAGE_KEY);
                
                if (storedAuthors) {
                    // If we have data in local storage, use it first (for instant loading)
                    setAuthors(JSON.parse(storedAuthors));
                    setLoading(false);
                }
                
                // Then try to fetch from API (for up-to-date data)
                try {
                    const data = await AuthorService.getAll();
                    setAuthors(data);
                    // Save to local storage
                    localStorage.setItem(AUTHORS_STORAGE_KEY, JSON.stringify(data));
                } catch (error) {
                    console.error("Error fetching authors from API:", error);
                    // If we already loaded from localStorage, we can continue with that data
                    if (!storedAuthors) {
                        throw error; // Re-throw if we don't have any data
                    }
                }
            } catch (error) {
                console.error("Error loading authors:", error);
            } finally {
                setLoading(false);
            }
        };

        fetchAuthors();

        // Set up the interval for refreshing - optional
        const intervalId = setInterval(() => {
            fetchAuthors();
        }, 60000); // Refresh every minute

        // Clean up the interval on component unmount
        return () => clearInterval(intervalId);
    }, []);

    // Lọc danh sách tác giả theo từ khóa tìm kiếm (ID hoặc tên)
    const filteredAuthors = authors.filter((author) => {
        const lowerSearch = search.toLowerCase();
        const idMatch = author.id.toString().includes(lowerSearch);
        const nameMatch = author.name.toLowerCase().includes(lowerSearch);
        return idMatch || nameMatch;
    });

    // Xử lý khi người dùng thêm tác giả mới
const handleAddAuthor = async (name: string) => {
    if (!name.trim()) return;
    
    try {
        setLoading(true);
        // Create a new author object with temporary ID
        const tempId = Date.now(); // Use timestamp as temporary ID
        const newAuthor: Author = {
            id: tempId,
            name: name.trim()
        };
        
        // Optimistically update UI
        const updatedAuthors = [...authors, newAuthor];
        setAuthors(updatedAuthors);
        localStorage.setItem(AUTHORS_STORAGE_KEY, JSON.stringify(updatedAuthors));
        
        // Reset input field
        setNewAuthorName("");
        
        // Make API call
        try {
            await AuthorService.add(name.trim());
            console.log("Add author:", name);
            
            // Fetch the updated list with proper IDs from server
            const freshData = await AuthorService.getAll();
            setAuthors(freshData);
            localStorage.setItem(AUTHORS_STORAGE_KEY, JSON.stringify(freshData));
        } catch (error) {
            console.error("Error adding author:", error);
            // Revert optimistic update on error
            const revertedAuthors = authors.filter(a => a.id !== tempId);
            setAuthors(revertedAuthors);
            localStorage.setItem(AUTHORS_STORAGE_KEY, JSON.stringify(revertedAuthors));
        }
    } finally {
        setLoading(false);
    }
};

// Xử lý khi người dùng cập nhật tác giả
const handleUpdateAuthor = async (id: number, newName: string) => {
    if (!newName.trim()) return;
    
    try {
        setLoading(true);
        // Optimistically update UI
        const updatedAuthors = authors.map(author => 
            author.id === id ? { ...author, name: newName.trim() } : author
        );
        setAuthors(updatedAuthors);
        localStorage.setItem(AUTHORS_STORAGE_KEY, JSON.stringify(updatedAuthors));
        
        // Make API call
        try {
            await AuthorService.update(id, newName.trim());
            console.log("Update author ID:", id, "with new name:", newName);
        } catch (error) {
            console.error("Error updating author:", error);
            // On error, revert the optimistic update
            const revertedAuthors = await AuthorService.getAll();
            setAuthors(revertedAuthors);
            localStorage.setItem(AUTHORS_STORAGE_KEY, JSON.stringify(revertedAuthors));
        }
    } finally {
        setLoading(false);
    }
};

// Xử lý khi người dùng xóa tác giả
const handleDeleteAuthor = async (id: number) => {
    try {
        setLoading(true);
        // Store the original author list in case we need to revert
        const originalAuthors = [...authors];
        
        // Optimistically update UI
        const updatedAuthors = authors.filter(author => author.id !== id);
        setAuthors(updatedAuthors);
        localStorage.setItem(AUTHORS_STORAGE_KEY, JSON.stringify(updatedAuthors));
        
        // Make API call
        try {
            await AuthorService.delete(id);
            console.log("Delete author ID:", id);
        } catch (error) {
            console.error("Error deleting author:", error);
            // Revert on error
            setAuthors(originalAuthors);
            localStorage.setItem(AUTHORS_STORAGE_KEY, JSON.stringify(originalAuthors));
        }
    } finally {
        setLoading(false);
    }
};

return (
    <>
        {/* Set title cho trang */}
        <title>Manage Authors</title>

        {/* Navbar cho trang admin, đánh dấu tab "authors" được chọn */}
        <AdminNavbar selected="authors" />

        <div className="min-h-screen bg-purple-50">
            <div className="p-4">
                {/* Tiêu đề và mô tả trang */}
                <h2 className="text-2xl font-semibold text-purple-700 mb-2">Manage Authors</h2>
                <p className="text-gray-700 mb-4">
                    View, search, edit or delete authors from the system. Use the filter or add a new author as needed.
                </p>

                {/* Thanh tìm kiếm và form thêm tác giả */}
                <div className="flex flex-col sm:flex-row sm:items-center sm:justify-between mb-4 gap-2">
                    {/* Input tìm kiếm */}
                    <input
                        type="text"
                        placeholder="Search by ID or Name..."
                        className="border border-purple-300 rounded px-3 py-2 w-full sm:w-1/2"
                        value={search}
                        onChange={(e) => setSearch(e.target.value)}
                    />

                    {/* Input + nút thêm tác giả mới */}
                    <div className="flex gap-2 w-full sm:w-auto">
                        <input
                            type="text"
                            placeholder="New author name"
                            className="border border-purple-300 rounded px-3 py-2 flex-grow"
                            value={newAuthorName}
                            onChange={(e) => setNewAuthorName(e.target.value)}
                            onKeyDown={(e) => {
                                if (e.key === "Enter") {
                                    e.preventDefault();
                                    handleAddAuthor(newAuthorName);
                                }
                            }}
                        />
                        <button
                            className="bg-green-500 text-white py-2 px-4 rounded hover:bg-green-600"
                            onClick={() => handleAddAuthor(newAuthorName)}
                        >
                            + Add New Author
                        </button>
                    </div>
                </div>
            </div>

            {/* Loading state */}
            {loading && (
                <div className="text-center py-4">
                    <p className="text-purple-600">Loading authors...</p>
                </div>
            )}

            {/* Hiển thị danh sách tác giả đã lọc */}
            <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 gap-4 px-4 pb-10">
                {filteredAuthors.map((author) => (
                    // Gọi component con để hiển thị từng tác giả
                    <AdminAuthor 
                        key={author.id} 
                        author={author} 
                        onUpdate={handleUpdateAuthor}
                        onDelete={handleDeleteAuthor}
                    />
                ))}
                
                {!loading && filteredAuthors.length === 0 && (
                    <div className="col-span-full text-center py-4">
                        <p className="text-gray-500">No authors found</p>
                    </div>
                )}
            </div>
        </div>
    </>
);
}