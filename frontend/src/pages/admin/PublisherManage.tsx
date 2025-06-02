// Trang quản lý nhà xuất bản (Publishers)
// Tương tự như trang quản lý tác giả
import { useState } from "react";
import AdminNavbar from "../../components/AdminNavbar.tsx";
import AdminPublisher from "../../components/AdminPublisher.tsx";
import publishers from "../../data/publishers.ts";

export default function PublisherManage() {
    const [search, setSearch] = useState("");
    const [newPublisherName, setNewPublisherName] = useState("");

    const filteredPublishers = publishers.filter((publisher) => {
        const lowerSearch = search.toLowerCase();
        const idMatch = publisher.id.toString().includes(lowerSearch);
        const nameMatch = publisher.name.toLowerCase().includes(lowerSearch);
        return idMatch || nameMatch;
    });

    const handleAddPublisher = (name: string) => {
        if (!name.trim()) return;
        // TODO: Gọi API backend thêm publisher mới
        console.log("Add publisher:", name);
        setNewPublisherName("");
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

                <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 gap-4 px-4 pb-10">
                    {filteredPublishers.map((publisher) => (
                        <AdminPublisher key={publisher.id} publisher={publisher} />
                    ))}
                </div>
            </div>
        </>
    );
}
