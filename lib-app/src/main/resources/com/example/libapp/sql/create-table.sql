CREATE TABLE IF NOT EXISTS Users (
                                     id INT AUTO_INCREMENT PRIMARY KEY,
                                     username VARCHAR(50) NOT NULL UNIQUE,
                                     password VARCHAR(255) NOT NULL,
                                     full_name VARCHAR(100),
                                     email VARCHAR(100) NOT NULL UNIQUE,
                                     role ENUM('admin', 'member') NOT NULL,
                                     created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS Category (
                                        id INT AUTO_INCREMENT PRIMARY KEY,
                                        name VARCHAR(50) NOT NULL UNIQUE
);

CREATE TABLE IF NOT EXISTS Author (
                                      id INT AUTO_INCREMENT PRIMARY KEY,
                                      name VARCHAR(100) NOT NULL,
                                      bio TEXT
);

CREATE TABLE IF NOT EXISTS Book (
                                    id INT AUTO_INCREMENT PRIMARY KEY,
                                    title VARCHAR(255) NOT NULL,
                                    author_id INT,
                                    category_id INT,
                                    total_copies INT NOT NULL,
                                    available_copies INT NOT NULL,
                                    FOREIGN KEY (author_id) REFERENCES Author(id),
                                    FOREIGN KEY (category_id) REFERENCES Category(id),
                                    CHECK (available_copies <= total_copies)
);

CREATE TABLE IF NOT EXISTS BorrowingRecord (
                                               id INT AUTO_INCREMENT PRIMARY KEY,
                                               user_id INT,
                                               book_id INT,
                                               borrow_date DATE NOT NULL,
                                               return_date DATE,
                                               FOREIGN KEY (user_id) REFERENCES Users(id),
                                               FOREIGN KEY (book_id) REFERENCES Book(id)
);

CREATE TABLE IF NOT EXISTS Reviews (
                                       id INT AUTO_INCREMENT PRIMARY KEY,
                                       user_id INT,
                                       book_id INT,
                                       rating INT CHECK (rating >= 1 AND rating <= 5),
                                       comment TEXT,
                                       review_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                       FOREIGN KEY (user_id) REFERENCES Users(id),
                                       FOREIGN KEY (book_id) REFERENCES Book(id)
);