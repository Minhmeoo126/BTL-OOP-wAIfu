package com.example.libapp.persistence;

import com.example.libapp.model.Book;
import com.example.libapp.model.BorrowingRecord;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BorrowingRecordDAO {

    public void addBorrowingRecord(BorrowingRecord record) {
        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            conn = DatabaseConnection.connect();

            String sql = "INSERT INTO BorrowingRecord (user_id, book_id, borrow_date, return_date) VALUES (?, ?, ?, ?)";
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, record.getUserId());
            pstmt.setInt(2, record.getBookId());
            pstmt.setString(3, record.getBorrowDate());
            pstmt.setString(4, record.getReturnDate());
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

    public List<BorrowingRecord> getAllBorrowingRecords() {
        List<BorrowingRecord> records = new ArrayList<>();
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = DatabaseConnection.connect();

            String sql = "SELECT br.id, br.user_id, br.book_id, br.borrow_date, br.return_date, b.title, a.name "
                    + "FROM BorrowingRecord br "
                    + "JOIN Book b ON br.book_id = b.id "
                    + "JOIN Author a ON b.author_id = a.id";
            pstmt = conn.prepareStatement(sql);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                BorrowingRecord record = new BorrowingRecord();
                record.setId(rs.getInt("id"));
                record.setUserId(rs.getInt("user_id"));
                record.setBookId(rs.getInt("book_id"));
                record.setBorrowDate(rs.getString("borrow_date"));
                record.setReturnDate(rs.getString("return_date"));
                record.setBookName(rs.getString("title"));
                record.setAuthorName(rs.getString("name"));

                records.add(record);
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
        return records;
    }

    public void updateBorrowingRecord(BorrowingRecord record) {
        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            conn = DatabaseConnection.connect();

            // Cập nhật ngày trả sách dựa trên user_id và book_id
            String sql = "UPDATE BorrowingRecord SET return_date = ? WHERE user_id = ? AND book_id = ? AND return_date IS NULL";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, record.getReturnDate());  // Cập nhật ngày trả sách
            pstmt.setInt(2, record.getUserId());  // user_id
            pstmt.setInt(3, record.getBookId());  // book_id
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