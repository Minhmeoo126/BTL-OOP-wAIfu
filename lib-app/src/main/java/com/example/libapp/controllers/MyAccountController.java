package com.example.libapp.controllers;

import com.example.libapp.model.User;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class MyAccountController {
    @FXML
    private Label usernameLabel;
    @FXML
    private Label emailLabel;
    @FXML
    private Label fullNameLabel;
    @FXML
    private Label roleLabel;
/**
    @FXML
    public void initialize() {
        User user = LoginController.getLoggedInUser();
        if (user != null) {
            usernameLabel.setText("Username: " + user.getUsername());
            emailLabel.setText("Email: " + user.getEmail());
            fullNameLabel.setText("Full Name: " + user.getFullName());
            roleLabel.setText("Role: " + user.getRole());
        }
    }
*/
    @FXML
    private void backToMain() {
        Stage stage = (Stage) usernameLabel.getScene().getWindow();
        stage.close();
    }
}