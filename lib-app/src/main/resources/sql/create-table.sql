CREATE DATABASE IF NOT EXISTS lib;
USE lib;

-- Bảng Users
CREATE TABLE IF NOT EXISTS Users (
                                     id INT AUTO_INCREMENT PRIMARY KEY,
                                     username VARCHAR(50) NOT NULL UNIQUE,
                                     password VARCHAR(255) NOT NULL,
                                     full_name VARCHAR(100) NOT NULL,
                                     email VARCHAR(100) UNIQUE,
                                     role ENUM('admin', 'member') NOT NULL DEFAULT 'member',  -- Bỏ librarian
                                     created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Bảng Category
CREATE TABLE IF NOT EXISTS Category (
                                        id INT AUTO_INCREMENT PRIMARY KEY,
                                        name VARCHAR(100) NOT NULL UNIQUE
);

-- Bảng Author
CREATE TABLE IF NOT EXISTS Author (
                                      id INT AUTO_INCREMENT PRIMARY KEY,
                                      name VARCHAR(100) NOT NULL,
                                      bio TEXT NULL
);

-- Bảng Book
CREATE TABLE IF NOT EXISTS Book (
                                    id INT AUTO_INCREMENT PRIMARY KEY,
                                    title VARCHAR(255) NOT NULL,
                                    author_id INT NOT NULL,
                                    category_id INT NOT NULL,
                                    genre ENUM('Fiction', 'Non-Fiction', 'Science', 'Romance', 'Mystery', 'Fantasy', 'History', 'Biography') NOT NULL,
                                    publication_year INT,
                                    available_copies INT NOT NULL CHECK (available_copies >= 0),
                                    total_copies INT NOT NULL CHECK (total_copies > 0),
                                    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                    FOREIGN KEY (author_id) REFERENCES Author(id) ON DELETE CASCADE,
                                    FOREIGN KEY (category_id) REFERENCES Category(id) ON DELETE CASCADE
);

-- Bảng BorrowingRecord (ĐÃ LOẠI BỎ CHECK)
CREATE TABLE IF NOT EXISTS BorrowingRecord (
                                               id INT AUTO_INCREMENT PRIMARY KEY,
                                               user_id INT NOT NULL,
                                               book_id INT NOT NULL,
                                               borrow_date DATE NOT NULL DEFAULT (CURRENT_DATE),
                                               due_date DATE NOT NULL,  -- Loại bỏ CHECK constraint do MySQL không hỗ trợ
                                               return_date DATE NULL,
                                               status ENUM('borrowed', 'returned', 'overdue') NOT NULL DEFAULT 'borrowed',
                                               FOREIGN KEY (user_id) REFERENCES Users(id) ON DELETE CASCADE,
                                               FOREIGN KEY (book_id) REFERENCES Book(id) ON DELETE CASCADE
);

-- Bảng Reviews
CREATE TABLE IF NOT EXISTS Reviews (
                                       id INT AUTO_INCREMENT PRIMARY KEY,
                                       user_id INT NOT NULL,
                                       book_id INT NOT NULL,
                                       rating INT CHECK (rating BETWEEN 1 AND 5),
                                       comment TEXT,
                                       created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                       FOREIGN KEY (user_id) REFERENCES Users(id) ON DELETE CASCADE,
                                       FOREIGN KEY (book_id) REFERENCES Book(id) ON DELETE CASCADE
);
