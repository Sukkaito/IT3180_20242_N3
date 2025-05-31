export type User = {
    id: string;
    name: string;
    userName: string;
    email: string;
    password: string;
    createdAt: string;
    updatedAt: string;
    roleId: number;
};

const users: User[] = [
    {
        id: "e1a9e224-5691-4a78-a219-1ef430ef2b3e",
        name: "Administrator",
        userName: "admin",
        email: "admin@library.com",
        password: "password",
        createdAt: new Date().toISOString(),
        updatedAt: new Date().toISOString(),
        roleId: 1,
    },
    {
        id: "b24d5066-6321-4de8-af43-9a852d55a0a6",
        name: "John Doe",
        userName: "john_doe",
        email: "john@example.com",
        password: "password",
        createdAt: new Date().toISOString(),
        updatedAt: new Date().toISOString(),
        roleId: 2,
    },
    {
        id: "c9b5f975-43c0-42f5-9b5e-ed62a4f935d1",
        name: "Jane Smith",
        userName: "jane_smith",
        email: "jane@example.com",
        password: "password",
        createdAt: new Date().toISOString(),
        updatedAt: new Date().toISOString(),
        roleId: 2,
    },
];

export default users;
