package com.example.libapp.viewmodel;

import com.example.libapp.model.User;
import com.example.libapp.persistence.UserDAO;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.util.List;
import java.util.logging.Logger;

public class LoginViewModel {
    private static final Logger logger = Logger.getLogger(LoginViewModel.class.getName());
    private final UserDAO userDAO = new UserDAO();
    private final StringProperty message = new SimpleStringProperty("");

    public StringProperty messageProperty() {
        return message;
    }

    public User login(String username, String password, String role) {
        if (username.isEmpty() || password.isEmpty()) {
            message.set("Username and password cannot be empty.");
            return null;
        }

        try {
            List<User> users = userDAO.getAllUsers();
            for (User user : users) {
                if (user.getUsername().equals(username)) {
                    if (user.getPassword().equals(password)) {
                        if (user.getRole().equalsIgnoreCase(role)) {
                            message.set("Login successful!");
                            logger.info("Login successful for username: " + username);
                            return user;
                        } else {
                            message.set("Invalid role. Expected: " + role + ", Found: " + user.getRole());
                            return null;
                        }
                    } else {
                        message.set("Incorrect password");
                        return null;
                    }
                }
            }
            message.set("Username " + username + " not found");
            return null;
        } catch (RuntimeException e) {
            message.set("Error accessing database: " + e.getMessage());
            logger.severe("Login failed: " + e.getMessage());
            return null;
        }
    }
}