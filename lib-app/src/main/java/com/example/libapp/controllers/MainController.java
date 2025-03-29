package com.example.libapp.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class MainController {
    @FXML
    private void openBookView() throws IOException {
        loadView("book-view.fxml", "View Books");
    }

    @FXML
    private void openSearchBook() throws IOException {
        loadView("search-book.fxml", "Search Books");
    }

    @FXML
    private void openMyAccount() throws IOException {
        loadView("my-account.fxml", "My Account");
    }

    @FXML
    private void openBorrowingHistory() throws IOException {
        loadView("borrowing-history.fxml", "Borrowing History");
    }

    @FXML
    private void openBorrowBook() throws IOException {
        loadView("borrow-book.fxml", "Borrow Book");
    }

    @FXML
    private void openReturnBook() throws IOException {
        loadView("return-book.fxml", "Return Book");
    }

    @FXML
    private void handleLogout() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/libapp/login.fxml"));
        Parent root = loader.load();

        // Lấy stage hiện tại từ một nút bất kỳ trong cửa sổ hiện tại
        Stage stage = (Stage) root.getScene().getWindow();

        stage.setScene(new Scene(root));
        stage.setTitle("Login to Library App");
    }

    private void loadView(String fxmlFile, String title) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/libapp/" + fxmlFile));
        Parent root = loader.load();
        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.setTitle(title);
        stage.show();
    }
}