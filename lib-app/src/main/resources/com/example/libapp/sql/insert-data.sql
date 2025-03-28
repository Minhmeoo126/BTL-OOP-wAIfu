-- Insert data into Users
INSERT INTO Users (username, password, role, email, full_name) VALUES
                                                                   ('admin', 'admin123', 'admin', 'admin@example.com', 'Admin User'),
                                                                   ('john_doe', 'password123', 'user', 'john.doe@example.com', 'John Doe'),
                                                                   ('jane_smith', 'password123', 'user', 'jane.smith@example.com', 'Jane Smith');

-- Insert data into Category
INSERT INTO Category (name) VALUES
                                ('Science Fiction'),
                                ('Romance'),
                                ('Mystery');

-- Insert data into Author
INSERT INTO Author (name) VALUES
                              ('Isaac Asimov'),
                              ('Jane Austen'),
                              ('Agatha Christie');

-- Insert data into Book
INSERT INTO Book (title, author_id, category_id, total_copies, available_copies) VALUES
                                                                                     ('Foundation', 1, 1, 5, 5),
                                                                                     ('Pride and Prejudice', 2, 2, 3, 3),
                                                                                     ('Murder on the Orient Express', 3, 3, 4, 4);

-- Insert data into BorrowingRecord
INSERT INTO BorrowingRecord (user_id, book_id, borrow_date, return_date) VALUES
                                                                             (2, 1, '2025-03-20', NULL),
                                                                             (3, 2, '2025-03-21', '2025-03-25');

-- Insert data into Reviews
INSERT INTO Reviews (user_id, book_id, rating, comment, review_date) VALUES
                                                                         (2, 1, 5, 'Amazing book, highly recommend!', '2025-03-22'),
                                                                         (3, 2, 4, 'A classic romance novel.', '2025-03-26');