package com.example.libapp.controllers;

import com.example.libapp.viewmodel.LoginViewModel;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class LoginController {
    @FXML
    private TextField usernameField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private Label errorLabel;

    private LoginViewModel loginViewModel = new LoginViewModel();

    @FXML
    private void handleLogin() {
        String username = usernameField.getText();
        String password = passwordField.getText();
        if (loginViewModel.login(username, password)) {
            errorLabel.setText("Login successful!");
        } else {
            errorLabel.setText("Invalid username or password.");
        }
    }
}