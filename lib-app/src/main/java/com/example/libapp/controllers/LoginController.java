package com.example.libapp.controllers;

import com.example.libapp.model.User;
import com.example.libapp.persistence.UserDAO;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class LoginController {
    @FXML
    private TextField usernameField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private Label errorLabel;

    private UserDAO userDAO = new UserDAO();
    private static User loggedInUser; // Lưu thông tin người dùng đã đăng nhập

    @FXML
    private void handleLogin() throws IOException {
        String username = usernameField.getText().trim();
        String password = passwordField.getText().trim();

        if (username.isEmpty() || password.isEmpty()) {
            errorLabel.setText("Please enter both username and password.");
            return;
        }

        for (User user : userDAO.getAllUsers()) {
            if (user.getUsername().equals(username) && user.getPassword().equals(password)) {
                loggedInUser = user; // Lưu người dùng đã đăng nhập
                // Chuyển sang giao diện chính
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/libapp/main.fxml"));
                Parent root = loader.load();
                Stage stage = (Stage) usernameField.getScene().getWindow();
                stage.setScene(new Scene(root));
                stage.setTitle("Library App - Main");
                return;
            }
        }

        errorLabel.setText("Invalid username or password.");
    }

    public static User getLoggedInUser() {
        return loggedInUser;
    }
}