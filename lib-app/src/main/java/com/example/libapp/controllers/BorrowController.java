package com.example.libapp.controllers;

import com.example.libapp.viewmodel.BorrowViewModel;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

public class BorrowController {
    @FXML
    private TextField userIdField;
    @FXML
    private TextField bookIdField;

    private BorrowViewModel borrowViewModel = new BorrowViewModel();

    @FXML
    private void handleBorrowBook() {
        int userId = Integer.parseInt(userIdField.getText());
        int bookId = Integer.parseInt(bookIdField.getText());
        borrowViewModel.borrowBook(userId, bookId);
    }

    @FXML
    private void handleReturnBook() {
        int userId = Integer.parseInt(userIdField.getText());
        int bookId = Integer.parseInt(bookIdField.getText());
        borrowViewModel.returnBook(userId, bookId);
    }
}