package com.example.libapp.persistence;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    private static final String URL = "jdbc:mysql://localhost:3306/lib"; //
    private static final String USER = "root";  //
    private static final String PASSWORD = "meomeomeo";

    public static Connection connect() {
        Connection connection = null;
        try {
            connection = DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println(" Kết nối thành công!");
        } catch (SQLException e) {
            System.out.println(" Kết nối thất bại!");
            e.printStackTrace();
        }
        return connection;
    }

    public static void main(String[] args) {
        connect(); // Test kết nối
    }
}
