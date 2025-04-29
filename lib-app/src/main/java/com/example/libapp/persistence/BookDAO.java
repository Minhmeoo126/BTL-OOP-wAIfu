package com.example.libapp.persistence;

import com.example.libapp.model.Book;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BookDAO {
    public void addBook(Book book) {
        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            conn = DatabaseConnection.connect();

            String sql = "INSERT INTO Book (title, author_id, category_id, total_copies, available_copies, description,thumbnail) VALUES (?, ?, ?, ?, ?, ? ,?)";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, book.getTitle());
            pstmt.setInt(2, book.getAuthorId());
            pstmt.setInt(3, book.getCategoryId());
            pstmt.setInt(4, book.getTotalCopies());
            pstmt.setInt(5, book.getAvailableCopies());
            pstmt.setString(6, book.getDescription()); // Can be null
            pstmt.setString(7,book.getThumbnail());
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

    public List<Book> getAllBooks() {
        List<Book> books = new ArrayList<>();
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = DatabaseConnection.connect();

            String sql = "SELECT b.id, b.title, b.author_id, a.name AS author_name, " +
                    "b.category_id, c.name AS category_name, b.total_copies, b.available_copies, b.description, b.thumbnail " +
                    "FROM Book b " +
                    "JOIN Author a ON b.author_id = a.id " +
                    "JOIN Category c ON b.category_id = c.id";
            pstmt = conn.prepareStatement(sql);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                Book book = new Book();
                book.setId(rs.getInt("id"));
                book.setTitle(rs.getString("title"));
                book.setAuthorId(rs.getInt("author_id"));
                book.setAuthorName(rs.getString("author_name"));
                book.setCategoryId(rs.getInt("category_id"));
                book.setCategoryName(rs.getString("category_name"));
                book.setTotalCopies(rs.getInt("total_copies"));
                book.setAvailableCopies(rs.getInt("available_copies"));
                book.setDescription(rs.getString("description"));
                book.setThumbnail(rs.getString("thumbnail"));
                books.add(book);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) rs.close();
                if (pstmt != null) pstmt.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return books;
    }

    public void updateBookAvailableCopies(int bookId, int newAvailableCopies) {
        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            conn = DatabaseConnection.connect();
            String sql = "UPDATE Book SET available_copies = ? WHERE id = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, newAvailableCopies);
            pstmt.setInt(2, bookId);
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

    public Book getBookById(int bookId) {
        Book book = null;
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = DatabaseConnection.connect();

            String sql = "SELECT b.id, b.title, b.author_id, a.name AS author_name, "
                    + "b.category_id, c.name AS category_name, b.total_copies, b.available_copies, b.description, b.thumbnail "
                    + "FROM Book b "
                    + "JOIN Author a ON b.author_id = a.id "
                    + "JOIN Category c ON b.category_id = c.id "
                    + "WHERE b.id = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, bookId);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                book = new Book();
                book.setId(rs.getInt("id"));
                book.setTitle(rs.getString("title"));
                book.setAuthorId(rs.getInt("author_id"));
                book.setAuthorName(rs.getString("author_name"));
                book.setCategoryId(rs.getInt("category_id"));
                book.setCategoryName(rs.getString("category_name"));
                book.setTotalCopies(rs.getInt("total_copies"));
                book.setAvailableCopies(rs.getInt("available_copies"));
                book.setDescription(rs.getString("description"));
                book.setThumbnail(rs.getString("thumbnail"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) rs.close();
                if (pstmt != null) pstmt.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return book;
    }

    public Book getBookByTitle(String title) {
        Book book = null;
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = DatabaseConnection.connect();

            String sql = "SELECT b.id, b.title, b.author_id, a.name AS author_name, "
                    + "b.category_id, c.name AS category_name, b.total_copies, b.available_copies, b.description, b.thumbnail "
                    + "FROM Book b "
                    + "JOIN Author a ON b.author_id = a.id "
                    + "JOIN Category c ON b.category_id = c.id "
                    + "WHERE LOWER(b.title) = LOWER(?)";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, title);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                book = new Book();
                book.setId(rs.getInt("id"));
                book.setTitle(rs.getString("title"));
                book.setAuthorId(rs.getInt("author_id"));
                book.setAuthorName(rs.getString("author_name"));
                book.setCategoryId(rs.getInt("category_id"));
                book.setCategoryName(rs.getString("category_name"));
                book.setTotalCopies(rs.getInt("total_copies"));
                book.setAvailableCopies(rs.getInt("available_copies"));
                book.setDescription(rs.getString("description"));
                book.setThumbnail(rs.getString("thumbnail"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) rs.close();
                if (pstmt != null) pstmt.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return book;
    }

}