package com.example.libapp;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.io.IOException;

public class DatabaseSetup {
    private static final String DB_URL = "jdbc:mysql://localhost:3306/lib?serverTimezone=UTC";
    private static final String USER = "root";  // Cập nhật user nếu khác
    private static final String PASSWORD = "meomeomeo";  // Cập nhật password nếu có

    public static void main(String[] args) {
        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASSWORD)) {
            System.out.println("Kết nối database thành công!");

            runSQLScript(conn, "src/main/resources/sql/create-table.sql");
            runSQLScript(conn, "src/main/resources/sql/create-triggers.sql");
            runSQLScript(conn, "src/main/resources/sql/insert-data.sql");

            System.out.println("Database setup hoàn tất!");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void runSQLScript(Connection conn, String filePath) {
        try {
            String sql = new String(Files.readAllBytes(Paths.get(filePath)));
            Statement stmt = conn.createStatement();
            stmt.execute(sql);
            System.out.println("Đã chạy script: " + filePath);
        } catch (IOException | SQLException e) {
            System.err.println("Lỗi khi chạy script: " + filePath);
            e.printStackTrace();
        }
    }
}
