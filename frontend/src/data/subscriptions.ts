export interface Subscription {
  id: number;
  title: string;
  // The following fields are for mock data only and will be removed in production
  userId?: string;
  bookId?: number;
  bookCopyId?: number;
  createdAt?: string;
  bookTitle?: string;
}

const subscriptions: Subscription[] = [
  {
    id: 1,
    title: "Clean Code",
    userId: "user1",
    bookId: 1,
    createdAt: "2023-05-10T10:15:00",
    bookTitle: "Clean Code"
  },
  {
    id: 2,
    title: "Design Patterns",
    userId: "user1",
    bookCopyId: 5,
    createdAt: "2023-05-12T14:30:00",
    bookTitle: "Design Patterns"
  }
];

export default subscriptions;
