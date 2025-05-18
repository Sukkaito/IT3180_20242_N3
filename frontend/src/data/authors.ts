// GET /api/authors

export interface Author {
    id: number;
    name: string;
}

const authors : Author[] = [
    {
        "id": 1,
        "name": "George Orwell"
    },
    {
        "id": 2,
        "name": "J.K. Rowling"
    },
    {
        "id": 3,
        "name": "Robert C. Martin"
    },
    {
        "id": 4,
        "name": "Stephen Hawking"
    }
];

export default authors;