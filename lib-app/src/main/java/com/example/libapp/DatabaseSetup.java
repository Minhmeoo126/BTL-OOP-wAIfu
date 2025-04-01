package com.example.libapp;

import com.example.libapp.persistence.DatabaseConnection;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseSetup {
    public static void main(String[] args) {
        try (Connection conn = DatabaseConnection.connect()) {
            System.out.println("Database connected successfully!");

            // Đảm bảo auto-commit được bật
            conn.setAutoCommit(true);

            // Chạy script xóa bảng
            runSQLScript(conn, "com/example/libapp/sql/drop-table.sql");
            // Chạy script tạo bảng
            runSQLScript(conn, "com/example/libapp/sql/create-table.sql");

            // Chạy script tạo trigger
            runTriggerScript(conn, "com/example/libapp/sql/create-triggers.sql");

            // Chạy script chèn dữ liệu
            runSQLScript(conn, "com/example/libapp/sql/insert-data.sql");

            System.out.println("Database setup completed!");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void runSQLScript(Connection conn, String resourcePath) throws SQLException {
        try (InputStream inputStream = DatabaseSetup.class.getClassLoader().getResourceAsStream(resourcePath)) {
            if (inputStream == null) {
                throw new IllegalArgumentException("File not found: " + resourcePath);
            }
            String sql = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);

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
                System.out.println("Script executed: " + resourcePath);
            }
        } catch (Exception e) {
            System.out.println("Error: " + resourcePath);
            e.printStackTrace();
        }
    }

    private static void runTriggerScript(Connection conn, String resourcePath) throws SQLException {
        try (InputStream inputStream = DatabaseSetup.class.getClassLoader().getResourceAsStream(resourcePath)) {
            if (inputStream == null) {
                throw new IllegalArgumentException("File not found: " + resourcePath);
            }
            String sql = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);

            // Loại bỏ các dòng comment (bắt đầu bằng --)
            sql = sql.replaceAll("(?m)^--.*\n?", "");

            // Tách các câu lệnh CREATE TRIGGER bằng từ khóa "CREATE TRIGGER"
            String[] triggerStatements = sql.split("(?=CREATE TRIGGER)");

            try (Statement stmt = conn.createStatement()) {
                for (String triggerStatement : triggerStatements) {
                    triggerStatement = triggerStatement.trim();
                    if (!triggerStatement.isEmpty()) {
                        stmt.execute(triggerStatement);
                    }
                }
                System.out.println("Script executed: " + resourcePath);
            }
        } catch (Exception e) {
            System.out.println("Error: " + resourcePath);
            e.printStackTrace();
        }
    }
}
