// GET /api/books

export interface Book {
    id: number,
    title: string,
    description: string,
    publisherId: number,
    authorIds: number[],
    categoryIds: number[]
}

const books: Book[] = [
    {
        "id": 1,
        "title": "1984",
        "description": "A dystopian novel set in a totalitarian regime",
        "publisherId": 1,
        "authorIds": [
            1
        ],
        "categoryIds": [
            1
        ]
    },
    {
        "id": 2,
        "title": "Harry Potter and the Philosopher's Stone",
        "description": "The first book in the Harry Potter series",
        "publisherId": 2,
        "authorIds": [
            2
        ],
        "categoryIds": [
            1
        ]
    },
    {
        "id": 3,
        "title": "Clean Code",
        "description": "A handbook of agile software craftsmanship",
        "publisherId": 3,
        "authorIds": [
            3
        ],
        "categoryIds": [
            4
        ]
    },
    {
        "id": 4,
        "title": "A Brief History of Time",
        "description": "A book about modern physics for non-scientists",
        "publisherId": 4,
        "authorIds": [
            4
        ],
        "categoryIds": [
            2
        ]
    }
]

export default books;