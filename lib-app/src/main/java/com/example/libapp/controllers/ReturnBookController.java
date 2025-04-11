package com.example.libapp.controllers;

import com.example.libapp.model.BorrowingRecord;
import com.example.libapp.persistence.BorrowingRecordDAO;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import com.example.libapp.persistence.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.time.LocalDate;

public class ReturnBookController {
    @FXML
    private TextField recordIdField;
    @FXML
    private Label messageLabel;

    private BorrowingRecordDAO borrowingRecordDAO = new BorrowingRecordDAO();
}
/**
    @FXML
    private void handleReturn() {
        try {
            int recordId = Integer.parseInt(recordIdField.getText().trim());
            for (BorrowingRecord record : borrowingRecordDAO.getAllBorrowingRecords()) {
                if (record.getId() == recordId && record.getUserId() == LoginController.getLoggedInUser().getId()) {
                    if (record.getReturnDate() != null) {
                        messageLabel.setText("Book already returned.");
                        messageLabel.setStyle("-fx-text-fill: red;");
                        return;
                    }

                    // Cập nhật return_date
                    try (Connection conn = DatabaseConnection.connect();
                         PreparedStatement pstmt = conn.prepareStatement(
                                 "UPDATE BorrowingRecord SET return_date = ? WHERE id = ?")) {
                        pstmt.setString(1, LocalDate.now().toString());
                        pstmt.setInt(2, recordId);
                        pstmt.executeUpdate();
                    }

                    messageLabel.setText("Book returned successfully!");
                    messageLabel.setStyle("-fx-text-fill: green;");
                    return;
                }
            }
            messageLabel.setText("Invalid record ID or not your borrowing record.");
            messageLabel.setStyle("-fx-text-fill: red;");
        } catch (NumberFormatException e) {
            messageLabel.setText("Please enter a valid Record ID.");
            messageLabel.setStyle("-fx-text-fill: red;");
        } catch (Exception e) {
            messageLabel.setText("Error: " + e.getMessage());
            messageLabel.setStyle("-fx-text-fill: red;");
        }
    }

    @FXML
    private void backToMain() {
        Stage stage = (Stage) recordIdField.getScene().getWindow();
        stage.close();
    }
}
 */