import { useState } from "react";
import UserNavbar from "../../components/UserNavbar";

interface Book {
  id: number;
  title: string;
  author: string;
  year: number;
  available: boolean;
}

export default function UserBookSearch() {
  const [query, setQuery] = useState("");
  const [results, setResults] = useState<Book[]>([]);
  const [loading, setLoading] = useState(false);

  const handleSearch = async () => {
    if (!query.trim()) return;

    setLoading(true);
    try {
      const response = await fetch(`/api/books?query=${encodeURIComponent(query)}`);
      const data = await response.json();
      setResults(data);
    } catch (error) {
      console.error("Error fetching books:", error);
    } finally {
      setLoading(false);
    }
  };

  return (
    <>
      <title>Search</title>
      <UserNavbar selected="search" />

      <div className="flex flex-col items-center justify-center h-auto bg-gray-100 pt-5 px-4">
        <h1 className="text-2xl font-bold text-blue-700 mb-4">Search</h1>
        <input
          type="text"
          value={query}
          onChange={(e) => setQuery(e.target.value)}
          placeholder="Search for books, authors, etc."
          className="border border-gray-300 rounded-lg px-4 py-2 w-full max-w-md mb-4"
        />
        <button
          onClick={handleSearch}
          className="bg-blue-500 text-white rounded-lg px-4 py-2 hover:bg-blue-600"
        >
          {loading ? "Searching..." : "Search"}
        </button>

        {/* Kết quả tìm kiếm */}
        <div className="mt-6 w-full max-w-2xl">
          {results.length > 0 && (
            <ul className="space-y-4">
              {results.map((book) => (
                <li key={book.id} className="bg-white p-4 rounded shadow">
                  <h2 className="text-lg font-semibold">{book.title}</h2>
                  <p className="text-gray-700">Author: {book.author}</p>
                  <p className="text-gray-500">Year: {book.year}</p>
                  <p className={book.available ? "text-green-600" : "text-red-600"}>
                    {book.available ? "Available" : "Not Available"}
                  </p>
                </li>
              ))}
            </ul>
          )}
        </div>
      </div>
    </>
  );
}
