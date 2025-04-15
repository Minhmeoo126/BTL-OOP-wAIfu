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
            Connection conn = DriverManager.getConnection(URL);
            if (!dbFile.exists()) {
                logger.info("Database file created: " + dbFile.getAbsolutePath());
                initializeDatabase(conn);
            }
            return conn;
        } catch (SQLException e) {
            logger.severe("Database connection error: " + e.getMessage());
            throw new RuntimeException(e);
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