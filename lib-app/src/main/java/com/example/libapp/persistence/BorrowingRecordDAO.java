package com.example.libapp.persistence;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Date;

public class BorrowingRecordDAO {
    public void borrowBook(int userId, int bookId) {
        String sql = "INSERT INTO BorrowingRecord (user_id, book_id, borrow_date) VALUES (?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            stmt.setInt(2, bookId);
            stmt.setDate(3, new java.sql.Date(new Date().getTime()));
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void returnBook(int userId, int bookId) {
        String sql = "UPDATE BorrowingRecord SET return_date = ? WHERE user_id = ? AND book_id = ? AND return_date IS NULL";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setDate(1, new java.sql.Date(new Date().getTime()));
            stmt.setInt(2, userId);
            stmt.setInt(3, bookId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}