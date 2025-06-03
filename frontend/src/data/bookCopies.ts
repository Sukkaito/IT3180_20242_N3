export type CopyStatus = "AVAILABLE" | "UNAVAILABLE";

export interface BookCopy {
    id: number;
    originalBookBookId: number; // Changed to camelCase to match DTO from backend
    status: CopyStatus;
}

const bookCopies: BookCopy[] = [
    { id: 1, originalBookBookId: 1, status: "AVAILABLE" },
    { id: 2, originalBookBookId: 2, status: "AVAILABLE" },
    { id: 3, originalBookBookId: 3, status: "AVAILABLE" },
    { id: 4, originalBookBookId: 4, status: "AVAILABLE" },
    { id: 5, originalBookBookId: 1, status: "AVAILABLE" },
    { id: 6, originalBookBookId: 2, status: "AVAILABLE" },
    { id: 7, originalBookBookId: 3, status: "AVAILABLE" },
    { id: 8, originalBookBookId: 4, status: "AVAILABLE" },
];

export default bookCopies;
