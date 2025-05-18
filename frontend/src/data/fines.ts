export interface Fine {
    id: string;
    user_id: string;
    book_loan_id: number;
    amount: number;
    createdAt: string;
    updatedAt: string;
}

const fines: Fine[] = [
    {
        id: "fine-1",
        user_id: "b24d5066-6321-4de8-af43-9a852d55a0a6",
        book_loan_id: 3,
        amount: 15000,
        createdAt: new Date(Date.now() - 2 * 24 * 60 * 60 * 1000).toISOString(),
        updatedAt: new Date(Date.now() - 2 * 24 * 60 * 60 * 1000).toISOString(),
    },
    {
        id: "fine-2",
        user_id: "c9b5f975-43c0-42f5-9b5e-ed62a4f935d1",
        book_loan_id: 2,
        amount: 5000,
        createdAt: new Date(Date.now() - 1 * 24 * 60 * 60 * 1000).toISOString(),
        updatedAt: new Date(Date.now() - 1 * 24 * 60 * 60 * 1000).toISOString(),
    }
];

export default fines;
