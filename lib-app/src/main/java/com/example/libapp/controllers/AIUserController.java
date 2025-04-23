package com.example.libapp.controllers;

import com.example.libapp.utils.SceneNavigator;
import com.example.libapp.viewmodel.MainViewModel;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

import java.io.IOException;

import static com.example.libapp.utils.SceneNavigator.loadView;

public class AIUserController {
    @FXML
    public Button AI;
    @FXML
    public Button myAccount;
    @FXML
    public Button addBook;
    @FXML
    public Button bookManage;
    @FXML
    public Button userManagement;
    @FXML
    public Button logout;
    @FXML
    public Button backToMain;
    private final MainViewModel viewModel = new MainViewModel();
    public Button returnBook;
    public Button borrowBooks;

    public void openMyAccount() throws IOException {
        viewModel.openMyAccount();
        loadView("User-my-account-view.fxml", myAccount);
    }

    public void Logout() throws IOException {
        viewModel.logout();
        loadView("login-view.fxml", logout);
    }

    public void backToMain(ActionEvent event) {
        SceneNavigator.backToMain(backToMain);
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
}

