package com.example.libapp.controllers;

import com.example.libapp.SessionManager;
import com.example.libapp.model.User;
import com.example.libapp.utils.SceneNavigator;
import com.example.libapp.viewmodel.MainViewModel;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.IOException;

import static com.example.libapp.utils.SceneNavigator.loadView;

public class MainController {
    @FXML
    public Button AI;
    @FXML
    public Button viewBooks;
    @FXML
    public Button searchBooks;
    @FXML
    public Button myAccount;
    @FXML
    public Button borrowingHistory;
    @FXML
    public Button borrowBooks;
    @FXML
    public Button returnBook;
    @FXML
    public Button Logout;
    @FXML
    public Button addBook;
    @FXML
    public Button logout;

    private final MainViewModel viewModel = new MainViewModel();
    public Button bookManagement;
    public Button userManagement;
    public Button backToMain;
    public Label UserName;

    public void initialize() {
        User currentUser = SessionManager.getInstance().getLoggedInUser();
        if (currentUser != null) {
            UserName.setText(currentUser.getUsername());
        } else {
            UserName.setText("khong co nguoi dung");
        }
    }

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
    private void openAI() throws IOException {
        viewModel.openAI();
        loadView("AI-view.fxml", AI);
    }

    public void addNewBook() throws IOException {
        viewModel.openAddBook();
        loadView("add-book-view.fxml",addBook);
    }

    public void Logout() throws IOException {
        viewModel.logout();
        loadView("login-view.fxml",Logout);
    }

    public void goToBookManagement() throws IOException {
        viewModel.openBookManagement();
        loadView("bookmanagement-view.fxml" , bookManagement);
    }

    public void goToUserManagement(ActionEvent event) throws IOException {
        viewModel.openUserManagement();
        loadView("Usersmanagement-view.fxml" , userManagement);
    }

    public void backToMain() {
        SceneNavigator.backToMain(backToMain);
    }
}