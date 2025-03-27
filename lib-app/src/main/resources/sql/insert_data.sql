USE lib;

-- Thêm dữ liệu vào Users
INSERT INTO Users (username, password, full_name, email, role) VALUES
                                                                   ('admin', 'admin123', 'Admin User', 'admin@example.com', 'admin'),
                                                                   ('john_doe', 'password1', 'John Doe', 'john@example.com', 'member'),
                                                                   ('jane_smith', 'password2', 'Jane Smith', 'jane@example.com', 'member');

-- Thêm dữ liệu vào Category
INSERT INTO Category (name) VALUES
                                ('Science Fiction'), ('History'), ('Romance');

-- Thêm dữ liệu vào Author
INSERT INTO Author (name, bio) VALUES
                                   ('Isaac Asimov', 'Famous for sci-fi works like Foundation.'),
                                   ('Jane Austen', 'English novelist known for Pride and Prejudice.');

-- Thêm dữ liệu vào Book
INSERT INTO Book (title, author_id, category_id, genre, publication_year, available_copies, total_copies) VALUES
                                                                                                              ('Foundation', 1, 1, 'Science', 1951, 5, 10),
                                                                                                              ('Pride and Prejudice', 2, 3, 'Romance', 1813, 3, 5);

-- Thêm dữ liệu vào BorrowingRecord
INSERT INTO BorrowingRecord (user_id, book_id, borrow_date, due_date, status) VALUES
                                                                                  (2, 1, '2025-03-20', '2025-04-03', 'borrowed'),
                                                                                  (3, 2, '2025-03-21', '2025-04-04', 'borrowed');

-- Thêm dữ liệu vào Reviews
INSERT INTO Reviews (user_id, book_id, rating, comment) VALUES
                                                            (2, 1, 5, 'Amazing book! A must-read for sci-fi lovers.'),
                                                            (3, 2, 4, 'Classic romance, well-written.');
