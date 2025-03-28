package com.example.libapp.controllers;

import com.example.libapp.viewmodel.UserViewModel;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

public class UserController {
    @FXML
    private TextField usernameField;
    @FXML
    private TextField emailField;
    @FXML
    private TextField fullNameField;

    private UserViewModel userViewModel = new UserViewModel();

    @FXML
    private void handleAddUser() {
        String username = usernameField.getText();
        String email = emailField.getText();
        String fullName = fullNameField.getText();
        userViewModel.addUser(username, "password123", "user", email, fullName); // Default password and role
    }
}