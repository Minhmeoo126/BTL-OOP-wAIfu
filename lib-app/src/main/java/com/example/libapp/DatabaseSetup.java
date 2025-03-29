package com.example.libapp;

import com.example.libapp.persistence.DatabaseConnection;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseSetup {
    public static void main(String[] args) {
        try (Connection conn = DatabaseConnection.connect()) {
            System.out.println("Database connected successfully!");

            // Đảm bảo auto-commit được bật
            conn.setAutoCommit(true);

            // Chạy script xóa bảng
            runSQLScript(conn, "src/main/resources/sql/drop-table.sql");

            // Chạy script tạo bảng
            runSQLScript(conn, "src/main/resources/sql/create-table.sql");

            // Chạy script tạo trigger
            runTriggerScript(conn, "src/main/resources/sql/create-triggers.sql");

            // Chạy script chèn dữ liệu
            runSQLScript(conn, "src/main/resources/sql/insert-data.sql");

            System.out.println("Database setup completed!");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void runSQLScript(Connection conn, String filePath) throws SQLException {
        try {
            // Đọc file script
            String sql = new String(Files.readAllBytes(Paths.get(filePath)));

            // Loại bỏ các dòng comment (bắt đầu bằng --)
            sql = sql.replaceAll("(?m)^--.*\n?", "");

            // Tách các câu lệnh SQL bằng dấu ;
            String[] statements = sql.split(";");

            try (Statement stmt = conn.createStatement()) {
                for (String statement : statements) {
                    statement = statement.trim();
                    if (!statement.isEmpty()) {
                        stmt.executeUpdate(statement);
                    }
                }
                System.out.println("Script executed: " + filePath);
            }
        } catch (Exception e) {
            System.out.println("Error: " + filePath);
            e.printStackTrace();
        }
    }

    private static void runTriggerScript(Connection conn, String filePath) throws SQLException {
        try {
            // Đọc file script
            String sql = new String(Files.readAllBytes(Paths.get(filePath)));

            // Loại bỏ các dòng comment (bắt đầu bằng --)
            sql = sql.replaceAll("(?m)^--.*\n?", "");

            // Tách các câu lệnh CREATE TRIGGER bằng từ khóa "CREATE TRIGGER"
            String[] triggerStatements = sql.split("(?=CREATE TRIGGER)");

            try (Statement stmt = conn.createStatement()) {
                for (String triggerStatement : triggerStatements) {
                    triggerStatement = triggerStatement.trim();
                    if (!triggerStatement.isEmpty()) {
                        // Thực thi toàn bộ câu lệnh CREATE TRIGGER
                        stmt.execute(triggerStatement);
                    }
                }
                System.out.println("Script executed: " + filePath);
            }
        } catch (Exception e) {
            System.out.println("Error: " + filePath);
            e.printStackTrace();
        }
    }
}