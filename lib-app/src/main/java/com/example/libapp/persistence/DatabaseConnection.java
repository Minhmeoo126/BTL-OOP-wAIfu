package com.example.libapp.persistence;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Logger;

public class DatabaseConnection {
    private static final Logger logger = Logger.getLogger(DatabaseConnection.class.getName());
    private static final String DB_URL = "jdbc:sqlite:lib.db"; // Database in project root

    public static Connection connect() throws SQLException {
        // Log the current working directory
        String workingDir = System.getProperty("user.dir");
        logger.info("Current working directory: " + workingDir);

        // Log the absolute path of the database file
        File dbFile = new File("lib.db"); // Fixed to match DB_URL
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
            stmt.execute("PRAGMA foreign_keys = ON;");
            // Ensure 'description' column exists in Book table
            stmt.execute("ALTER TABLE Book ADD COLUMN description TEXT;");
        } catch (SQLException e) {
            // Ignore if column already exists
            if (!e.getMessage().contains("duplicate column name")) {
                throw e;
            }
        }
    }
}