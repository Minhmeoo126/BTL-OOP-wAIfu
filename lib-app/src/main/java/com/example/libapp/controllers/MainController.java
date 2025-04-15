package com.example.libapp.controllers;

import com.example.libapp.SessionManager;
import com.example.libapp.viewmodel.MainViewModel;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class MainController {
    private final MainViewModel viewModel = new MainViewModel();

    @FXML
    private void openBookView() throws IOException {
        viewModel.openBookView();
        loadView("book-view.fxml", "View Books");
    }

    @FXML
    private void openSearchBook() throws IOException {
        viewModel.openSearchBook();
        loadView("search-book.fxml", "Search Books");
    }

    @FXML
    private void openMyAccount() throws IOException {
        viewModel.openMyAccount();
        loadView("my-account.fxml", "My Account");
    }

    @FXML
    private void openBorrowingHistory() throws IOException {
        viewModel.openBorrowingHistory();
        loadView("borrowing-history.fxml", "Borrowing History");
    }

    @FXML
    private void openBorrowBook() throws IOException {
        viewModel.openBorrowBook();
        loadView("borrow-book.fxml", "Borrow Book");
    }

    @FXML
    private void openReturnBook() throws IOException {
        viewModel.openReturnBook();
        loadView("return-book.fxml", "Return Book");
    }

    @FXML
    private void handleLogout() throws IOException {
        viewModel.logout();
        SessionManager.getInstance().clearSession();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/libapp/login-view.fxml"));
        Parent root = loader.load();
        Stage stage = (Stage) root.getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.setTitle("Login to Library App");
    }

    private void loadView(String fxmlFile, String title) throws IOException {
        if (SessionManager.getInstance().getLoggedInUser() == null) {
            throw new IllegalStateException("User not logged in");
        }
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/libapp/" + fxmlFile));
        Parent root = loader.load();
        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.setTitle(title);
        stage.show();
    }
}