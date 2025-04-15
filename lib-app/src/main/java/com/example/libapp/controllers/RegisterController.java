package com.example.libapp.controllers;

import com.example.libapp.viewmodel.UserViewModel;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import java.io.IOException;

public class RegisterController {
    @FXML
    private Button CreateAccount;
    @FXML
    private PasswordField addUserPassword;
    @FXML
    private TextField addUserName;
    @FXML
    private TextField emailField;
    @FXML
    private TextField fullNameField;
    @FXML
    private CheckBox AdminSelected;
    @FXML
    private CheckBox UserSelected;
    @FXML
    private Label information;

    private final UserViewModel viewModel = new UserViewModel();

    @FXML
    public void initialize() {
        information.textProperty().bind(viewModel.messageProperty());
    }

    @FXML
    public void GoToLogin() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/libapp/view/login-view.fxml"));
            if (loader.getLocation() == null) {
                viewModel.messageProperty().set("Error: login-view.fxml not found");
                return;
            }
            Parent root = loader.load();
            Stage stage = (Stage) CreateAccount.getScene().getWindow();
            stage.setScene(new Scene(root, 900, 600));
            stage.setTitle("Login");
            stage.show();
        } catch (IOException e) {
            viewModel.messageProperty().set("Error loading login view: " + e.getMessage());
        }
    }

    @FXML
    public void Register(ActionEvent event) {
        String username = addUserName.getText().trim();
        String password = addUserPassword.getText();
        String email = emailField.getText().trim();
        String fullName = fullNameField.getText().trim();
        boolean adminSelected = AdminSelected.isSelected();
        boolean userSelected = UserSelected.isSelected();

        if (username.isEmpty() || password.isEmpty() || email.isEmpty() || fullName.isEmpty()) {
            viewModel.messageProperty().set("All fields are required.");
            return;
        }

        if (!adminSelected && !userSelected) {
            viewModel.messageProperty().set("Please select a role (Admin or User).");
            return;
        }

        if (adminSelected && userSelected) {
            viewModel.messageProperty().set("Please select only one role.");
            return;
        }

        String role = adminSelected ? "ADMIN" : "USER";
        viewModel.addUser(username, password, role, email, fullName);
        if (viewModel.messageProperty().get().contains("successfully")) {
            GoToLogin();
        }
    }
}