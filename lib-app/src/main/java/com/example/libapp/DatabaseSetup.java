package com.example.libapp;

import com.example.libapp.persistence.DatabaseConnection;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.stream.Collectors;

public class DatabaseSetup {
    public static void main(String[] args) {
        try (Connection conn = DatabaseConnection.connect()) {
            System.out.println("Database connected successfully!");

            // Đảm bảo auto-commit được bật
            conn.setAutoCommit(true);

            // Đường dẫn tài nguyên
            String dropTablePath = "/sql/drop-table.sql";
            String createTablePath = "/sql/create-table.sql";
            String createTriggersPath = "/sql/create-triggers.sql";
            String insertDataPath = "/sql/insert-data.sql";

            // Chạy script xóa bảng
            runSQLScript(conn, dropTablePath);
            // Chạy script tạo bảng
            runSQLScript(conn, createTablePath);
            // Chạy script tạo trigger
            runTriggerScript(conn, createTriggersPath);
            // Chạy script chèn dữ liệu
            runSQLScript(conn, insertDataPath);

            System.out.println("Database setup completed!");
        } catch (SQLException e) {
            System.out.println("Database connection error:");
            e.printStackTrace();
        }
    }

    private static void runSQLScript(Connection conn, String resourcePath) throws SQLException {
        try {
            // Thử nhiều cách khác nhau để tải tài nguyên
            InputStream inputStream = DatabaseSetup.class.getResourceAsStream(resourcePath);

            if (inputStream == null) {
                // Thử với tiền tố /com/example/libapp
                inputStream = DatabaseSetup.class.getResourceAsStream("/com/example/libapp" + resourcePath);
            }

            if (inputStream == null) {
                // Thử với ClassLoader
                inputStream = DatabaseSetup.class.getClassLoader().getResourceAsStream("com/example/libapp" + resourcePath);
            }

            if (inputStream == null) {
                // Thử với ClassLoader không có tiền tố
                inputStream = DatabaseSetup.class.getClassLoader().getResourceAsStream(resourcePath.substring(1));
            }

            if (inputStream == null) {
                System.out.println("ERROR: Không thể tìm thấy tài nguyên: " + resourcePath);
                System.out.println("Vui lòng kiểm tra cấu trúc thư mục resources và đường dẫn.");
                return;
            }

            // Đọc nội dung file SQL
            String sql = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))
                    .lines().collect(Collectors.joining("\n"));

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
                System.out.println("Script executed successfully: " + resourcePath);
            }
        } catch (Exception e) {
            System.out.println("Error executing script: " + resourcePath);
            e.printStackTrace();
        }
    }

    private static void runTriggerScript(Connection conn, String resourcePath) throws SQLException {
        try {
            // Thử nhiều cách khác nhau để tải tài nguyên
            InputStream inputStream = DatabaseSetup.class.getResourceAsStream(resourcePath);

            if (inputStream == null) {
                // Thử với tiền tố /com/example/libapp
                inputStream = DatabaseSetup.class.getResourceAsStream("/com/example/libapp" + resourcePath);
            }

            if (inputStream == null) {
                // Thử với ClassLoader
                inputStream = DatabaseSetup.class.getClassLoader().getResourceAsStream("com/example/libapp" + resourcePath);
            }

            if (inputStream == null) {
                // Thử với ClassLoader không có tiền tố
                inputStream = DatabaseSetup.class.getClassLoader().getResourceAsStream(resourcePath.substring(1));
            }

            if (inputStream == null) {
                System.out.println("ERROR: Không thể tìm thấy tài nguyên: " + resourcePath);
                System.out.println("Vui lòng kiểm tra cấu trúc thư mục resources và đường dẫn.");
                return;
            }

            // Đọc nội dung file SQL
            String sql = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))
                    .lines().collect(Collectors.joining("\n"));

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
                System.out.println("Trigger script executed successfully: " + resourcePath);
            }
        } catch (Exception e) {
            System.out.println("Error executing trigger script: " + resourcePath);
            e.printStackTrace();
        }
    }
}