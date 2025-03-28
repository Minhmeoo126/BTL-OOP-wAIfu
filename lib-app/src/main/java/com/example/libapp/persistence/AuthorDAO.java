package com.example.libapp.persistence;

import com.example.libapp.model.Author;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AuthorDAO {
    public List<Author> getAllAuthors() {
        List<Author> authors = new ArrayList<>();
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;

        try {
            conn = DatabaseConnection.connect();
            stmt = conn.createStatement();
            rs = stmt.executeQuery("SELECT * FROM Author");

            while (rs.next()) {
                Author author = new Author(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("bio")
                );
                authors.add(author);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) rs.close();
                if (stmt != null) stmt.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return authors;
    }

    public void addAuthor(Author author) {
        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            conn = DatabaseConnection.connect();
            String sql = "INSERT INTO Author (name, bio) VALUES (?, ?)";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, author.getName());
            pstmt.setString(2, author.getBio());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (pstmt != null) pstmt.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}