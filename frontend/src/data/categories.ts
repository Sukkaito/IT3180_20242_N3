// GET /api/categories

export interface Category {
    id: number;
    name: string;
}

const categories: Category[] = [
    {
        "id": 1,
        "name": "Fiction"
    },
    {
        "id": 2,
        "name": "Science"
    },
    {
        "id": 3,
        "name": "History"
    },
    {
        "id": 4,
        "name": "Computer Science"
    }
]

export default categories;