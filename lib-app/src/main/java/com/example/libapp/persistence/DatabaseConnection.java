package com.example.libapp.persistence;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class DatabaseConnection {
    private static final Logger logger = Logger.getLogger(DatabaseConnection.class.getName());
    private static final String DB_DIR = "lib-app";
    private static final String URL = "jdbc:sqlite:lib-app/lib.db";

    public static Connection connect() {
        try {
            File dbDir = new File(DB_DIR);
            if (!dbDir.exists()) {
                dbDir.mkdirs();
                logger.info("Created database directory: " + dbDir.getAbsolutePath());
            }
            File dbFile = new File(DB_DIR + "/lib.db");
            // Kiểm tra xem file có tồn tại và là file cơ sở dữ liệu hợp lệ không
            if (dbFile.exists()) {
                // Kiểm tra xem file có phải là file văn bản không (đọc byte đầu tiên)
                if (isTextFile(dbFile)) {
                    logger.warning("File lib.db is a text file, not a valid SQLite database. Deleting...");
                    dbFile.delete();
                } else {
                    Connection tempConn = null;
                    try {
                        tempConn = DriverManager.getConnection(URL);
                        if (!tableExists(tempConn, "Users")) {
                            logger.info("Existing database file is invalid or missing tables. Deleting and reinitializing...");
                            tempConn.close();
                            dbFile.delete();
                        } else {
                            tempConn.close();
                            return DriverManager.getConnection(URL);
                        }
                    } catch (SQLException e) {
                        logger.warning("Existing database file is invalid: " + e.getMessage());
                        if (tempConn != null) tempConn.close();
                        dbFile.delete();
                    }
                }
            }
            // Tạo kết nối mới
            Connection conn = DriverManager.getConnection(URL);
            logger.info("Database file path: " + dbFile.getAbsolutePath());
            // Khởi tạo cơ sở dữ liệu nếu cần
            if (!tableExists(conn, "Users")) {
                logger.info("Table 'Users' does not exist. Initializing database...");
                initializeDatabase(conn);
            }
            return conn;
        } catch (SQLException e) {
            logger.severe("Database connection error: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    // Phương thức kiểm tra xem file có phải là file văn bản không
    private static boolean isTextFile(File file) {
        try {
            // Đọc vài byte đầu tiên của file
            byte[] buffer = new byte[5];
            java.io.FileInputStream fis = new java.io.FileInputStream(file);
            int bytesRead = fis.read(buffer);
            fis.close();
            if (bytesRead < 5) return true; // File quá nhỏ, có thể là văn bản
            // SQLite database file bắt đầu bằng "SQLite format 3"
            String header = new String(buffer, StandardCharsets.UTF_8);
            return !header.startsWith("SQLit");
        } catch (Exception e) {
            logger.warning("Error checking file type: " + e.getMessage());
            return true; // Nếu không đọc được, giả định là file văn bản để an toàn
        }
    }

    private static boolean tableExists(Connection conn, String tableName) throws SQLException {
        try (var stmt = conn.createStatement()) {
            stmt.executeQuery("SELECT 1 FROM " + tableName + " LIMIT 1");
            return true;
        } catch (SQLException e) {
            return false;
        }
    }

    private static void initializeDatabase(Connection conn) {
        try {
            conn.setAutoCommit(true);
            runSQLScript(conn, "/com/example/libapp/sql/drop-table.sql");
            runSQLScript(conn, "/com/example/libapp/sql/create-table.sql");
            runSQLScript(conn, "/com/example/libapp/sql/create-triggers.sql");
            runSQLScript(conn, "/com/example/libapp/sql/insert-data.sql");
            logger.info("Database initialized successfully");
        } catch (SQLException e) {
            logger.severe("Error initializing database: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    private static void runSQLScript(Connection conn, String resourcePath) throws SQLException {
        try {
            InputStream inputStream = DatabaseConnection.class.getResourceAsStream(resourcePath);
            if (inputStream == null) {
                logger.severe("Cannot find resource: " + resourcePath);
                throw new SQLException("Resource not found: " + resourcePath);
            }
            String sql = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))
                    .lines().collect(Collectors.joining("\n"));
            sql = sql.replaceAll("(?m)^--.*\n?", "");
            String[] statements = resourcePath.contains("create-triggers.sql") ?
                    sql.split("(?=CREATE TRIGGER)") :
                    sql.split(";");
            try (Statement stmt = conn.createStatement()) {
                for (String statement : statements) {
                    statement = statement.trim();
                    if (!statement.isEmpty()) {
                        logger.info("Executing SQL: " + statement);
                        stmt.executeUpdate(statement);
                    }
                }
                logger.info("Executed script: " + resourcePath);
            }
        } catch (Exception e) {
            logger.severe("Error executing script " + resourcePath + ": " + e.getMessage());
            throw new SQLException("Failed to execute script: " + resourcePath, e);
        }
    }
}