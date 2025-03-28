-- Create Users table
CREATE TABLE Users (
                       id INT AUTO_INCREMENT PRIMARY KEY,
                       username VARCHAR(50) NOT NULL UNIQUE,
                       password VARCHAR(255) NOT NULL,
                       role ENUM('admin', 'user') NOT NULL DEFAULT 'user',
                       email VARCHAR(100) NOT NULL UNIQUE,
                       full_name VARCHAR(100) NOT NULL
);

-- Create Category table
CREATE TABLE Category (
                          id INT AUTO_INCREMENT PRIMARY KEY,
                          name VARCHAR(50) NOT NULL UNIQUE
);

-- Create Author table
CREATE TABLE Author (
                        id INT AUTO_INCREMENT PRIMARY KEY,
                        name VARCHAR(100) NOT NULL UNIQUE
);

-- Create Book table
CREATE TABLE Book (
                      id INT AUTO_INCREMENT PRIMARY KEY,
                      title VARCHAR(255) NOT NULL,
                      author_id INT NOT NULL,
                      category_id INT NOT NULL,
                      total_copies INT NOT NULL,
                      available_copies INT NOT NULL,
                      FOREIGN KEY (author_id) REFERENCES Author(id),
                      FOREIGN KEY (category_id) REFERENCES Category(id)
);

-- Create BorrowingRecord table
CREATE TABLE BorrowingRecord (
                                 id INT AUTO_INCREMENT PRIMARY KEY,
                                 user_id INT NOT NULL,
                                 book_id INT NOT NULL,
                                 borrow_date DATE NOT NULL,
                                 return_date DATE,
                                 FOREIGN KEY (user_id) REFERENCES Users(id),
                                 FOREIGN KEY (book_id) REFERENCES Book(id)
);

-- Create Reviews table
CREATE TABLE Reviews (
                         id INT AUTO_INCREMENT PRIMARY KEY,
                         user_id INT NOT NULL,
                         book_id INT NOT NULL,
                         rating INT NOT NULL CHECK (rating >= 1 AND rating <= 5),
                         comment TEXT,
                         review_date DATE NOT NULL,
                         FOREIGN KEY (user_id) REFERENCES Users(id),
                         FOREIGN KEY (book_id) REFERENCES Book(id)
);