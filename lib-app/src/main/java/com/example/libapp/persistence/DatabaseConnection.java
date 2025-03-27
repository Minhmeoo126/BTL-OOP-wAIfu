package com.example.libapp.persistence;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    private static final String URL = "jdbc:mysql://localhost:3306/lib?serverTimezone=UTC";
    private static final String USER = "root";
    private static final String PASSWORD = "meomeomeo";

    public static Connection connect() {
        try {
            Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println("Kết nối MySQL thành công!");
            return connection;
        } catch (SQLException e) {
            System.err.println("Kết nối thất bại! Kiểm tra lại thông tin đăng nhập.");
            e.printStackTrace();
            return null;
        }
    }

    public static void main(String[] args) {
        try (Connection conn = connect()) {
            if (conn != null) {
                System.out.println("Kết nối kiểm tra hoạt động tốt.");
            } else {
                System.out.println("Kiểm tra lại thông tin MySQL.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
