import { Book as BookType } from "../data/books";
import { Author as AuthorType } from "../data/authors";
import { Category as CategoryType } from "../data/categories";
import authors from "../data/authors";
import categories from "../data/categories";

interface BookProps {
    book: BookType;
    onBorrowClick?: (book: BookType) => void;
}

export default function Book({ book, onBorrowClick }: BookProps) {
    const handleBorrowClick = (e: React.MouseEvent) => {
        e.preventDefault();
        if (onBorrowClick) {
            onBorrowClick(book);
        }
    };

    return (
        <div className="bg-white shadow-md rounded-lg p-4 hover:shadow-lg transition">
        <h3 className="text-lg font-semibold text-blue-700">{book.title}</h3>
        <p className="text-gray-500 text-sm">
            {
                book.authorIds.map((authorId: number) => {
                    const author = authors.find((author: AuthorType) => author.id === authorId);
                    return author ? author.name : "Unknown Author";
                }).join(", ")
            }
        </p>
        <p className="text-gray-500 text-sm mb-2">
            {
                book.categoryIds.map((categoryId: number) => {
                    const category = categories.find((category: CategoryType) => category.id === categoryId);
                    return category ? category.name : "Unknown Category";
                }).join(", ")
            }
        </p>
        <p className="text-gray-600 text-sm mb-3 line-clamp-2">{book.description}</p>
        <button 
            className="w-full mt-2 bg-blue-500 text-white py-1.5 px-3 rounded hover:bg-blue-600 transition flex items-center justify-center"
            onClick={handleBorrowClick}
        >
            <svg xmlns="http://www.w3.org/2000/svg" className="h-4 w-4 mr-1" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M12 6v6m0 0v6m0-6h6m-6 0H6" />
            </svg>
            Borrow
        </button>
        </div>
    );
}