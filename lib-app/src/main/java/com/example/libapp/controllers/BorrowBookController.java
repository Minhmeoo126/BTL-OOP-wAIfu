package com.example.libapp.controllers;

import com.example.libapp.model.BorrowingRecord;
import com.example.libapp.persistence.BorrowingRecordDAO;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.time.LocalDate;

public class BorrowBookController {
    @FXML
    private TextField bookIdField;
    @FXML
    private Label messageLabel;

    private BorrowingRecordDAO borrowingRecordDAO = new BorrowingRecordDAO();

    @FXML
    private void handleBorrow() {
        try {
            int bookId = Integer.parseInt(bookIdField.getText().trim());
            BorrowingRecord record = new BorrowingRecord();
            record.setUserId(LoginController.getLoggedInUser().getId());
            record.setBookId(bookId);
            record.setBorrowDate(LocalDate.now().toString());
            record.setReturnDate(null);

            borrowingRecordDAO.addBorrowingRecord(record);
            messageLabel.setText("Book borrowed successfully!");
            messageLabel.setStyle("-fx-text-fill: green;");
        } catch (NumberFormatException e) {
            messageLabel.setText("Please enter a valid Book ID.");
            messageLabel.setStyle("-fx-text-fill: red;");
        } catch (Exception e) {
            messageLabel.setText("Error: " + e.getMessage());
            messageLabel.setStyle("-fx-text-fill: red;");
        }
    }

    @FXML
    private void backToMain() {
        Stage stage = (Stage) bookIdField.getScene().getWindow();
        stage.close();
    }
}