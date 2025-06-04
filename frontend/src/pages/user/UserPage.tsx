import { useState } from "react";
import UserNavbar from "../../components/UserNavbar";

interface Book {
  id: number;
  title: string;
  author: string;
  availableCopies: number;
  totalCopies: number;
}

// Mảng tĩnh giả lập dữ liệu sách
const bookList: Book[] = [
  {
    id: 1,
    title: "Clean Code",
    author: "Robert C. Martin",
    availableCopies: 2,
    totalCopies: 5,
  },
  {
    id: 2,
    title: "The Pragmatic Programmer",
    author: "Andrew Hunt",
    availableCopies: 0,
    totalCopies: 3,
  },
  {
    id: 3,
    title: "Introduction to Algorithms",
    author: "Thomas H. Cormen",
    availableCopies: 1,
    totalCopies: 4,
  },
];

export default function BookListPage() {
  const [selectedBook, setSelectedBook] = useState<Book | null>(null);
  const [notification, setNotification] = useState<string | null>(null);

  const handleBorrow = (book: Book) => {
    if (book.availableCopies === 0) return;
    setSelectedBook(book);
  };

  const confirmBorrow = () => {
    setNotification(`Successfully requested to borrow "${selectedBook?.title}"`);
    setSelectedBook(null);
  };

  const closePopup = () => {
    setSelectedBook(null);
  };

  return (
    <>
      <title>Book List</title>
      <UserNavbar selected="home" />
      <div className="p-6 bg-gray-100 min-h-screen">
        <h1 className="text-2xl font-bold mb-4 text-blue-700">Available Books</h1>

        {notification && (
          <div className="mb-4 p-4 bg-green-100 text-green-700 rounded">
            {notification}
          </div>
        )}

        <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-4">
          {bookList.map((book) => (
            <div key={book.id} className="bg-white p-4 rounded shadow">
              <h2 className="text-xl font-semibold">{book.title}</h2>
              <p className="text-gray-600">Author: {book.author}</p>
              <p className="text-gray-700">
                {book.availableCopies > 0
                  ? `${book.availableCopies} copy(ies) available`
                  : "No copies available"}
              </p>

              <button
                className={`mt-3 px-4 py-2 rounded ${
                  book.availableCopies > 0
                    ? "bg-blue-600 text-white hover:bg-blue-700"
                    : "bg-gray-400 text-white cursor-not-allowed"
                }`}
                disabled={book.availableCopies === 0}
                onClick={() => handleBorrow(book)}
              >
                Borrow
              </button>
            </div>
          ))}
        </div>

        {/* Popup Borrow Form */}
        {selectedBook && (
          <div className="fixed inset-0 bg-black bg-opacity-40 flex items-center justify-center z-50">
            <div className="bg-white p-6 rounded-lg shadow-lg w-full max-w-md">
              <h2 className="text-xl font-bold mb-4 text-blue-700">Borrow Book</h2>
              <p className="mb-2">Title: {selectedBook.title}</p>
              <p className="mb-4">Author: {selectedBook.author}</p>

              {/* Form có thể mở rộng thêm nếu cần nhập thông tin */}
              <button
                className="bg-green-600 text-white px-4 py-2 rounded hover:bg-green-700 mr-2"
                onClick={confirmBorrow}
              >
                Confirm Borrow
              </button>
              <button
                className="bg-gray-400 text-white px-4 py-2 rounded hover:bg-gray-500"
                onClick={closePopup}
              >
                Cancel
              </button>
            </div>
          </div>
        )}
      </div>
    </>
  );
}
