-- Insert sample data into Users
INSERT INTO Users (username, password, full_name, email, role, created_at) VALUES
                                                                               ('admin', 'admin123', 'Admin User', 'admin@example.com', 'admin', '2025-03-27 11:00:00'),
                                                                               ('john_doe', 'password1', 'John Doe', 'john@example.com', 'member', '2025-03-27 11:00:00'),
                                                                               ('jane_smith', 'password2', 'Jane Smith', 'jane@example.com', 'member', '2025-03-27 11:00:00');

-- Insert sample data into Category
INSERT INTO Category (name) VALUES
                                ('Science Fiction'),
                                ('Romance'),
                                ('Mystery');

-- Insert sample data into Author
INSERT INTO Author (name, bio) VALUES
                                   ('Isaac Asimov', 'A prolific science fiction writer known for the Foundation series.'),
                                   ('Jane Austen', 'An English novelist known for her romance novels.');

-- Insert sample data into Book
INSERT INTO Book (title, author_id, category_id, total_copies, available_copies) VALUES
                                                                                     ('Foundation', 1, 1, 5, 5),
                                                                                     ('Pride and Prejudice', 2, 2, 3, 3);

-- Insert sample data into BorrowingRecord
INSERT INTO BorrowingRecord (user_id, book_id, borrow_date, return_date) VALUES
    (2, 1, '2025-03-27', NULL);

-- Insert sample data into Reviews
INSERT INTO Reviews (user_id, book_id, rating, comment, review_date) VALUES
                                                                         (2, 1, 5, 'Amazing book! Loved the world-building.', '2025-03-27 11:30:00'),
                                                                         (3, 2, 4, 'A classic romance novel, very engaging.', '2025-03-27 11:45:00');