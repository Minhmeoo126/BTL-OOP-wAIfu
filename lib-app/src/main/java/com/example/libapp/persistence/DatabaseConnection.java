package com.example.libapp.persistence;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseConnection {
    private static final String URL = "jdbc:sqlite:lib.db"; // File cơ sở dữ liệu SQLite

    public static Connection connect() throws SQLException {
        Connection conn = DriverManager.getConnection(URL);
        // Bật hỗ trợ khóa ngoại trong SQLite
        try (Statement stmt = conn.createStatement()) {
            stmt.execute("PRAGMA foreign_keys = ON;");
        }
        return conn;
    }
}