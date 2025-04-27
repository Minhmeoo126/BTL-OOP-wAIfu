package com.example.libapp.persistence;

import java.io.File;
import java.sql.*;
import java.util.logging.Logger;

public class DatabaseConnection {
    private static final Logger logger = Logger.getLogger(DatabaseConnection.class.getName());
    private static final String DB_URL = "jdbc:sqlite:lib-app/lib.db";

    // Embedded SQL scripts
    private static final String CREATE_TABLE_SQL = """
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
            
            CREATE TABLE IF NOT EXISTS chat_history (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                user_id INTEGER NOT NULL,
                message TEXT NOT NULL,
                response TEXT NOT NULL,
                timestamp DATETIME DEFAULT CURRENT_TIMESTAMP,
                FOREIGN KEY(user_id) REFERENCES Users(id)
            );
            
            """;

    private static final String CREATE_TRIGGERS_SQL = """
            CREATE TRIGGER IF NOT EXISTS before_book_insert
            BEFORE INSERT ON Book
            FOR EACH ROW
            BEGIN
                SELECT RAISE(ABORT, 'Total copies and available copies must be non-negative')
                WHERE NEW.total_copies < 0 OR NEW.available_copies < 0;
            
                SELECT RAISE(ABORT, 'Available copies cannot exceed total copies')
                WHERE NEW.available_copies > NEW.total_copies;
            END;
            
            CREATE TRIGGER IF NOT EXISTS before_book_update
            BEFORE UPDATE ON Book
            FOR EACH ROW
            BEGIN
                SELECT RAISE(ABORT, 'Total copies and available copies must be non-negative')
                WHERE NEW.total_copies < 0 OR NEW.available_copies < 0;
            
                SELECT RAISE(ABORT, 'Available copies cannot exceed total copies')
                WHERE NEW.available_copies > NEW.total_copies;
            END;
            
            CREATE TRIGGER IF NOT EXISTS after_borrow_insert
            AFTER INSERT ON BorrowingRecord
            FOR EACH ROW
            BEGIN
                SELECT RAISE(ABORT, 'No copies available to borrow')
                WHERE (SELECT available_copies FROM Book WHERE id = NEW.book_id) < 0;
            
                UPDATE Book
                SET available_copies = available_copies - 1
                WHERE id = NEW.book_id;
            END;
            
            CREATE TRIGGER IF NOT EXISTS after_borrow_update
            AFTER UPDATE ON BorrowingRecord
            FOR EACH ROW
            WHEN NEW.return_date IS NOT NULL AND OLD.return_date IS NULL
            BEGIN
                UPDATE Book
                SET available_copies = available_copies + 1
                WHERE id = NEW.book_id;
            END;
            """;

    private static final String INSERT_DATA_SQL = """
            -- Insert data into Users
            INSERT OR IGNORE INTO Users (username, password, role, email, full_name) VALUES
                ('admin', 'admin123', 'ADMIN', 'admin@example.com', 'Admin User'),
                ('john_doe', 'password123', 'USER', 'john.doe@example.com', 'John Doe'),
                ('jane_smith', 'password123', 'USER', 'jane.smith@example.com', 'Jane Smith');
            """;

    public static Connection connect() throws SQLException {
        String workingDir = System.getProperty("user.dir");
        logger.info("Current working directory: " + workingDir);

        File dbFile = new File("lib-app/lib.db");
        logger.info("Database file absolute path: " + dbFile.getAbsolutePath());

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
            stmt.execute("PRAGMA foreign_keys = ON;");
            executeRawSql(stmt, "create-tables", CREATE_TABLE_SQL);
            executeRawSql(stmt, "create-triggers", CREATE_TRIGGERS_SQL);
            executeRawSql(stmt, "insert-data", INSERT_DATA_SQL);

            // Add thumbnail column to Book table if it doesn't exist
            addColumnIfNotExists(conn, "Book", "thumbnail", "TEXT");
        }
    }

    private static void addColumnIfNotExists(Connection connection, String tableName, String columnName, String columnDefinition) throws SQLException {
        boolean exists = false;
        DatabaseMetaData meta = connection.getMetaData();
        try (ResultSet rs = meta.getColumns(null, null, tableName, columnName)) {
            if (rs.next()) {
                exists = true;
            }
        }

        if (!exists) {
            String sql = "ALTER TABLE " + tableName + " ADD COLUMN " + columnName + " " + columnDefinition + ";";
            try (Statement stmt = connection.createStatement()) {
                stmt.execute(sql);
                logger.info("Added column '" + columnName + "' to table '" + tableName + "'.");
            }
        } else {
            logger.info("Column '" + columnName + "' already exists in table '" + tableName + "'. Skipping...");
        }
    }

    private static void executeRawSql(Statement stmt, String scriptName, String sql) throws SQLException {
        logger.info("Executing SQL script: " + scriptName);
        try {
            stmt.executeUpdate(sql);
            logger.info("Script '" + scriptName + "' executed successfully.");
        } catch (SQLException e) {
            logger.severe("Failed to execute script: " + scriptName);
            throw e;
        }
    }

    public void saveChatHistory(int userId, String message, String response) throws SQLException {
        String sql = "INSERT INTO chat_history (user_id, message, response) VALUES (?, ?, ?)";

        try (Connection conn = connect(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, userId);
            pstmt.setString(2, message);
            pstmt.setString(3, response);
            pstmt.executeUpdate();
        }
    }

    public ResultSet getChatHistory(int userId) throws SQLException {
        // Kết nối cơ sở dữ liệu
        String sql = "SELECT message, response, timestamp FROM chat_history WHERE user_id = ? ORDER BY timestamp DESC";

        // Kết nối với cơ sở dữ liệu và chuẩn bị câu lệnh truy vấn
        Connection conn = connect();  // Kết nối với cơ sở dữ liệu
        PreparedStatement pstmt = conn.prepareStatement(sql);  // Tạo PreparedStatement từ câu lệnh SQL
        pstmt.setInt(1, userId);  // Gán ID người dùng vào câu lệnh SQL

        // Thực hiện truy vấn và trả về ResultSet
        return pstmt.executeQuery();  // Trả về kết quả truy vấn
    }

}
