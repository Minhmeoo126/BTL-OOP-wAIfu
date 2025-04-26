CREATE TABLE IF NOT EXISTS Users (
                                     id INTEGER PRIMARY KEY AUTOINCREMENT,
                                     username TEXT NOT NULL UNIQUE,
                                     password TEXT NOT NULL,
                                     role TEXT NOT NULL DEFAULT 'user',
                                     email TEXT NOT NULL UNIQUE,
                                     full_name TEXT NOT NULL
);

CREATE TABLE IF NOT EXISTS Category (
                                        id INTEGER PRIMARY KEY AUTOINCREMENT,
                                        name TEXT NOT NULL UNIQUE
);

CREATE TABLE IF NOT EXISTS Author (
                                      id INTEGER PRIMARY KEY AUTOINCREMENT,
                                      name TEXT NOT NULL UNIQUE
);

CREATE TABLE IF NOT EXISTS Book (
                                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                                    title TEXT NOT NULL,
                                    author_id INTEGER NOT NULL,
                                    category_id INTEGER NOT NULL,
                                    total_copies INTEGER NOT NULL,
                                    available_copies INTEGER NOT NULL,
                                    description TEXT,
                                    FOREIGN KEY (author_id) REFERENCES Author(id),
    FOREIGN KEY (category_id) REFERENCES Category(id)
    );

CREATE TABLE IF NOT EXISTS BorrowingRecord (
                                               id INTEGER PRIMARY KEY AUTOINCREMENT,
                                               user_id INTEGER NOT NULL,
                                               book_id INTEGER NOT NULL,
                                               borrow_date TEXT NOT NULL,
                                               return_date TEXT,
                                               FOREIGN KEY (user_id) REFERENCES Users(id),
    FOREIGN KEY (book_id) REFERENCES Book(id)
    );

CREATE TABLE IF NOT EXISTS Reviews (
                                       id INTEGER PRIMARY KEY AUTOINCREMENT,
                                       user_id INTEGER NOT NULL,
                                       book_id INTEGER NOT NULL,
                                       rating INTEGER NOT NULL CHECK (rating >= 1 AND rating <= 5),
    comment TEXT,
    review_date TEXT NOT NULL,
    FOREIGN KEY (user_id) REFERENCES Users(id),
    FOREIGN KEY (book_id) REFERENCES Book(id)
    );