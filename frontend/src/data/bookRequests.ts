export enum BookRequestTypeEnum {
    BORROWING = "BORROWING",
    RETURNING = "RETURNING",
}

export enum BookRequestStatusEnum {
    ACCEPTED = "ACCEPTED",
    DENIED = "DENIED",
    PENDING = "PENDING",
}

export interface BookRequest {
    id: string;
    bookLoanId: number; // tham chiếu đến bookLoan.id, chỉ lưu id thay vì object
    status: BookRequestStatusEnum;
    type: BookRequestTypeEnum;
    createdAt: string;  // ISO date string
    updatedAt: string;  // ISO date string
}

// Mình giả lập 4 request mẫu:
const bookRequests: BookRequest[] = [
    {
        id: "req-1",
        bookLoanId: 1,
        status: BookRequestStatusEnum.PENDING,
        type: BookRequestTypeEnum.BORROWING,
        createdAt: new Date(Date.now() - 7 * 24 * 60 * 60 * 1000).toISOString(),  // 7 ngày trước
        updatedAt: new Date(Date.now() - 7 * 24 * 60 * 60 * 1000).toISOString(),
    },
    {
        id: "req-2",
        bookLoanId: 2,
        status: BookRequestStatusEnum.ACCEPTED,
        type: BookRequestTypeEnum.BORROWING,
        createdAt: new Date(Date.now() - 14 * 24 * 60 * 60 * 1000).toISOString(),
        updatedAt: new Date(Date.now() - 13 * 24 * 60 * 60 * 1000).toISOString(),
    },
    {
        id: "req-3",
        bookLoanId: 3,
        status: BookRequestStatusEnum.DENIED,
        type: BookRequestTypeEnum.RETURNING,
        createdAt: new Date(Date.now() - 10 * 24 * 60 * 60 * 1000).toISOString(),
        updatedAt: new Date(Date.now() - 9 * 24 * 60 * 60 * 1000).toISOString(),
    },
    {
        id: "req-4",
        bookLoanId: 4,
        status: BookRequestStatusEnum.PENDING,
        type: BookRequestTypeEnum.RETURNING,
        createdAt: new Date(Date.now() - 3 * 24 * 60 * 60 * 1000).toISOString(),
        updatedAt: new Date(Date.now() - 3 * 24 * 60 * 60 * 1000).toISOString(),
    },
];

export default bookRequests;
