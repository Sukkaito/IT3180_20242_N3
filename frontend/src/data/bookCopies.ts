export interface BookCopy {
    id: number;
    original_book_book_id: number;
    status: "AVAILABLE" | "UNAVAILABLE";
}

const bookCopies: BookCopy[] = [
    { id: 1, original_book_book_id: 1, status: "AVAILABLE" },
    { id: 2, original_book_book_id: 2, status: "AVAILABLE" },
    { id: 3, original_book_book_id: 3, status: "AVAILABLE" },
    { id: 4, original_book_book_id: 4, status: "AVAILABLE" },
    { id: 5, original_book_book_id: 1, status: "AVAILABLE" },
    { id: 6, original_book_book_id: 2, status: "AVAILABLE" },
    { id: 7, original_book_book_id: 3, status: "AVAILABLE" },
    { id: 8, original_book_book_id: 4, status: "AVAILABLE" },
];

export default bookCopies;
