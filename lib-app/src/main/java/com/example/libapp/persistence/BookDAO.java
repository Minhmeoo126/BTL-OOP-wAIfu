package com.example.libapp.persistence;

import com.example.libapp.model.Book;
import javafx.scene.control.Label;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BookDAO {

    public void addBook(Book book) {
        if (!isValidBook(book)) {
            System.err.println("Dữ liệu sách không hợp lệ. Không thể thêm vào cơ sở dữ liệu.");
            return;
        }

        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            conn = DatabaseConnection.connect();

            String sql = "INSERT INTO Book (title, isbn, author_id, category_id, total_copies, available_copies, description, thumbnail) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, book.getTitle());
            pstmt.setString(2, book.getIsbn());
            pstmt.setInt(3, book.getAuthorId());
            pstmt.setInt(4, book.getCategoryId());
            pstmt.setInt(5, book.getTotalCopies());
            pstmt.setInt(6, book.getAvailableCopies());
            pstmt.setString(7, book.getDescription());
            pstmt.setString(8, book.getThumbnail());
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

            String sql = "SELECT b.id, b.title, b.isbn, b.author_id, a.name AS author_name, " +
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
                book.setIsbn(rs.getString("isbn"));
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

    public Book getBookById(int bookId) {
        Book book = null;
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = DatabaseConnection.connect();

            String sql = "SELECT b.id, b.title, b.isbn, b.author_id, a.name AS author_name, " +
                    "b.category_id, c.name AS category_name, b.total_copies, b.available_copies, b.description, b.thumbnail " +
                    "FROM Book b " +
                    "JOIN Author a ON b.author_id = a.id " +
                    "JOIN Category c ON b.category_id = c.id " +
                    "WHERE b.id = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, bookId);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                book = new Book();
                book.setId(rs.getInt("id"));
                book.setTitle(rs.getString("title"));
                book.setIsbn(rs.getString("isbn"));
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

            String sql = "SELECT b.id, b.title, b.isbn, b.author_id, a.name AS author_name, " +
                    "b.category_id, c.name AS category_name, b.total_copies, b.available_copies, b.description, b.thumbnail " +
                    "FROM Book b " +
                    "JOIN Author a ON b.author_id = a.id " +
                    "JOIN Category c ON b.category_id = c.id " +
                    "WHERE LOWER(b.title) = LOWER(?)";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, title);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                book = new Book();
                book.setId(rs.getInt("id"));
                book.setTitle(rs.getString("title"));
                book.setIsbn(rs.getString("isbn"));
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

    public Book getBookByISBN(String isbn) {
        Book book = null;
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = DatabaseConnection.connect();

            String sql = "SELECT b.id, b.title, b.isbn, b.author_id, a.name AS author_name, " +
                    "b.category_id, c.name AS category_name, b.total_copies, b.available_copies, b.description, b.thumbnail " +
                    "FROM Book b " +
                    "JOIN Author a ON b.author_id = a.id " +
                    "JOIN Category c ON b.category_id = c.id " +
                    "WHERE b.isbn = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, isbn);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                book = new Book();
                book.setId(rs.getInt("id"));
                book.setTitle(rs.getString("title"));
                book.setIsbn(rs.getString("isbn"));
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

    public void addOrUpdateBookByIsbn(Book book) {
        Book existing = getBookByISBN(book.getIsbn());

        if (existing != null) {
            int newTotal = existing.getTotalCopies() + book.getTotalCopies();
            int newAvailable = existing.getAvailableCopies() + book.getAvailableCopies();

            try (Connection conn = DatabaseConnection.connect();
                 PreparedStatement pstmt = conn.prepareStatement(
                         "UPDATE Book SET total_copies = ?, available_copies = ? WHERE isbn = ?")) {

                pstmt.setInt(1, newTotal);
                pstmt.setInt(2, newAvailable);
                pstmt.setString(3, book.getIsbn());
                pstmt.executeUpdate();

            } catch (SQLException e) {
                e.printStackTrace();
            }

        } else {
            addBook(book);
        }
    }

    public boolean deleteBookByIsbn(String isbn) {
        Book book = getBookByISBN(isbn);
        if (book == null) return false;

        if (book.getAvailableCopies() < book.getTotalCopies()) {
            return false;
        }

        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement pstmt = conn.prepareStatement("DELETE FROM Book WHERE isbn = ?")) {
            pstmt.setString(1, isbn);
            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
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

    public void updateBook(Book book) {
        String sql = """
        UPDATE Book SET
            title = ?,
            author_id = ?,
            category_id = ?,
            total_copies = ?,
            available_copies = ?,
            description = ?,
            thumbnail = ?
        WHERE isbn = ?;
    """;

        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, book.getTitle());
            pstmt.setInt(2, book.getAuthorId());
            pstmt.setInt(3, book.getCategoryId());
            pstmt.setInt(4, book.getTotalCopies());
            pstmt.setInt(5, book.getAvailableCopies());
            pstmt.setString(6, book.getDescription());
            pstmt.setString(7, book.getThumbnail());
            pstmt.setString(8, book.getIsbn());

            int rowsUpdated = pstmt.executeUpdate();

            if (rowsUpdated == 0) {
                System.out.println("Không có sách nào được cập nhật với ISBN: " + book.getIsbn());
            } else {
                System.out.println("Cập nhật sách thành công (ISBN: " + book.getIsbn() + ")");
            }

        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Lỗi khi cập nhật sách: " + e.getMessage());
        }
    }

    private boolean isValidBook(Book book) {
        if (book == null) return false;
        if (book.getTitle() == null || book.getTitle().trim().isEmpty()) return false;
        if (book.getIsbn() == null || book.getIsbn().trim().isEmpty()) return false;
        if (book.getAuthorId() <= 0) return false;
        if (book.getCategoryId() <= 0) return false;
        if (book.getTotalCopies() <= 0) return false;
        if (book.getAvailableCopies() < 0 || book.getAvailableCopies() > book.getTotalCopies()) return false;
        return true;
    }


    public void updateBookCopies(int bookId, int newTotalCopies , int newAvailableCopies) {
        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            conn = DatabaseConnection.connect();
            String sql = "UPDATE Book SET total_copies = ?,available_copies = ? WHERE id = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1,newTotalCopies);
            pstmt.setInt(2, newAvailableCopies);
            pstmt.setInt(3, bookId);
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

    public static boolean updateBookInfo(int bookID ,String thumbnail, String title, int category_id, int author_id, String description, Label messageLabel) {
        // Câu lệnh SQL để cập nhật thông tin người dùng
        String sql = "UPDATE Book SET title = ?, category_id = ?, author_id = ?, description = ? , thumbnail = ? WHERE id = ?";

        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            // Thiết lập các tham số cho câu lệnh SQL
            stmt.setString(1, title);
            stmt.setInt(2, category_id);
            stmt.setInt(3, author_id);
            stmt.setString(4, description);
            stmt.setString(5,thumbnail);
            stmt.setInt(6 ,bookID);
            stmt.executeUpdate();
            // Thực thi câu lệnh SQL
            int rowsUpdated = stmt.executeUpdate();

            // Cập nhật giao diện người dùng sau khi cập nhật cơ sở dữ liệu
            if (rowsUpdated > 0) {
                System.out.println("Success");
                messageLabel.setText("Success");
                return true;
            } else {
                System.out.println("Fail");
                messageLabel.setText("Fail");
                return false;
            }

        } catch (SQLException e) {
            // Xử lý lỗi nếu có
            e.printStackTrace();
            messageLabel.setText("Lỗi khi cập nhật thông tin");
            return false;
        }
    }

    public List<Book> getListBookByTitle(String title) {
        List<Book> allBookByTitle = new ArrayList<>();
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = DatabaseConnection.connect();

            String sql = "SELECT b.id, b.title, b.isbn, b.author_id, a.name AS author_name, " +
                    "b.category_id, c.name AS category_name, b.total_copies, b.available_copies, b.description, b.thumbnail " +
                    "FROM Book b " +
                    "JOIN Author a ON b.author_id = a.id " +
                    "JOIN Category c ON b.category_id = c.id " +
                    "WHERE LOWER(b.title) = LOWER(?)";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, title);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                Book book = new Book();
                book.setId(rs.getInt("id"));
                book.setTitle(rs.getString("title"));
                book.setIsbn(rs.getString("isbn"));
                book.setAuthorId(rs.getInt("author_id"));
                book.setAuthorName(rs.getString("author_name"));
                book.setCategoryId(rs.getInt("category_id"));
                book.setCategoryName(rs.getString("category_name"));
                book.setTotalCopies(rs.getInt("total_copies"));
                book.setAvailableCopies(rs.getInt("available_copies"));
                book.setDescription(rs.getString("description"));
                book.setThumbnail(rs.getString("thumbnail"));
                allBookByTitle.add(book);
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

        return allBookByTitle;
    }
}
