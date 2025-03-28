package com.example.libapp.persistence;

import com.example.libapp.model.Book;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class BookDAO {
    public void addBook(String title, String authorName, String categoryName, int totalCopies, int availableCopies) {
        String sql = "INSERT INTO Book (title, author_id, category_id, total_copies, available_copies) " +
                "VALUES (?, (SELECT id FROM Author WHERE name = ?), (SELECT id FROM Category WHERE name = ?), ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, title);
            stmt.setString(2, authorName);
            stmt.setString(3, categoryName);
            stmt.setInt(4, totalCopies);
            stmt.setInt(5, availableCopies);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}