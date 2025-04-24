package com.example.libapp.controllers;

import com.example.libapp.SessionManager;
import com.example.libapp.model.User;
import com.example.libapp.persistence.UserDAO;
import com.example.libapp.utils.SceneNavigator;
import com.example.libapp.viewmodel.UserViewModel;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;

import static com.example.libapp.utils.SceneNavigator.loadView;

public class UserController {
    public Button AI;
    public Button myAccount;
    public Button addBook;
    public Button bookManage;
    public Button userManagement;
    public Button logout;
    public Label UserName;
    @FXML
    private TableView<User> UsersTable;

    @FXML
    private TableColumn<User, String> nameAccountColumn;
    @FXML
    private TableColumn<User, String> PassWordColumn;
    @FXML
    private TableColumn<User, String> EmailColumn;
    @FXML
    private TableColumn<User, String> fullNameColumn;

    private final ObservableList<User> users = FXCollections.observableArrayList();

    @FXML
    private Button backToMain;
    @FXML
    private TextField usernameField;
    @FXML
    private TextField emailField;
    @FXML
    private TextField fullNameField;
    @FXML
    private Label messageLabel;

    private final UserViewModel viewModel = new UserViewModel();

    @FXML
    public void initialize() {
        nameAccountColumn.setCellValueFactory(new PropertyValueFactory<>("username"));
        PassWordColumn.setCellValueFactory(new PropertyValueFactory<>("password"));
        EmailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
        fullNameColumn.setCellValueFactory(new PropertyValueFactory<>("fullName"));
        loadUser();
        messageLabel.textProperty().bind(viewModel.messageProperty());
        User currentUser = SessionManager.getInstance().getLoggedInUser();
        if (currentUser != null) {
            UserName.setText(currentUser.getUsername());
        } else {
            UserName.setText("khong co nguoi dung");
        }
    }

    private void loadUser() {
        UserDAO dao = new UserDAO();
        users.setAll(dao.getAllUsers()); // Lấy danh sách từ database
        UsersTable.setItems(users);
    }


    @FXML
    private void handleAddUser() {
        viewModel.addUser(
                usernameField.getText().trim(),
                "password123", // Mật khẩu mặc định, nên thay bằng input
                "USER", // Vai trò mặc định
                emailField.getText().trim(),
                fullNameField.getText().trim()
        );
        messageLabel.setStyle(viewModel.messageProperty().get().contains("successfully") ?
                "-fx-text-fill: green;" : "-fx-text-fill: red;");
    }

    @FXML
    private void backToMain() {
        SceneNavigator.backToMain(backToMain);
    }

    public void openMyAccount() throws IOException {
        viewModel.openMyAccount();
        loadView("my-account.fxml", myAccount);
    }

    public void addNewBook(ActionEvent event) {
    }

    public void goToBookManage() throws IOException {
        viewModel.openBookManage();
        loadView("bookmanagement-view.fxml", bookManage);
    }

    public void goToUserManagement() throws IOException {
        viewModel.openUserManagement();
        loadView("Usersmanagement-view.fxml", userManagement);
    }

    public void goToAI() throws IOException {
        viewModel.openAI();
        loadView("AI-view.fxml", AI);
    }

    public void Logout() throws IOException {
        viewModel.Logout();
        loadView("login-view.fxml", logout);
    }
}