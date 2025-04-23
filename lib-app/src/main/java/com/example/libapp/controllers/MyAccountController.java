package com.example.libapp.controllers;

import com.example.libapp.viewmodel.MyAccountViewModel;
import com.example.libapp.SessionManager;
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

public class MyAccountController {
    public Button AI;
    public Button addBook;
    public Button bookManage;
    public Button userManagement;
    public Button myAccount;
    public Button returnBook;
    @FXML
    private Label usernameLabel;
    @FXML
    private Label emailLabel;
    @FXML
    private Label fullNameLabel;
    @FXML
    private Label roleLabel;
    @FXML
    private Button backToMain;
    private final MyAccountViewModel viewModel = new MyAccountViewModel();
    //@FXML
    //public void initialize() {
      //  usernameLabel.textProperty().bind(viewModel.usernameProperty());
       // emailLabel.textProperty().bind(viewModel.emailProperty());
        //fullNameLabel.textProperty().bind(viewModel.fullNameProperty());
       // roleLabel.textProperty().bind(viewModel.roleProperty());
       // viewModel.loadUserInfo(SessionManager.getInstance().getLoggedInUser());
   // }

    public void addNewBook(ActionEvent event) {

    }

    public void goToBookManage(ActionEvent event) throws IOException {
        viewModel.openBookMangagement();
        loadView("bookmanagement-view.fxml",bookManage);

    }

    public void goToUserManagement() throws IOException {
        viewModel.openUserManagement();
        loadView("Usersmanagement-view.fxml",userManagement);
    }

    public void goToAI() throws IOException {
        viewModel.openAI();
        loadView("AI-view.fxml" , AI);
    }

    public void openMyAccount() throws IOException{
        viewModel.openmyaccount();
        loadView("my-account.fxml",myAccount);
    }

    public void returnBooks(ActionEvent event) {
    }
}