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
            logger.warning("Login failed: Username or password is empty.");
            return null;
        }

        try {
            List<User> users = userDAO.getAllUsers();
            logger.info("Retrieved " + users.size() + " users from the database.");
            for (User user : users) {
                logger.info("Checking user: " + user.getUsername() + ", Role: " + user.getRole());
                if (user.getUsername().equals(username)) {
                    logger.info("Username match found: " + username);
                    if (user.getPassword().equals(password)) {
                        logger.info("Password match for username: " + username);
                        if (user.getRole().equalsIgnoreCase(role)) {
                            message.set("Login successful!");
                            logger.info("Login successful for username: " + username + ", Role: " + role);
                            return user;
                        } else {
                            message.set("Invalid role. Expected: " + role + ", Found: " + user.getRole());
                            logger.warning("Role mismatch for username: " + username + ". Expected: " + role + ", Found: " + user.getRole());
                            return null;
                        }
                    } else {
                        message.set("Incorrect password");
                        logger.warning("Incorrect password for username: " + username + ". Entered: " + password + ", Expected: " + user.getPassword());
                        return null;
                    }
                }
            }
            message.set("Username " + username + " not found");
            logger.warning("Username not found: " + username);
            return null;
        } catch (RuntimeException e) {
            message.set("Error accessing database: " + e.getMessage());
            logger.severe("Login failed: " + e.getMessage());
            return null;
        }
    }
}