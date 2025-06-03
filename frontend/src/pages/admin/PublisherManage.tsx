// Trang quản lý nhà xuất bản (Publishers)
// Tương tự như trang quản lý tác giả
import { useState, useEffect } from "react";
import AdminNavbar from "../../components/AdminNavbar.tsx";
import AdminPublisher from "../../components/AdminPublisher.tsx";
import { Publisher } from "../../data/publishers.ts";
import { publisherService } from "../../services/publisherService.ts";

// Local storage key
const PUBLISHERS_STORAGE_KEY = "library_publishers";

export default function PublisherManage() {
    const [search, setSearch] = useState("");
    const [newPublisherName, setNewPublisherName] = useState("");
    const [publishers, setPublishers] = useState<Publisher[]>([]);
    const [loading, setLoading] = useState(true);

    // Load publishers from API or local storage
    useEffect(() => {
        const fetchPublishers = async () => {
            try {
                setLoading(true);
                // Try to load from local storage first
                const storedPublishers = localStorage.getItem(PUBLISHERS_STORAGE_KEY);
                
                if (storedPublishers) {
                    // If we have data in local storage, use it first (for instant loading)
                    setPublishers(JSON.parse(storedPublishers));
                    setLoading(false);
                }
                
                // Then try to fetch from API (for up-to-date data)
                try {
                    const data = await publisherService.getAll();
                    setPublishers(data);
                    // Save to local storage
                    localStorage.setItem(PUBLISHERS_STORAGE_KEY, JSON.stringify(data));
                } catch (error) {
                    console.error("Error fetching publishers from API:", error);
                    // If we already loaded from localStorage, we can continue with that data
                    if (!storedPublishers) {
                        throw error; // Re-throw if we don't have any data
                    }
                }
            } catch (error) {
                console.error("Error loading publishers:", error);
            } finally {
                setLoading(false);
            }
        };

        fetchPublishers();

        // Set up the interval for refreshing - optional, can be removed if not needed
        const intervalId = setInterval(() => {
            fetchPublishers();
        }, 60000); // Refresh every minute

        // Clean up the interval on component unmount
        return () => clearInterval(intervalId);
    }, []);

    const filteredPublishers = publishers.filter((publisher) => {
        const lowerSearch = search.toLowerCase();
        const idMatch = publisher.id.toString().includes(lowerSearch);
        const nameMatch = publisher.name.toLowerCase().includes(lowerSearch);
        return idMatch || nameMatch;
    });

    const handleAddPublisher = async (name: string) => {
        if (!name.trim()) return;
        
        try {
            setLoading(true);
            // Create a new publisher object with temporary ID
            const tempId = Date.now(); // Use timestamp as temporary ID
            const newPublisher: Publisher = {
                id: tempId,
                name: name.trim()
            };
            
            // Optimistically update UI
            const updatedPublishers = [...publishers, newPublisher];
            setPublishers(updatedPublishers);
            localStorage.setItem(PUBLISHERS_STORAGE_KEY, JSON.stringify(updatedPublishers));
            
            // Reset input field
            setNewPublisherName("");
            
            // Make API call
            try {
                await publisherService.create({ name: name.trim() });
                console.log("Add publisher:", name);
                
                // Fetch the updated list with proper IDs from server
                const freshData = await publisherService.getAll();
                setPublishers(freshData);
                localStorage.setItem(PUBLISHERS_STORAGE_KEY, JSON.stringify(freshData));
            } catch (error) {
                console.error("Error adding publisher:", error);
                // Revert optimistic update on error
                const revertedPublishers = publishers.filter(p => p.id !== tempId);
                setPublishers(revertedPublishers);
                localStorage.setItem(PUBLISHERS_STORAGE_KEY, JSON.stringify(revertedPublishers));
            }
        } finally {
            setLoading(false);
        }
    };

    const handleUpdatePublisher = async (id: number, newName: string) => {
        if (!newName.trim()) return;
        
        try {
            setLoading(true);
            // Optimistically update UI
            const updatedPublishers = publishers.map(pub => 
                pub.id === id ? { ...pub, name: newName.trim() } : pub
            );
            setPublishers(updatedPublishers);
            localStorage.setItem(PUBLISHERS_STORAGE_KEY, JSON.stringify(updatedPublishers));
            
            // Make API call
            try {
                await publisherService.update(id.toString(), { name: newName.trim() });
                console.log("Update publisher ID:", id, "with new name:", newName);
            } catch (error) {
                console.error("Error updating publisher:", error);
                // On error, revert the optimistic update
                const revertedPublishers = await publisherService.getAll();
                setPublishers(revertedPublishers);
                localStorage.setItem(PUBLISHERS_STORAGE_KEY, JSON.stringify(revertedPublishers));
            }
        } finally {
            setLoading(false);
        }
    };

    const handleDeletePublisher = async (id: number) => {
        try {
            setLoading(true);
            // Store the original publisher list in case we need to revert
            const originalPublishers = [...publishers];
            
            // Optimistically update UI
            const updatedPublishers = publishers.filter(pub => pub.id !== id);
            setPublishers(updatedPublishers);
            localStorage.setItem(PUBLISHERS_STORAGE_KEY, JSON.stringify(updatedPublishers));
            
            // Make API call
            try {
                await publisherService.delete(id.toString());
                console.log("Delete publisher ID:", id);
            } catch (error) {
                console.error("Error deleting publisher:", error);
                // Revert on error
                setPublishers(originalPublishers);
                localStorage.setItem(PUBLISHERS_STORAGE_KEY, JSON.stringify(originalPublishers));
            }
        } finally {
            setLoading(false);
        }
    };

    return (
        <>
            <title>Manage Publishers</title>
            <AdminNavbar selected="publishers" />

            <div className="min-h-screen bg-purple-50">
                <div className="p-4">
                    <h2 className="text-2xl font-semibold text-purple-700 mb-2">Manage Publishers</h2>
                    <p className="text-gray-700 mb-4">
                        View, search, edit or delete publishers from the system. Use the filter or add a new publisher as needed.
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
                                placeholder="New publisher name"
                                className="border border-purple-300 rounded px-3 py-2 flex-grow"
                                value={newPublisherName}
                                onChange={(e) => setNewPublisherName(e.target.value)}
                                onKeyDown={(e) => {
                                    if (e.key === "Enter") {
                                        e.preventDefault();
                                        handleAddPublisher(newPublisherName);
                                    }
                                }}
                            />
                            <button
                                className="bg-green-500 text-white py-2 px-4 rounded hover:bg-green-600"
                                onClick={() => handleAddPublisher(newPublisherName)}
                            >
                                + Add New Publisher
                            </button>
                        </div>
                    </div>
                </div>

                {/* Loading state */}
                {loading && (
                    <div className="text-center py-4">
                        <p className="text-purple-600">Loading publishers...</p>
                    </div>
                )}

                <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 gap-4 px-4 pb-10">
                    {filteredPublishers.map((publisher) => (
                        <AdminPublisher 
                            key={publisher.id} 
                            publisher={publisher} 
                            onUpdate={handleUpdatePublisher}
                            onDelete={handleDeletePublisher}
                        />
                    ))}
                    
                    {!loading && filteredPublishers.length === 0 && (
                        <div className="col-span-full text-center py-4">
                            <p className="text-gray-500">No publishers found</p>
                        </div>
                    )}
                </div>
            </div>
        </>
    );
}
