import { Book as BookType } from "../data/books";
import { Author as AuthorType } from "../data/authors";
import { Category as CategoryType } from "../data/categories";
import authors from "../data/authors";
import categories from "../data/categories";

export default function Book({ book }: { book: BookType }) {
    return (
        <div className="bg-white shadow-md rounded-lg p-4">
        <h3 className="text-lg font-semibold text-blue-700">{book.title}</h3>
        {<p className="text-gray-500">
            {
                book.authorIds.map((authorId: number) => {
                    const author = authors.find((author: AuthorType) => author.id === authorId);
                    return author ? author.name : "Unknown Author";
                }).join(", ")
            }
        </p>}
        {<p className="text-gray-500">
            {
                book.categoryIds.map((categoryId: number) => {
                    const category = categories.find((category: CategoryType) => category.id === categoryId);
                    return category ? category.name : "Unknown Category";
                }).join(", ")
            }
        </p>}
        <p className="text-gray-500">{book.description}</p>
        <button className="mt-2 bg-blue-500 text-white py-1 px-3 rounded hover:bg-blue-600">Borrow</button>
        </div>
    );
}