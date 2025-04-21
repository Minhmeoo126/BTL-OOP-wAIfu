package com.example.libapp.controllers;

import com.example.libapp.viewmodel.UserViewModel;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class UserController {
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
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/libapp/view/main.fxml" ));
            Parent root = loader.load();
            Stage stage = (Stage) backToMain.getScene().getWindow();
            stage.setScene(new Scene(root, 900, 600));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}