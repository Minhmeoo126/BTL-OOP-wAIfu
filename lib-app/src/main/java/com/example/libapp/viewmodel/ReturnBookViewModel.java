package com.example.libapp.viewmodel;

import com.example.libapp.model.BorrowingRecord;
import com.example.libapp.persistence.BorrowingRecordDAO;
import com.example.libapp.controllers.LoginController;
import com.example.libapp.persistence.DatabaseConnection;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.time.LocalDate;

public class ReturnBookViewModel {
    private final BorrowingRecordDAO borrowingRecordDAO = new BorrowingRecordDAO();
    private final StringProperty recordId = new SimpleStringProperty();
    private final StringProperty message = new SimpleStringProperty();

    public StringProperty recordIdProperty() {
        return recordId;
    }

    public StringProperty messageProperty() {
        return message;
    }

    public void returnBook() {
        try {
            int recordIdValue = Integer.parseInt(recordId.get().trim());
            for (BorrowingRecord record : borrowingRecordDAO.getAllBorrowingRecords()) {
                if (record.getId() == recordIdValue && record.getUserId() == LoginController.getLoggedInUser().getId()) {
                    if (record.getReturnDate() != null) {
                        message.set("Book already returned.");
                        return;
                    }

                    // Cập nhật return_date
                    try (Connection conn = DatabaseConnection.connect();
                         PreparedStatement pstmt = conn.prepareStatement(
                                 "UPDATE BorrowingRecord SET return_date = ? WHERE id = ?")) {
                        pstmt.setString(1, LocalDate.now().toString());
                        pstmt.setInt(2, recordIdValue);
                        pstmt.executeUpdate();
                    }

                    message.set("Book returned successfully!");
                    return;
                }
            }
            message.set("Invalid record ID or not your borrowing record.");
        } catch (NumberFormatException e) {
            message.set("Please enter a valid Record ID.");
        } catch (Exception e) {
            message.set("Error: " + e.getMessage());
        }
    }
}