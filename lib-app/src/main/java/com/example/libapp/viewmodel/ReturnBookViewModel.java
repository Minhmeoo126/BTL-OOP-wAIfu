package com.example.libapp.viewmodel;

import com.example.libapp.model.BorrowingRecord;
import com.example.libapp.persistence.BorrowingRecordDAO;
import com.example.libapp.persistence.DatabaseConnection;
import com.example.libapp.model.User;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;

public class ReturnBookViewModel {
    private final BorrowingRecordDAO borrowingRecordDAO = new BorrowingRecordDAO();
    private final StringProperty message = new SimpleStringProperty("");
    private User loggedInUser;
    public StringProperty messageProperty() {
        return message;
    }

    public void setLoggedInUser(User user) {
        this.loggedInUser = user;
    }

    public void returnBook(String recordIdText) {
        try {
            int recordId = Integer.parseInt(recordIdText.trim());
            if (loggedInUser == null) {
                message.set("Please log in to return a book.");
                return;
            }

            for (BorrowingRecord record : borrowingRecordDAO.getAllBorrowingRecords()) {
                if (record.getId() == recordId && record.getUserId() == loggedInUser.getId()) {
                    if (record.getReturnDate() != null) {
                        message.set("Book already returned.");
                        return;
                    }

                    try (java.sql.Connection conn = DatabaseConnection.connect();
                         java.sql.PreparedStatement pstmt = conn.prepareStatement(
                                 "UPDATE BorrowingRecord SET return_date = ? WHERE id = ?")) {
                        pstmt.setString(1, java.time.LocalDate.now().toString());
                        pstmt.setInt(2, recordId);
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