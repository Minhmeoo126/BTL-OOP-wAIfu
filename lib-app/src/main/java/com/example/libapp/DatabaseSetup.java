package com.example.libapp;

import com.example.libapp.persistence.DatabaseConnection;
import java.sql.Connection;
import java.sql.SQLException;

public class DatabaseSetup {
    public static void main(String[] args) {
        try (Connection conn = DatabaseConnection.connect()) {
            System.out.println("Database connected successfully!");
            System.out.println("Database setup completed!");
        } catch (SQLException e) {
            System.out.println("Database connection error:");
            e.printStackTrace();
        }
    }
}