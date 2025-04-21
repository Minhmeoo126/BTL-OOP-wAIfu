package com.example.libapp.controllers;

import com.example.libapp.SessionManager;
import com.example.libapp.viewmodel.MainViewModel;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;

public class MainController {
    private final MainViewModel viewModel = new MainViewModel();
    public Button viewBooks;
    public Button searchBooks;
    public Button myAccount;
    public Button borrowingHistory;
    public Button borrowBooks;
    public Button returnBook;
    public Button Logout;

    @FXML
    private void openBookView() throws IOException {
        viewModel.openBookView();
        loadView("book-view.fxml", viewBooks);
    }

    @FXML
    private void openSearchBook() throws IOException {
        viewModel.openSearchBook();
        loadView("search-book.fxml", searchBooks);
    }

    @FXML
    private void openMyAccount() throws IOException {
        viewModel.openMyAccount();
        loadView("my-account.fxml", myAccount);
    }

    @FXML
    private void openBorrowingHistory() throws IOException {
        viewModel.openBorrowingHistory();
        loadView("borrow-history.fxml", borrowingHistory);
    }

    @FXML
    private void openBorrowBook() throws IOException {
        viewModel.openBorrowBook();
        loadView("borrow-book.fxml", borrowBooks);
    }

    @FXML
    private void openReturnBook() throws IOException {
        viewModel.openReturnBook();
        loadView("return-book.fxml", returnBook);
    }

    @FXML
    private void handleLogout() throws IOException {
        viewModel.logout();
        loadView("login-view.fxml" , Logout);
    }

    private void loadView(String fxmlFile, Button button) throws IOException {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/libapp/view/" + fxmlFile));
            Parent root = loader.load();
            Stage stage = (Stage) button.getScene().getWindow();
            stage.setScene(new Scene(root, 900, 600));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}