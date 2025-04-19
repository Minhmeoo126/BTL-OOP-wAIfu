-- Create Users table
CREATE TABLE Users (
                       id INTEGER PRIMARY KEY AUTOINCREMENT,
                       username TEXT NOT NULL UNIQUE,
                       password TEXT NOT NULL,
                       role TEXT NOT NULL DEFAULT 'user', -- Thay ENUM bằng TEXT
                       email TEXT NOT NULL UNIQUE,
                       full_name TEXT NOT NULL
);

-- Create Category table
CREATE TABLE Category (
                          id INTEGER PRIMARY KEY AUTOINCREMENT,
                          name TEXT NOT NULL UNIQUE
);

-- Create Author table
CREATE TABLE Author (
                        id INTEGER PRIMARY KEY AUTOINCREMENT,
                        name TEXT NOT NULL UNIQUE
);

-- Create Book table
CREATE TABLE Book (
                      id INTEGER PRIMARY KEY AUTOINCREMENT,
                      title TEXT NOT NULL,
                      author_id INTEGER NOT NULL,
                      category_id INTEGER NOT NULL,
                      total_copies INTEGER NOT NULL,
                      available_copies INTEGER NOT NULL,
                      FOREIGN KEY (author_id) REFERENCES Author(id),
                      FOREIGN KEY (category_id) REFERENCES Category(id)
);

-- Create BorrowingRecord table
CREATE TABLE BorrowingRecord (
                                 id INTEGER PRIMARY KEY AUTOINCREMENT,
                                 user_id INTEGER NOT NULL,
                                 book_id INTEGER NOT NULL,
                                 borrow_date TEXT NOT NULL, -- SQLite không có kiểu DATE, dùng TEXT để lưu định dạng 'YYYY-MM-DD'
                                 return_date TEXT, -- Có thể NULL
                                 FOREIGN KEY (user_id) REFERENCES Users(id),
                                 FOREIGN KEY (book_id) REFERENCES Book(id)
);

-- Create Reviews table
CREATE TABLE Reviews (
                         id INTEGER PRIMARY KEY AUTOINCREMENT,
                         user_id INTEGER NOT NULL,
                         book_id INTEGER NOT NULL,
                         rating INTEGER NOT NULL CHECK (rating >= 1 AND rating <= 5),
                         comment TEXT,
                         review_date TEXT NOT NULL, -- SQLite không có kiểu DATE, dùng TEXT
                         FOREIGN KEY (user_id) REFERENCES Users(id),
                         FOREIGN KEY (book_id) REFERENCES Book(id)
);