-- Clear existing data (optional)
-- Uncomment if needed, but be careful in production
-- TRUNCATE TABLE book_loan, book, author, category, app_user CASCADE;

-- Insert Categories
INSERT INTO categories (id, name) VALUES
    (1, 'Fiction'),
    (2, 'Science'),
    (3, 'History'),
    (4, 'Computer Science');

SELECT setval('categories_id_seq', (SELECT MAX(id) FROM categories));

-- Insert Authors
INSERT INTO authors (id, name) VALUES
    (1, 'George Orwell'),
    (2, 'J.K. Rowling'),
    (3, 'Robert C. Martin'),
    (4, 'Stephen Hawking');

SELECT setval('authors_id_seq', (SELECT MAX(id) FROM authors));

-- Insert Publishers
INSERT INTO publishers (id, name) VALUES
    (1, 'Secker & Warburg'),
    (2, 'Bloomsbury Publishing'),
    (3, 'Prentice Hall'),
    (4, 'Bantam Books');

SELECT setval('publishers_id_seq', (SELECT MAX(id) FROM publishers));

-- Insert Books
INSERT INTO books (book_id, title, description, publisher_id) VALUES
    (1, '1984', 'A dystopian novel set in a totalitarian regime', 1), -- Publisher ID 1
    (2, 'Harry Potter and the Philosopher''s Stone', 'The first book in the Harry Potter series', 2), -- Publisher ID 2
    (3, 'Clean Code', 'A handbook of agile software craftsmanship', 3), -- Publisher ID 3
    (4, 'A Brief History of Time', 'A book about modern physics for non-scientists', 4); -- Publisher ID 4

SELECT setval('books_seq', (SELECT MAX(book_id) FROM books));

-- Insert Book Copies
INSERT INTO book_copies (id, original_book_book_id, status) VALUES
    ('1', 1, 'AVAILABLE'),
    ('2', 2, 'AVAILABLE'),
    ('3', 3, 'AVAILABLE'),
    ('4', 4, 'AVAILABLE'),
    ('5', 1, 'AVAILABLE'),
    ('6', 2, 'AVAILABLE'),
    ('7', 3, 'AVAILABLE'),
    ('8', 4, 'AVAILABLE');

SELECT setval('book_copies_seq', (SELECT MAX(id) FROM book_copies));

-- Insert Books-Authors relationships
INSERT INTO books_authors (book_id, author_id) VALUES
    (1, 1), -- 1984 by George Orwell
    (2, 2), -- Harry Potter by J.K. Rowling
    (3, 3), -- Clean Code by Robert C. Martin
    (4, 4); -- A Brief History of Time by Stephen Hawking

-- Insert Books-Categories relationships
INSERT INTO books_categories (book_id, category_id) VALUES
    (1, 1), -- 1984 is Fiction
    (2, 1), -- Harry Potter is Fiction
    (3, 4), -- Clean Code is Computer Science
    (4, 2); -- A Brief History of Time is Science

-- Insert Roles
INSERT INTO roles (id, name) VALUES
     (1, 'ADMIN'),
     (2, 'USER'),
     (3, 'STAFF');

SELECT setval('roles_id_seq', (SELECT MAX(id) FROM roles));

-- Insert Permissions
INSERT INTO permissions (id, name) VALUES
     (1, 'READ_BOOKS'),
     (2, 'WRITE_BOOKS'),
     (3, 'MANAGE_USERS'),
     (4, 'BORROW_BOOKS');

SELECT setval('permissions_id_seq', (SELECT MAX(id) FROM permissions));

-- Insert Role Permissions
INSERT INTO roles_permissions (role_id, permission_id) VALUES
     (1, 1), -- ADMIN can READ_BOOKS
     (1, 2), -- ADMIN can WRITE_BOOKS
     (1, 3), -- ADMIN can MANAGE_USERS
     (2, 1), -- USER can READ_BOOKS
     (2, 4); -- USER can BORROW_BOOKS

-- Insert Users
INSERT INTO users (id, name, user_name, email, password, created_at, updated_at, role_id) VALUES
     ('e1a9e224-5691-4a78-a219-1ef430ef2b3e', 'Administrator', 'admin', 'admin@library.com', 'password', NOW(), NOW(), 1),
     ('b24d5066-6321-4de8-af43-9a852d55a0a6', 'John Doe', 'john_doe', 'john@example.com', 'password', NOW(), NOW(), 2),
     ('c9b5f975-43c0-42f5-9b5e-ed62a4f935d1', 'Jane Smith', 'jane_smith', 'jane@example.com', 'password', NOW(), NOW(), 2),
     ('d3b5f975-43c0-42f5-9b5e-ed62a4f935d1', 'Alice Johnson', 'alice_johnson', 'alice@example.com', 'password', NOW(), NOW(), 2),
     ('f8e1c2b3-a4d5-6e7f-8090-abcdef123456', 'Michael Smith', 'michael_smith', 'michael@example.com', 'password', NOW(), NOW(), 3),
     ('f0e1d2c3-b4a5-6789-0abc-def123456789', 'Bob Brown', 'bob_brown', 'bob@example.com', 'password', NOW(), NOW(), 3);

-- Note: Passwords are stored as plain text for development purposes

-- Insert Book Loans
INSERT INTO book_loans (id, book_copy_id, user_id, loan_date, return_date, actual_return_date, status, current_book_request_id, loaned_at, updated_at,loan_duration) VALUES
    ('1b6e6cbf-cd10-4a51-b3e7-aaae8364fc22', 1, 'b24d5066-6321-4de8-af43-9a852d55a0a6', NOW() - INTERVAL '10 days', NOW() + INTERVAL '20 days', NULL, 'BORROWED', NULL, NOW() - INTERVAL '10 days', NOW() - INTERVAL '10 days',30),
    ('cf9592e2-ec63-45d4-bacc-a51f8c4a5a38', 2, 'c9b5f975-43c0-42f5-9b5e-ed62a4f935d1', NOW() - INTERVAL '5 days', NOW() + INTERVAL '25 days', NULL, 'BORROWED', NULL, NOW() - INTERVAL '5 days', NOW() - INTERVAL '5 days',30),
    ('86621cd0-4fad-47d8-947e-bf1025afaedf', 3, 'b24d5066-6321-4de8-af43-9a852d55a0a6', NOW() - INTERVAL '30 days', NOW(), NOW() - INTERVAL '2 days', 'RETURNED', NULL, NOW() - INTERVAL '30 days', NOW() - INTERVAL '2 days',30),
    ('46c9dd3c-7a59-4928-a0e0-830aff62294b', 4, 'c9b5f975-43c0-42f5-9b5e-ed62a4f935d1', NOW() - INTERVAL '15 days', NOW() + INTERVAL '15 days', NULL, 'REQUEST_RETURNING', NULL, NOW() - INTERVAL '15 days', NOW(),30);

-- Insert Book Requests
INSERT INTO book_requests (id, book_loan_id, book_copy_id, user_id, status, type, created_at, updated_at) VALUES
    ('1', NULL, 1, 'b24d5066-6321-4de8-af43-9a852d55a0a6', 'PENDING', 'BORROWING', CURRENT_TIMESTAMP - INTERVAL '1 day', CURRENT_TIMESTAMP - INTERVAL '1 day'),
    ('2', 'cf9592e2-ec63-45d4-bacc-a51f8c4a5a38', 2, 'c9b5f975-43c0-42f5-9b5e-ed62a4f935d1', 'ACCEPTED', 'BORROWING', CURRENT_TIMESTAMP - INTERVAL '2 days', CURRENT_TIMESTAMP - INTERVAL '2 days'),
    ('3', '86621cd0-4fad-47d8-947e-bf1025afaedf', 3, 'b24d5066-6321-4de8-af43-9a852d55a0a6', 'PENDING', 'RETURNING', CURRENT_TIMESTAMP - INTERVAL '3 days', CURRENT_TIMESTAMP - INTERVAL '3 days'),
    ('4', '46c9dd3c-7a59-4928-a0e0-830aff62294b', 4, 'c9b5f975-43c0-42f5-9b5e-ed62a4f935d1', 'DENIED', 'RETURNING', CURRENT_TIMESTAMP - INTERVAL '4 days', CURRENT_TIMESTAMP - INTERVAL '4 days'),
    ('5', NULL, 5, 'e1a9e224-5691-4a78-a219-1ef430ef2b3e', 'PENDING', 'BORROWING', CURRENT_TIMESTAMP - INTERVAL '5 days', CURRENT_TIMESTAMP - INTERVAL '5 days');

INSERT INTO status_logs (component, message, status, timestamp) VALUES
    ('database', 'Database connection re-established', 'OK', NOW() - INTERVAL '5 hours 1 minute'),
    ('server', 'Application started', 'OK', NOW() - INTERVAL '4 hours 2 minutes'),
    ('server', 'Application shutting down', 'NOT OK', NOW() - INTERVAL '4 hours 1 minute'),
    ('server', 'Application restarting', 'OK', NOW() - INTERVAL '4 hours'),
    ('database', 'Database connection lost', 'NOT OK', NOW() - INTERVAL '3 hours'),
    ('server', 'Application started', 'OK', NOW() - INTERVAL '2 hours'),
    ('server', 'Application shutting down', 'NOT OK', NOW() - INTERVAL '1 hour');

-- Insert Fines
INSERT INTO fines (id, user_id, book_loan_id, amount, created_at, updated_at, description) VALUES
    ('1', 'b24d5066-6321-4de8-af43-9a852d55a0a6', '86621cd0-4fad-47d8-947e-bf1025afaedf', 5.00, NOW() - INTERVAL '2 days', NOW() - INTERVAL '2 days', 'Late return fine for Clean Code'),
    ('2', 'c9b5f975-43c0-42f5-9b5e-ed62a4f935d1', '46c9dd3c-7a59-4928-a0e0-830aff62294b', 3.00, NOW() - INTERVAL '1 day', NOW() - INTERVAL '1 day', 'Late return fine for A Brief History of Time');