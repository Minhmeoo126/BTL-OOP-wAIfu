package com.example.libapp.persistence;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Logger;

public class DatabaseConnection {
    private static final Logger logger = Logger.getLogger(DatabaseConnection.class.getName());
    private static final String DB_URL = "jdbc:sqlite:lib-app/lib.db"; // Relative path to database

    // Embedded SQL scripts
    private static final String CREATE_TABLE_SQL = """
        -- Create Users table
        CREATE TABLE Users (
            id INTEGER PRIMARY KEY AUTOINCREMENT,
            username TEXT NOT NULL UNIQUE,
            password TEXT NOT NULL,
            role TEXT NOT NULL DEFAULT 'user',
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
            description TEXT,
            FOREIGN KEY (author_id) REFERENCES Author(id),
            FOREIGN KEY (category_id) REFERENCES Category(id)
        );

        -- Create BorrowingRecord table
        CREATE TABLE BorrowingRecord (
            id INTEGER PRIMARY KEY AUTOINCREMENT,
            user_id INTEGER NOT NULL,
            book_id INTEGER NOT NULL,
            borrow_date TEXT NOT NULL,
            return_date TEXT,
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
            review_date TEXT NOT NULL,
            FOREIGN KEY (user_id) REFERENCES Users(id),
            FOREIGN KEY (book_id) REFERENCES Book(id)
        );
        """;

    /**
    private static final String CREATE_TRIGGERS_SQL = """
            -- Check total valid book when INSERT
            CREATE TRIGGER before_book_insert
                BEFORE INSERT ON Book
                FOR EACH ROW
            BEGIN
                SELECT CASE
                           WHEN NEW.total_copies < 0 OR NEW.available_copies < 0 THEN
                               RAISE(ABORT, 'Total copies and available copies must be non-negative')
                           WHEN NEW.available_copies > NEW.total_copies THEN
                               RAISE(ABORT, 'Available copies cannot exceed total copies')
                           END;
            END;
            
            -- Check total books when UPDATE
            CREATE TRIGGER before_book_update
                BEFORE UPDATE ON Book
                FOR EACH ROW
            BEGIN
                SELECT CASE
                           WHEN NEW.total_copies < 0 OR NEW.available_copies < 0 THEN
                               RAISE(ABORT, 'Total copies and available copies must be non-negative')
                           WHEN NEW.available_copies > NEW.total_copies THEN
                               RAISE(ABORT, 'Available copies cannot exceed total copies')
                           END;
            END;
            
            -- Update available_copies when borrow
            CREATE TRIGGER after_borrow_insert
                AFTER INSERT ON BorrowingRecord
                FOR EACH ROW
            BEGIN
                UPDATE Book
                SET available_copies = available_copies - 1
                WHERE id = NEW.book_id;
            END;
            
            -- Update available_copies when return
            CREATE TRIGGER after_borrow_update
                AFTER UPDATE ON BorrowingRecord
                FOR EACH ROW
                WHEN NEW.return_date IS NOT NULL AND OLD.return_date IS NULL
            BEGIN
                UPDATE Book
                SET available_copies = available_copies + 1
                WHERE id = NEW.book_id;
            END;
            
        """;**/

    private static final String INSERT_DATA_SQL = """
        -- Insert data into Users
        INSERT INTO Users (username, password, role, email, full_name) VALUES
            ('admin', 'admin123', 'ADMIN', 'admin@example.com', 'Admin User'),
            ('john_doe', 'password123', 'USER', 'john.doe@example.com', 'John Doe'),
            ('jane_smith', 'password123', 'USER', 'jane.smith@example.com', 'Jane Smith');
        """;

    public static Connection connect() throws SQLException {
        // Log the current working directory
        String workingDir = System.getProperty("user.dir");
        logger.info("Current working directory: " + workingDir);

        // Log the absolute path of the database file
        File dbFile = new File("lib-app/lib.db");
        logger.info("Database file absolute path: " + dbFile.getAbsolutePath());

        // Ensure the database file can be created
        File parentDir = dbFile.getParentFile();
        if (parentDir != null && !parentDir.exists()) {
            parentDir.mkdirs();
        }

        Connection conn = DriverManager.getConnection(DB_URL);
        initializeDatabase(conn);
        logger.info("Database connected successfully");
        return conn;
    }

    private static void initializeDatabase(Connection conn) throws SQLException {
        try (Statement stmt = conn.createStatement()) {
            // Enable foreign key support
            stmt.execute("PRAGMA foreign_keys = ON;");

            // Execute embedded SQL scripts
            executeSqlScript(stmt, "create-table", CREATE_TABLE_SQL);
            //executeSqlScript(stmt, "create-triggers", CREATE_TRIGGERS_SQL);
            executeSqlScript(stmt, "insert-data", INSERT_DATA_SQL);
        }
    }

    private static void executeSqlScript(Statement stmt, String scriptName, String sql) throws SQLException {
        // Log the SQL being executed
        logger.info("Executing SQL script: " + scriptName);

        // Split SQL by semicolon, ignoring empty statements
        String[] statements = sql.split("(?<=;)(?=\\s*(\\n|$))");
        for (int i = 0; i < statements.length; i++) {
            String statement = statements[i].trim();
            if (!statement.isEmpty() && !statement.startsWith("--")) {
                try {
                    logger.info("Executing statement " + (i + 1) + ": " + statement);
                    stmt.execute(statement);
                } catch (SQLException e) {
                    logger.severe("Error executing statement " + (i + 1) + ": " + statement);
                    throw new SQLException("Failed to execute statement " + (i + 1) + " in script: " + scriptName, e);
                }
            }
        }
    }
}