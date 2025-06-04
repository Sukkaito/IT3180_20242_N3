export type LoanStatus =
    | "REJECTED"
    | "REQUEST_BORROWING"
    | "BORROWED"
    | "REQUEST_RETURNING"
    | "RETURNED"
    | "NONRETURNABLE";

export interface BookLoan {
    id: number;
    book_copy_id: number;
    user_id: string;
    loan_date: string;           // ISO string format
    return_date: string;         // ISO string format
    actual_return_date: string | null;
    status: LoanStatus;
    current_book_request_id: number | null;
    loaned_at: string;           // ISO string format
    updated_at: string;          // ISO string format
}

const bookLoans: BookLoan[] = [
    {
        id: 1,
        book_copy_id: 1,
        user_id: "b24d5066-6321-4de8-af43-9a852d55a0a6",
        loan_date: new Date(Date.now() - 10 * 24 * 60 * 60 * 1000).toISOString(),
        return_date: new Date(Date.now() + 20 * 24 * 60 * 60 * 1000).toISOString(),
        actual_return_date: null,
        status: "BORROWED",
        current_book_request_id: null,
        loaned_at: new Date(Date.now() - 10 * 24 * 60 * 60 * 1000).toISOString(),
        updated_at: new Date(Date.now() - 10 * 24 * 60 * 60 * 1000).toISOString(),
    },
    {
        id: 2,
        book_copy_id: 2,
        user_id: "c9b5f975-43c0-42f5-9b5e-ed62a4f935d1",
        loan_date: new Date(Date.now() - 5 * 24 * 60 * 60 * 1000).toISOString(),
        return_date: new Date(Date.now() + 25 * 24 * 60 * 60 * 1000).toISOString(),
        actual_return_date: null,
        status: "BORROWED",
        current_book_request_id: null,
        loaned_at: new Date(Date.now() - 5 * 24 * 60 * 60 * 1000).toISOString(),
        updated_at: new Date(Date.now() - 5 * 24 * 60 * 60 * 1000).toISOString(),
    },
    {
        id: 3,
        book_copy_id: 3,
        user_id: "b24d5066-6321-4de8-af43-9a852d55a0a6",
        loan_date: new Date(Date.now() - 30 * 24 * 60 * 60 * 1000).toISOString(),
        return_date: new Date().toISOString(),
        actual_return_date: new Date(Date.now() - 2 * 24 * 60 * 60 * 1000).toISOString(),
        status: "RETURNED",
        current_book_request_id: null,
        loaned_at: new Date(Date.now() - 30 * 24 * 60 * 60 * 1000).toISOString(),
        updated_at: new Date(Date.now() - 2 * 24 * 60 * 60 * 1000).toISOString(),
    },
    {
        id: 4,
        book_copy_id: 4,
        user_id: "c9b5f975-43c0-42f5-9b5e-ed62a4f935d1",
        loan_date: new Date(Date.now() - 15 * 24 * 60 * 60 * 1000).toISOString(),
        return_date: new Date(Date.now() + 15 * 24 * 60 * 60 * 1000).toISOString(),
        actual_return_date: null,
        status: "REQUEST_RETURNING",
        current_book_request_id: null,
        loaned_at: new Date(Date.now() - 15 * 24 * 60 * 60 * 1000).toISOString(),
        updated_at: new Date().toISOString(),
    },
];

export default bookLoans;
