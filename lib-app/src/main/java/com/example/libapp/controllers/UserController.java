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
    private Label messageLabel;

    private final UserViewModel viewModel = new UserViewModel();

    @FXML
    public void initialize() {
        messageLabel.textProperty().bind(viewModel.messageProperty());
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
        Stage stage = (Stage) usernameField.getScene().getWindow();
        stage.close();
    }
}