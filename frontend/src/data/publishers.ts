// GET /api/publishers

export interface Publisher {
    id: number;
    name: string;
}

const publishers: Publisher[] = [
    {
        "id": 1,
        "name": "Secker & Warburg"
    },
    {
        "id": 2,
        "name": "Bloomsbury Publishing"
    },
    {
        "id": 3,
        "name": "Prentice Hall"
    },
    {
        "id": 4,
        "name": "Bantam Books"
    }
]

export default publishers;