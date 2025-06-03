export type LoanStatus =
    | "REJECTED"
    | "REQUEST_BORROWING"
    | "BORROWED"
    | "REQUEST_RETURNING"
    | "RETURNED"
    | "NONRETURNABLE";

// Updated to match backend DTO
export interface BookLoan {
    id: string;
    bookCopyId: number;
    bookCopyOriginalBookTitle: string;
    userUserName: string;
    loanDate: string;           // ISO string format
    dueDate: string;            // ISO string format
    actualReturnDate: string | null;
    status: LoanStatus;
    loanedAt: string;           // ISO string format
    updatedAt: string;          // ISO string format
}

// Update the default data to match the new interface
const bookLoans: BookLoan[] = [
    {
        id: "1",
        bookCopyId: 1,
        bookCopyOriginalBookTitle: "To Kill a Mockingbird",
        userUserName: "johndoe",
        loanDate: new Date(Date.now() - 10 * 24 * 60 * 60 * 1000).toISOString(),
        dueDate: new Date(Date.now() + 20 * 24 * 60 * 60 * 1000).toISOString(),
        actualReturnDate: null,
        status: "BORROWED",
        loanedAt: new Date(Date.now() - 10 * 24 * 60 * 60 * 1000).toISOString(),
        updatedAt: new Date(Date.now() - 10 * 24 * 60 * 60 * 1000).toISOString(),
    },
    {
        id: "2",
        bookCopyId: 2,
        bookCopyOriginalBookTitle: "1984",
        userUserName: "janedoe",
        loanDate: new Date(Date.now() - 5 * 24 * 60 * 60 * 1000).toISOString(),
        dueDate: new Date(Date.now() + 25 * 24 * 60 * 60 * 1000).toISOString(),
        actualReturnDate: null,
        status: "BORROWED",
        loanedAt: new Date(Date.now() - 5 * 24 * 60 * 60 * 1000).toISOString(),
        updatedAt: new Date(Date.now() - 5 * 24 * 60 * 60 * 1000).toISOString(),
    },
    {
        id: "3",
        bookCopyId: 3,
        bookCopyOriginalBookTitle: "The Great Gatsby",
        userUserName: "johndoe",
        loanDate: new Date(Date.now() - 30 * 24 * 60 * 60 * 1000).toISOString(),
        dueDate: new Date().toISOString(),
        actualReturnDate: new Date(Date.now() - 2 * 24 * 60 * 60 * 1000).toISOString(),
        status: "RETURNED",
        loanedAt: new Date(Date.now() - 30 * 24 * 60 * 60 * 1000).toISOString(),
        updatedAt: new Date(Date.now() - 2 * 24 * 60 * 60 * 1000).toISOString(),
    },
    {
        id: "4",
        bookCopyId: 4,
        bookCopyOriginalBookTitle: "Pride and Prejudice",
        userUserName: "janedoe",
        loanDate: new Date(Date.now() - 15 * 24 * 60 * 60 * 1000).toISOString(),
        dueDate: new Date(Date.now() + 15 * 24 * 60 * 60 * 1000).toISOString(),
        actualReturnDate: null,
        status: "REQUEST_RETURNING",
        loanedAt: new Date(Date.now() - 15 * 24 * 60 * 60 * 1000).toISOString(),
        updatedAt: new Date().toISOString(),
    },
];

export default bookLoans;
