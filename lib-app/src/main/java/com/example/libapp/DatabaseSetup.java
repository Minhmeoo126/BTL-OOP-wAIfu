package com.example.libapp;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.DatabaseMetaData;

public class DatabaseSetup {
    private static final String URL = "jdbc:mysql://localhost:3306/lib?serverTimezone=UTC";
    private static final String USER = "root";
    private static final String PASSWORD = "meomeomeo";

    public static void main(String[] args) {
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD)) {
            System.out.println("Database connected successfully!");

            // Đảm bảo auto-commit được bật
            conn.setAutoCommit(true);

            // Chạy script tạo bảng
            runSQLScript(conn, "src/main/resources/com/example/libapp/sql/create-table.sql");

            // Chạy script tạo trigger
            runTriggerScript(conn, "src/main/resources/com/example/libapp/sql/create-triggers.sql");

            // Đồng bộ hóa cơ sở dữ liệu
            //synchronizeDatabase(conn);

            // Chạy script chèn dữ liệu
            runSQLScript(conn, "src/main/resources/com/example/libapp/sql/insert-data.sql");

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

    private static void synchronizeDatabase(Connection conn) throws SQLException {
        try {
            // Lấy DatabaseMetaData để làm mới metadata của cơ sở dữ liệu
            DatabaseMetaData metaData = conn.getMetaData();

            // Kiểm tra danh sách bảng để đảm bảo metadata được làm mới
            System.out.println("Synchronizing database metadata...");
            try (var rs = metaData.getTables(null, null, null, new String[]{"TABLE"})) {
                while (rs.next()) {
                    String tableName = rs.getString("TABLE_NAME");
                    System.out.println("Found table: " + tableName);
                }
            }

            // Thêm độ trễ nhỏ (nếu cần)
            Thread.sleep(1000);

            System.out.println("Database synchronization completed!");
        } catch (InterruptedException e) {
            System.out.println("Synchronization interrupted: " + e.getMessage());
        }
    }
}