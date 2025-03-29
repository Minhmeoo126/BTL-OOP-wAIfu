package com.example.libapp.persistence;

import com.example.libapp.model.BorrowingRecord;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

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

            String sql = "SELECT * FROM BorrowingRecord";
            pstmt = conn.prepareStatement(sql);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                BorrowingRecord record = new BorrowingRecord();
                record.setId(rs.getInt("id"));
                record.setUserId(rs.getInt("user_id"));
                record.setBookId(rs.getInt("book_id"));
                record.setBorrowDate(rs.getString("borrow_date"));
                record.setReturnDate(rs.getString("return_date"));
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
}