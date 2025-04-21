package com.example.libapp.persistence;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Logger;

public class DatabaseConnection {
    private static final Logger logger = Logger.getLogger(DatabaseConnection.class.getName());
    private static final String DB_URL = "jdbc:sqlite:lib-app/lib.db";

    public static Connection connect() throws SQLException {
        // Log the current working directory
        String workingDir = System.getProperty("user.dir");
        logger.info("Current working directory: " + workingDir);

        // Log the absolute path of the database file
        File dbFile = new File("lib-app/lib.db");
        logger.info("Database file absolute path: " + dbFile.getAbsolutePath());

        Connection conn = DriverManager.getConnection(DB_URL);
        initializeDatabase(conn);
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