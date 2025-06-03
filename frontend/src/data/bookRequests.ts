export enum BookRequestTypeEnum {
    BORROWING = "BORROWING",
    RETURNING = "RETURNING",
}

export enum BookRequestStatusEnum {
    ACCEPTED = "ACCEPTED",
    DENIED = "DENIED",
    PENDING = "PENDING",
}

// Updated to match backend DTO
export interface BookRequest {
    id: string;
    bookLoanId: string; // Changed to string to match DTO
    username: string;   // Added username to match DTO
    status: BookRequestStatusEnum;
    type: BookRequestTypeEnum;
    createdAt: string;  // ISO date string
    updatedAt: string;  // ISO date string
}

// Updated sample data to match the new interface
const bookRequests: BookRequest[] = [
    {
        id: "req-1",
        bookLoanId: "1",
        username: "johndoe",
        status: BookRequestStatusEnum.PENDING,
        type: BookRequestTypeEnum.BORROWING,
        createdAt: new Date(Date.now() - 7 * 24 * 60 * 60 * 1000).toISOString(),
        updatedAt: new Date(Date.now() - 7 * 24 * 60 * 60 * 1000).toISOString(),
    },
    {
        id: "req-2",
        bookLoanId: "2",
        username: "janedoe",
        status: BookRequestStatusEnum.ACCEPTED,
        type: BookRequestTypeEnum.BORROWING,
        createdAt: new Date(Date.now() - 14 * 24 * 60 * 60 * 1000).toISOString(),
        updatedAt: new Date(Date.now() - 13 * 24 * 60 * 60 * 1000).toISOString(),
    },
    {
        id: "req-3",
        bookLoanId: "3",
        username: "johndoe",
        status: BookRequestStatusEnum.DENIED,
        type: BookRequestTypeEnum.RETURNING,
        createdAt: new Date(Date.now() - 10 * 24 * 60 * 60 * 1000).toISOString(),
        updatedAt: new Date(Date.now() - 9 * 24 * 60 * 60 * 1000).toISOString(),
    },
    {
        id: "req-4",
        bookLoanId: "4",
        username: "janedoe",
        status: BookRequestStatusEnum.PENDING,
        type: BookRequestTypeEnum.RETURNING,
        createdAt: new Date(Date.now() - 3 * 24 * 60 * 60 * 1000).toISOString(),
        updatedAt: new Date(Date.now() - 3 * 24 * 60 * 60 * 1000).toISOString(),
    },
];

export default bookRequests;
