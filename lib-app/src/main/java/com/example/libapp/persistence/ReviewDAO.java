package com.example.libapp.persistence;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Date;

public class ReviewDAO {
    public void addReview(int userId, int bookId, int rating, String comment) {
        String sql = "INSERT INTO Reviews (user_id, book_id, rating, comment, review_date) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            stmt.setInt(2, bookId);
            stmt.setInt(3, rating);
            stmt.setString(4, comment);
            stmt.setDate(5, new java.sql.Date(new Date().getTime()));
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}