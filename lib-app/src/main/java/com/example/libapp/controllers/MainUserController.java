package com.example.libapp.controllers;

import com.example.libapp.utils.SceneNavigator;
import com.example.libapp.viewmodel.MainViewModel;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

import java.io.IOException;

import static com.example.libapp.utils.SceneNavigator.loadView;

public class MainUserController {
    @FXML
    public Button AI;
    @FXML
    public Button backToMain;
    @FXML
    public Button myAccount;
    @FXML
    public Button returnBook;
    @FXML
    public Button borrowBooks;
    @FXML
    public Button logout;
    private final MainViewModel viewModel = new MainViewModel();

    public void backToMain() {
        SceneNavigator.backToMain(backToMain);
    }

    public void openMyAccount() throws IOException {
        viewModel.openMyAccount();
        loadView("User-my-account-view.fxml", myAccount);
    }

    public void openReturnBook() throws IOException {
        viewModel.openReturnBook();
        loadView("return-book.fxml", returnBook);
    }

    public void openBorrowBook() throws IOException {
        viewModel.openBorrowBook();
        loadView("borrow-book.fxml", borrowBooks);
    }

    public void openAI() throws IOException {
        viewModel.openAI();
        loadView("User-Ai-view.fxml", AI);
    }

    public void Logout() throws IOException {
        viewModel.logout();
        loadView("login-view.fxml", logout);
    }
}
