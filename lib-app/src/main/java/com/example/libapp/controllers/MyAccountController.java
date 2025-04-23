package com.example.libapp.controllers;

import com.example.libapp.model.User;
import com.example.libapp.viewmodel.MyAccountViewModel;
import com.example.libapp.SessionManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.stage.Stage;

import java.io.IOException;

import static com.example.libapp.utils.SceneNavigator.loadView;

public class MyAccountController {
    @FXML
    public Button AI;
    @FXML
    public Button addBook;
    @FXML
    public Button bookManage;
    @FXML
    public Button userManagement;
    @FXML
    public Button myAccount;
    @FXML
    public Button returnBook;
    @FXML
    public Button logout;
    @FXML
    public Label nameAccount;
    @FXML
    public Label Role;
    @FXML
    public Label BorrowedBooks;
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
    public void initialize(){
        User currentUser = SessionManager.getInstance().getLoggedInUser();
        if (currentUser != null) {
            nameAccount.setText("Name: " + currentUser.getUsername());
            Role.setText("Role: " + currentUser.getRole());
            BorrowedBooks.setText("Borrowed: " + "0");
        } else {
            nameAccount.setText("Không có người dùng");
            Role.setText("Không xác định");
            BorrowedBooks.setText("0");
        }
    }

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

    public void openMyAccount() throws IOException{
        viewModel.openmyaccount();
        loadView("my-account.fxml",myAccount);
    }

    public void returnBooks(ActionEvent event) {
    }

    public void goToAI(ActionEvent event) throws IOException {
        viewModel.openAI();
        loadView("AI-view.fxml" , AI);
    }

    public void Logout() throws IOException {
        viewModel.logout();
        loadView("login-view.fxml",logout);
    }

}