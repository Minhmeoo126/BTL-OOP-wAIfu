package com.example.libapp.controllers;

import com.example.libapp.viewmodel.UserViewModel;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class UserController {
    @FXML
    private TextField usernameField;
    @FXML
    private TextField emailField;
    @FXML
    private TextField fullNameField;
    @FXML
    private Label messageLabel; // Thêm Label để hiển thị thông báo

    private final UserViewModel userViewModel = new UserViewModel();

    @FXML
    public void initialize() {
        messageLabel.textProperty().bind(userViewModel.messageProperty());
    }

    @FXML
    private void handleAddUser() {
        String username = usernameField.getText().trim();
        String email = emailField.getText().trim();
        String fullName = fullNameField.getText().trim();

        if (username.isEmpty() || email.isEmpty() || fullName.isEmpty()) {
            messageLabel.setText("Please fill in all fields.");
            messageLabel.setStyle("-fx-text-fill: red;");
            return;
        }

        userViewModel.addUser(username, "password123", "user", email, fullName); // Default password and role
        if (messageLabel.getText().contains("successfully")) {
            messageLabel.setStyle("-fx-text-fill: green;");
        } else {
            messageLabel.setStyle("-fx-text-fill: red;");
        }
    }

    @FXML
    private void backToMain() {
        Stage stage = (Stage) usernameField.getScene().getWindow();
        stage.close();
    }
}