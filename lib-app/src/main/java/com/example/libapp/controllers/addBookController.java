package com.example.libapp.controllers;

import com.example.libapp.SessionManager;
import com.example.libapp.model.User;
import com.example.libapp.utils.SceneNavigator;
import com.example.libapp.viewmodel.MainViewModel;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;


import java.io.IOException;

import static com.example.libapp.utils.SceneNavigator.loadView;

public class addBookController {

    public Button AI;
    public Button backToMain;
    public Button myAccount;
    public Button addBook;
    public Button bookManagement;
    public Button userManagement;
    public Button logout;
    public Label UserName;
    private final MainViewModel viewModel = new MainViewModel();

    public void initialize(){
        User currentUser = SessionManager.getInstance().getLoggedInUser();
        if (currentUser != null) {
            UserName.setText(currentUser.getUsername());
        } else{
            UserName.setText("khong co nguoi dung");
        }
    }
    public void backToMain() {
        SceneNavigator.backToMain(backToMain);
    }

    public void openMyAccount() throws IOException {
        viewModel.openMyAccount();
        loadView("my-account.fxml" , myAccount);

    }

    public void addNewBook() throws IOException {
        viewModel.openAddBook();
        loadView("add-book-view.fxml",addBook);
    }

    public void goToBookManagement() throws IOException {
        viewModel.openBookManagement();
        loadView("bookmanagement-view.fxml" , bookManagement);
    }

    public void goToUserManagement() throws IOException {
        viewModel.openUserManagement();
        loadView("Usersmanagement-view.fxml" , userManagement);
    }

    public void openAI() throws IOException {
        viewModel.openAI();
        loadView("AI-view.fxml" , AI);
    }

    public void Logout() throws IOException {
        viewModel.logout();
        loadView("login-view.fxml" ,logout);
    }
}

