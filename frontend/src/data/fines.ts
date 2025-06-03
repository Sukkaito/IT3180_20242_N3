export interface Fine {
    id: string;
    amount: number;
    description: string;
    username: string;
    bookLoanId: string; // Changed to camelCase to match DTO from backend
    createdAt: string;
    updatedAt: string;
}

const fines: Fine[] = [
    {
        id: "fine-1",
        amount: 15000,
        description: "Late return penalty",
        username: "johndoe",
        bookLoanId: "3", // Updated to use the camelCase property name
        createdAt: new Date(Date.now() - 2 * 24 * 60 * 60 * 1000).toISOString(),
        updatedAt: new Date(Date.now() - 2 * 24 * 60 * 60 * 1000).toISOString(),
    },
    {
        id: "fine-2",
        amount: 5000,
        description: "Book damage",
        username: "janedoe",
        bookLoanId: "2", // Updated to use the camelCase property name
        createdAt: new Date(Date.now() - 1 * 24 * 60 * 60 * 1000).toISOString(),
        updatedAt: new Date(Date.now() - 1 * 24 * 60 * 60 * 1000).toISOString(),
    }
];

export default fines;
