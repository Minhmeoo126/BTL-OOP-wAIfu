package com.example.libapp.controllers;

import com.example.libapp.SessionManager;
import com.example.libapp.model.User;
import com.example.libapp.utils.SceneNavigator;
import com.example.libapp.viewmodel.MainViewModel;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;

import java.io.IOException;

import static com.example.libapp.utils.SceneNavigator.loadView;

public class AIController {
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
    @FXML
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
    public void openMyAccount() throws IOException {
        viewModel.openMyAccount();
        loadView("my-account.fxml",myAccount);
    }

    public void addNewBook(ActionEvent event) {
    }

    public void goToBookManage() throws IOException {
        viewModel.openBookManagement();
        loadView("bookmanagement-view.fxml" , bookManage);
    }

    public void goToUserManagement() throws IOException {
        viewModel.openUserManagement();
        loadView("Usersmanagement-view.fxml" , userManagement);
    }

    public void Logout() throws IOException {
        viewModel.logout();
        loadView("login-view.fxml" ,logout);
    }

    public void backToMain(ActionEvent event) {
        SceneNavigator.backToMain(backToMain);
    }

    public void openAI() throws IOException {
        viewModel.openAI();
        loadView("AI-view.fxml" , AI);
    }
}
